package at.ac.tuwien.workflow.camel;

import java.util.Date;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import at.ac.tuwien.workflow.dao.Ingredient;
import at.ac.tuwien.workflow.dao.Invoice;
import at.ac.tuwien.workflow.dao.Meal;
import at.ac.tuwien.workflow.dao.Order;
import at.ac.tuwien.workflow.dao.PurchaseList;
import at.ac.tuwien.workflow.hazelcast.DatabaseBuilder;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;

public final class CamelMainClass {
    
	//twitter account credentials
		private static String consumerKey = "DIEFSMeTdUOyng3caQu95oLbA";
	    private static String consumerSecret = "uA4rUObPXJhcFcN3UtDhcxxSsw5cpKcjjYV7tYubnlAk8Xa2eB";
	    private static String accessToken = "2525488392-5gnV3UkCw6TVamQA7TEfybCyKF0FgzieacSucGK";
	    private static String accessTokenSecret = "RAayJ7wP4oEwEwH1Z5Zgwu2xSefbXvkPyFC5WhZZPHsWH";
	
    public static void main(String args[]) throws Exception {
    	
        CamelContext context = new DefaultCamelContext();
        
        //from example jmsToFile
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        context.addComponent("foodSupplyCruise-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        CruiseRouteBuilder rb = new CruiseRouteBuilder();
        
        //setup Twitter authentification
        rb.setAccessToken(accessToken);
        rb.setAccessTokenSecret(accessTokenSecret);
        rb.setConsumerKey(consumerKey);
        rb.setConsumerSecret(consumerSecret);

        context.addRoutes(rb);
        
        ProducerTemplate template = context.createProducerTemplate();
        
        context.start();
        
        //start Hazelcast Database to get purchaselist from caboose
        DatabaseBuilder.buildDatabase();
        
		//nonPricesMeal
		PurchaseList purchListNonPriced = new PurchaseList(new Date(),"lunch");
        
		Meal carbonaraNonPriced = new Meal("Spaghetti a la Carbonara", true);
		carbonaraNonPriced.addIngredient(new Ingredient("Spaghetti",60,"kg",300));
		carbonaraNonPriced.addIngredient(new Ingredient("Ham",30,"kg",1000));
		carbonaraNonPriced.addIngredient(new Ingredient("Cream",10,"l",200));
		carbonaraNonPriced.addIngredient(new Ingredient("Eggs",600,"units",600));
		carbonaraNonPriced.addIngredient(new Ingredient("Parmesan",10,"kg",650));
		purchListNonPriced.addMeal(carbonaraNonPriced);
		
		Meal tiramisuNonPriced = new Meal("Tiramisu", false);
		tiramisuNonPriced.addIngredient(new Ingredient("Eggs",200,"units",200));
		tiramisuNonPriced.addIngredient(new Ingredient("Sugar",5,"kg",30));
		tiramisuNonPriced.addIngredient(new Ingredient("Mascarpone",20,"kg",400));
		tiramisuNonPriced.addIngredient(new Ingredient("Ladyfingers",15,"kg",500));
		tiramisuNonPriced.addIngredient(new Ingredient("Coffee liqueur",3,"l",150));
		tiramisuNonPriced.addIngredient(new Ingredient("Kakao powder",1,"kg",50));
		purchListNonPriced.addMeal(tiramisuNonPriced);
		
		Order order = new Order();
		order.setList(purchListNonPriced);
		order.setDateCreated(new Date());
		order.setOrderNr("723");
		
		//start is the order
		template.sendBody("foodSupplyCruise-jms:queue:orderIn.queue", order);
		
		//We get an invoice from the local retailer including the asked ingredients (incl. price),
		//invoiceDate and invoiceCurrency (here: Fiji Dollars).
		//TODO @luke currency and companyName must be added to the ingredient beans during mail process
		//Invoice invoice = new Invoice(purchListNonPriced.getMeals(), new Date(), "FJD", "Fiji Food Delivery Inc.");
		
		//starts the business process after getting the invoice
		//template.sendBody("foodSupplyCruise-jms:queue:processedMail.queue", invoice);
		template.sendBody("direct:get", "hazelcast:multipmap:foo");
		
		ProducerTemplate templateTwitter = context.createProducerTemplate();
		//templateTwitter.sendBody("direct:tweet", purchList);

		Thread.sleep(10000);
		
        context.stop();
        
        DatabaseBuilder.shutdownDb();
        
    }
    
}
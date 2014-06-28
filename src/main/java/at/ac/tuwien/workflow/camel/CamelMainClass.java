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
        
		//Purchaselist for 400 persons
		Meal carbonara = new Meal("Spaghetti a la Carbonara", true);
		carbonara.addIngredient(new Ingredient("Spaghetti",60,"kg",300));
		carbonara.addIngredient(new Ingredient("Ham",30,"kg",1000));
		carbonara.addIngredient(new Ingredient("Cream",10,"l",200));
		carbonara.addIngredient(new Ingredient("Eggs",600,"units",500));
		carbonara.addIngredient(new Ingredient("Parmesan",10,"kg",650));
		
		PurchaseList purchList = new PurchaseList(new Date(),"lunch");
		purchList.addMeal(carbonara);
		
		//nonPricesMeal
		//TODO @luke prices must be added to the ingredient beans during mail process
		Meal carbonaraNonPriced = new Meal("Spaghetti a la Carbonara", true);
		carbonaraNonPriced.addIngredient(new Ingredient("Spaghetti",60,"kg"));
		carbonaraNonPriced.addIngredient(new Ingredient("Ham",30,"kg"));
		carbonaraNonPriced.addIngredient(new Ingredient("Cream",10,"l"));
		carbonaraNonPriced.addIngredient(new Ingredient("Eggs",600,"units"));
		carbonaraNonPriced.addIngredient(new Ingredient("Parmesan",10,"kg"));
		
		PurchaseList purchListNonPriced = new PurchaseList(new Date(),"lunch");
		purchListNonPriced.addMeal(carbonaraNonPriced);
		
		Order order = new Order();
		order.setList(purchListNonPriced);
		order.setDateCreated(new Date());
		order.setOrderNr("723");
		
		//start is the order
		template.sendBody("foodSupplyCruise-jms:queue:orderIn.queue", order);
		
		//We get an invoice from the local retailer including the asked ingredients (incl. price),
		//invoiceDate and invoiceCurrency (here: Fiji Dollars).
		Invoice invoice = new Invoice(purchList.getMeals(), new Date(), "FJD");
		
		//starts the business process after getting the invoice
		template.sendBody("foodSupplyCruise-jms:queue:processedMail.queue", invoice);
		
		ProducerTemplate templateTwitter = context.createProducerTemplate();
		//templateTwitter.sendBody("direct:tweet", purchList);

		Thread.sleep(30000);
		
        context.stop();
    }
    
}
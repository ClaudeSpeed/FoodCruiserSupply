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
import at.ac.tuwien.workflow.dao.PurchaseList;

public final class CamelMainClass {
    
	//twitter account credentials
	private static String consumerKey = "WuRNypprsexhZtWmjOC0qa95y";
    private static String consumerSecret = "7e2mdWsXpZlCxe3cj1fQFzH9xJXDR57ORo5a4gYzPEQlowh5bg";
    private static String accessToken = "1426115462-pLnbFV2yDQZc4c2sWEbY0HeWsPNOoaZvcFG0LB2";
    private static String accessTokenSecret = "0pSYQE4xFVi9GlaIWCP3BpOrM2BJwUcTPvbkaBG12Sg1j";
	
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
		Meal carbonara = new Meal("Spaghetti a la Carbonara",true);
		carbonara.addIngredient(new Ingredient("Spaghetti",60,"kg",300));
		carbonara.addIngredient(new Ingredient("Ham",30,"kg",1000));
		carbonara.addIngredient(new Ingredient("Cream",10,"l",200));
		carbonara.addIngredient(new Ingredient("Eggs",600,"units",500));
		carbonara.addIngredient(new Ingredient("Parmesan",10,"kg",650));
		
		PurchaseList purchList = new PurchaseList(new Date(),"lunch");
		purchList.addMeal(carbonara);
		
		//We get an invoice from the local retailer including the asked ingredients (incl. price),
		//invoiceDate and invoiceCurrency (here: Fiji Dollars).
		Invoice invoice = new Invoice(purchList.getMeals(), new Date(), "FJD");
		
		template.sendBody("foodSupplyCruise-jms:queue:processedMail.queue", invoice);
		
		ProducerTemplate templateTwitter = context.createProducerTemplate();
		templateTwitter.sendBody("direct:tweet", purchList);

		Thread.sleep(5000);
		
        context.stop();
    }
    
}
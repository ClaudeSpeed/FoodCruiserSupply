package at.ac.tuwien.workflow.processors;

import java.util.Collection;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.Ingredient;
import at.ac.tuwien.workflow.dao.Invoice;
import at.ac.tuwien.workflow.dao.Meal;
import at.ac.tuwien.workflow.dao.Order;
import at.ac.tuwien.workflow.dao.PurchaseList;
import at.ac.tuwien.workflow.hazelcast.DatabaseBuilder;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;


public class HazelcastProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		HazelcastInstance hz = DatabaseBuilder.getHz();
        
        MultiMap<Integer, Meal> mmMeals = hz.getMultiMap("meals");
        MultiMap<Integer, Ingredient> mmIngredients = hz.getMultiMap("ingredients");
		
        PurchaseList purchList = new PurchaseList(new Date(),"Lunch");
        
        for (int i = 1; i <= mmMeals.size(); i++) {
        	Collection<Meal> colMeals = mmMeals.get(i);
            for (Meal m : colMeals) {
            	Collection<Ingredient> colIngredients = mmIngredients.get(i);
            	for (Ingredient ingr : colIngredients) {
            		m.addIngredient(ingr);
            	}
            	purchList.addMeal(m);
            }
        }
        
		Order order = new Order();
		order.setList(purchList);
		order.setDateCreated(new Date());
		order.setOrderNr("723");
		
		//We get an invoice from the local retailer including the asked ingredients (incl. price),
		//invoiceDate and invoiceCurrency (here: Fiji Dollars).
		//TODO @luke currency and companyName must be added to the ingredient beans during mail process
		//Invoice invoice = new Invoice(purchList.getMeals(), new Date(), "FJD", "Fiji Food Delivery Inc.");
        
		exchange.getIn().setBody(order);
		//exchange.getIn().setBody(invoice);
		
	}
	
}

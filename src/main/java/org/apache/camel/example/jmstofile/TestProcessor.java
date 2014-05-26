package org.apache.camel.example.jmstofile;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TestProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		
//		PurchaseList pl = exchange.getIn().getMandatoryBody(PurchaseList.class);
//		List<Meal> meals = pl.getMeals();
//		for (Meal meal : meals) {
//			System.out.println(meal.getName());	
//		}
	}

}

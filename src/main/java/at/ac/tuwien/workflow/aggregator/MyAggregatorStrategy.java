package at.ac.tuwien.workflow.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyAggregatorStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null)
			return newExchange;
		
		System.out.println("MyAggregatorStrategy");
		String oldPriceString = oldExchange.getIn().getHeader("TotalPrice", String.class);
		String newPriceString = newExchange.getIn().getHeader("TotalPrice", String.class);
//		oldPriceString = oldPriceString.replace("TotalPrice=", "");
//		newPriceString = newPriceString.replace("TotalPrice=", "");
//		oldPriceString = oldPriceString.replace("€", "").trim();
//		newPriceString = newPriceString.replace("€", "").trim();
		System.out.println("OldPrice:" + oldPriceString);
		System.out.println("NewPrice:" + newPriceString);
		Double oldPrice = Double.parseDouble(oldPriceString);
		Double newPrice = Double.parseDouble(newPriceString);
		if(oldPrice > newPrice)
		{
			return newExchange;
		}
		else
		{
			return oldExchange;
		}
	}

}

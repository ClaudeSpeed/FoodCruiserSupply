package at.ac.tuwien.workflow.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyAggregatorStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null)
			return newExchange;
		
		System.out.println("MyAggregatorStrategy");
		String oldPriceString = oldExchange.getIn().getHeader("Price", String.class);
		String newPriceString = newExchange.getIn().getHeader("Price", String.class);
		oldPriceString = oldPriceString.replace("Price=", "");
		newPriceString = newPriceString.replace("Price=", "");
		oldPriceString = oldPriceString.replace("€", "").trim();
		newPriceString = newPriceString.replace("€", "").trim();
//		System.out.println("OldPrice:" + oldPriceString);
//		System.out.println("NewPrice:" + newPriceString);
		int oldPrice = Integer.parseInt(oldPriceString);
		int newPrice = Integer.parseInt(newPriceString);
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

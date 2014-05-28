package at.ac.tuwien.workflow.processors;

import java.text.SimpleDateFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.PurchaseList;

public class TwitterProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		PurchaseList purchList = exchange.getIn().getBody(PurchaseList.class);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		String tweetStr = sdf.format(purchList.getMenueDate()) 
				+ " Dear passengers, enjoy this delicious highlight in our caboose for " 
				+ purchList.getMenueType() + ": " + purchList.getHighlights();
		
		exchange.getIn().setBody(tweetStr);

	}

}

package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class HttpProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String input = exchange.getIn().getBody(String.class);
		
		System.out.println("jupadu: " + input);
		
		exchange.getIn().setBody(input);
	}
	
}

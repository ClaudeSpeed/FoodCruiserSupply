package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("ERROR");
		System.out.println(exchange.getIn().getBody().getClass());
	}

}

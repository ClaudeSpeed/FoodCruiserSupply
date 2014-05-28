package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ERROR");
	}

}

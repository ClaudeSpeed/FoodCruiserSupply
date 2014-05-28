package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ToObjectProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String pl = exchange.getIn().getMandatoryBody(String.class);
		System.out.println(pl);
	}
}

package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ToMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		exchange.getIn().setHeader("to", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("from", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("subject", "OrderNr " + 1);
		
		//exchange.getIn().addAttachment("attachment",  )
	}

}

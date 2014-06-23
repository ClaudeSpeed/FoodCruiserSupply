package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.Order;

public class ToMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Order order = exchange.getIn().getBody(Order.class);
		String i = order.getOrderNr();
		exchange.getIn().setHeader("to", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("from", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("subject", "OrderNr " + 1);
		
		//exchange.getIn().addAttachment("attachment",  )
	}

}

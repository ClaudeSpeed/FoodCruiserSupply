package at.ac.tuwien.workflow.processors;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.asm.Handle;

import com.thoughtworks.xstream.XStream;

import at.ac.tuwien.workflow.dao.Order;
import at.ac.tuwien.workflow.helper.Helper;

public class ToMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		System.out.println("ToMailProcessor");
		Order order = exchange.getIn().getBody(Order.class);
		
		Helper h = new Helper();
		//Set receipients
		 //
		exchange.getIn().setHeader("to", exchange.getIn().getHeader("from"));;
		//exchange.getIn().setHeader("to", "reailersuperduper@gmail.com");
		exchange.getIn().setHeader("from", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("subject", "OrderNr " + order.getOrderNr());
		//exchange.getIn().setHeader("subject", "OrderNr");
		
		//Translate into xml
		XStream xstream = new XStream();
		String plXML = xstream.toXML(order);
		
		//create xml-file
		File tempFile = File.createTempFile("Order", ".xml");
	    PrintWriter writer = null;
	    try {
	        writer = new PrintWriter(new FileWriter(tempFile));
	        writer.println(plXML);
	    } finally {
	        if (writer != null)
	            writer.close();
	    }
	    
	    //generate msg
	    StringBuilder sb = new StringBuilder();
	    sb.append("Dear Retailer,<br />");
	    sb.append("You won the bidding. <br />");
	    sb.append("Please fullfill the attached order until the 20th of June");
	    sb.append("Sincerly, The FoodSupplyCruiser.");
	    
	    //set Msg
	    exchange.getIn().setBody(sb.toString());
	    //attach file
		exchange.getIn().addAttachment("Order", new DataHandler(new FileDataSource(tempFile)));
		
		//remove this header to let the mail component set the right one
		exchange.getIn().removeHeader("content-type");
		
		System.out.println("EndToMailProcessor");
	}

}

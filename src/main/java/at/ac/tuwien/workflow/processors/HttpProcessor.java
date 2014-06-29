package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class HttpProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//die Klasse macht eigentlich nichts außer den Body in String konvertieren
		//wenn ich den Processor weglasse, funktioniert der HTTP Request jedenfalls nicht mehr
		
		String input = exchange.getIn().getBody(String.class);
		
		System.out.println("Received exchange rate from YahooFinance API: " + input);
		
		exchange.getIn().setBody(input);
	}
	
}

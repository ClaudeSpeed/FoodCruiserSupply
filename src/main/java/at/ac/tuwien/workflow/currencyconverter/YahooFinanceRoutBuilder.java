package at.ac.tuwien.workflow.currencyconverter;

import org.apache.camel.builder.RouteBuilder;
import at.ac.tuwien.workflow.processors.HttpProcessor;

public class YahooFinanceRoutBuilder extends RouteBuilder {
	
	String from;
	String to;
	
    public void configure() {
    	
	    	from("http://quote.yahoo.com/d/quotes.csv?f=l1&s=FJDEUR=X")
	    	.process(new HttpProcessor());
	    	
    }
    
}

package at.ac.tuwien.workflow.currencyconverter;

import org.apache.camel.builder.RouteBuilder;

import at.ac.tuwien.workflow.processors.HttpProcessor;

public class YahooFinanceRoutBuilder extends RouteBuilder {
	
	private String fromCurrency;
	private String toCurrency;
	
    public YahooFinanceRoutBuilder(String from, String to) {
		super();
		fromCurrency = from;
		toCurrency = to;
	}

	public void configure() {
    	
    	from("http://quote.yahoo.com/d/quotes.csv?f=l1&s=" + fromCurrency + toCurrency + "=X")
    	.process(new HttpProcessor())
    	.log("Received exchange rate from YahooFinance API")
    	.to("direct:exchangeRate");
	    	
    }
    
}

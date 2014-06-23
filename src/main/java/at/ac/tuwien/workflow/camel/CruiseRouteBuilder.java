package at.ac.tuwien.workflow.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.twitter.TwitterComponent;

import at.ac.tuwien.workflow.aggregator.MyAggregatorStrategy;
import at.ac.tuwien.workflow.processors.*;

public class CruiseRouteBuilder extends RouteBuilder {
	
	private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
	
    public void configure() {
    	
    	boolean runErrorHandling = true;
    	boolean runWeather = true;
    	boolean runMail = true;
    	boolean runTwitter = false;
    	boolean runCurrencyConverter = false;
    	boolean runFtpStore = false;
    	
    	//errorHandling
    	if (runErrorHandling) {
	        errorHandler(deadLetterChannel("foodSupplyCruise-jms:queue:error.queue")
	           .useOriginalMessage().maximumRedeliveries(5)
	           .onRedelivery(new ErrorProcessor())
	           .redeliveryDelay(5000));
    	}
    	
    	//weather
    	if (runWeather) {
	        from("weather://weatherService?location=Vienna,Austria&mode=JSON")
	        	.process(new WeatherProcessor())
	        	.choice()
	        		.when(header("Rain")).to("file://noRain?fileName=${date:now:yyyy-MM-dd} WeatherData")
	        		.otherwise().to("file://weather?fileName=${date:now:yyyy-MM-dd} WeatherData");
    	}
    	
        //mail
    	if (runMail) {
    		String domain = "gmail.com";
    		String username = "foodsupplycruiser@gmail.com";
    		String password = "foodSC01";

    		Endpoint fromMail = endpoint("imaps://imap." + domain + ":993?username=" + username + "&password=" + password + "&fetchSize=1&searchTerm.subjectOrBody=OrderNr&unseen=true&consumer.delay=60000");
    		Endpoint fromMailPrice = endpoint("imaps://imap." + domain + ":993?username=" + username + "&password=" + password + "&searchTerm.body=Price&unseen=true&consumer.delay=60000&debugMode=false");
    		Endpoint toMail = endpoint("smtps://smtp." + domain + ":465?username=" + username + "&password=" + password);

    		//ask for price for order
    		from("foodSupplyCruise-jms:queue:orderIn.queue").process(new AskPriceProcessor()).to(toMail);
    		
    		//receive priced order
    		from(fromMailPrice)
    		.process(new MyMailProcessor()) //Convert from XML to Object, Set Header
    		.aggregate(header("OrderID"), new MyAggregatorStrategy()) //Decide cheeper price // better OrderID
    			.completionTimeout(1000L)
    			//.log(LoggingLevel.INFO, "nach timeout")
    		.to("foodSupplyCruise-jms:queue:mailbuffer.queue");
    		
    		//send order mail
    		from("foodSupplyCruise-jms:queue:mailbuffer.queue").process(new ToMailProcessor()).to(toMail);
    		
    		//receive confirmMail and translate to Invoice
    		from(fromMail).process(new ToInvoiceProcessor()).to("foodSupplyCruise-jms:queue:processedMail.queue");
    	}
    	
		//push to twitter: https://twitter.com/topalexandru
    	if (runTwitter) {
			TwitterComponent tc = getContext().getComponent("twitter", TwitterComponent.class);
			tc.setAccessToken(accessToken);
			tc.setAccessTokenSecret(accessTokenSecret);
			tc.setConsumerKey(consumerKey);
			tc.setConsumerSecret(consumerSecret);
			from("direct:tweet").process(new TwitterProcessor()).to("twitter://timeline/user");
    	}
		
		//generate report, convert currency, calculate exchange profit
    	if (runCurrencyConverter) {
	        //generate report for accountancy
	    	from("foodSupplyCruise-jms:queue:processedMail.queue")
		    	.process(new GenerateReportProcessor())
		    	.to("foodSupplyCruise-jms:queue:generatedReports.queue");
	    	
	    	//convert curency
	    	from("foodSupplyCruise-jms:queue:generatedReports.queue")
		    	.process(new CurrencyConverterProcessor())
		    	.to("foodSupplyCruise-jms:queue:convertedReports.queue");
	    	
	    	//calculate exchange profit
	    	from("foodSupplyCruise-jms:queue:convertedReports.queue")
		    	.process(new ExchangeProfitProcessor())
	    		.to("foodSupplyCruise-jms:queue:advancedConvertedReports.queue");
	    	
	    	//calculate exchange profit
	    	from("foodSupplyCruise-jms:queue:advancedConvertedReports.queue")
		    	.process(new ReportToFileProcessor())
	    		.to("file://target/upload");
	    	
    	}
		
		//generate report as a file and store on ftp server
    	if (runFtpStore) {
	    	

	    	
	        // configure properties component
	        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
	        pc.setLocation("classpath:ftp.properties");

	        // lets shutdown faster in case of in-flight messages stack up
	        getContext().getShutdownStrategy().setTimeout(10);
	        
	        //store on ftp server
	        //to see result, connect to ftp://81.19.145.70:21 username=hockeystuff_cruiserfoodsupply pass=cfs1234
	        from("file:target/upload?moveFailed=../error")
	            .log("Uploading file ${file:name}")
	            .to("{{ftp.client}}")
	            .log("Uploaded file ${file:name} complete.");
	    	
    	}
    	
    }
    
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}
    
}

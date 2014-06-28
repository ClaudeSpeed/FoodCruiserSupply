package at.ac.tuwien.workflow.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.twitter.TwitterComponent;

import at.ac.tuwien.workflow.processors.CurrencyConverterProcessor;
import at.ac.tuwien.workflow.processors.ErrorProcessor;
import at.ac.tuwien.workflow.processors.ExchangeProfitProcessor;
import at.ac.tuwien.workflow.processors.GenerateReportProcessor;
import at.ac.tuwien.workflow.processors.MyMailProcessor;
import at.ac.tuwien.workflow.processors.ReportToFileProcessor;
import at.ac.tuwien.workflow.processors.ToMailProcessor;
import at.ac.tuwien.workflow.processors.TwitterProcessor;
import at.ac.tuwien.workflow.processors.WeatherProcessor;

public class CruiseRouteBuilder extends RouteBuilder {
	
	private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
	
    public void configure() {
    	
    	boolean runErrorHandling = false;
    	boolean runWeather = false;
    	boolean runMail = false;
    	boolean runTwitter = false;
    	boolean runCurrencyConverter = true;
    	boolean runFtpStore = true;
    	
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
    	
        //send and receive mail
    	if (runMail) {
    		String domain = "gmail.com";
    		String username = "foodsupplycruiser@gmail.com";
    		String password = "foodSC01";

    		Endpoint fromMail = endpoint("imaps://imap." + domain + ":993?username=" + username + "&password=" + password + "&fetchSize=1&searchTerm.subjectOrBody=OrderNr&unseen=true&consumer.delay=60000");
    		Endpoint toMail = endpoint("smtps://smtp." + domain + ":465?username=" + username + "&password=" + password);

    		//SendMail
    		//möglichkeit für content enrichment (receipients)
    		from("foodSupplyCruise-jms:queue:test.queue").marshal().xstream("ISO-8859-1").process(new ToMailProcessor()).to(toMail);

    		//ReceiveMail
    		from(fromMail).process(new MyMailProcessor()).to("file://inbox?fileName=Test");
    		from("file://inbox").unmarshal().xstream("ISO-8859-1").to("foodSupplyCruise-jms:queue:test2.queue");
    		from("file://inbox").unmarshal().xstream("ISO-8859-1")
	    		.choice()
	    		.when(header("subject").contains("reject")).process(new ToMailProcessor()).to(toMail)
	    		.otherwise().to("foodSupplyCruise-jms:queue:processedMail.queue");
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
	    	
    	}
		
		//generate report as a file and store this file on ftp server
    	if (runFtpStore) {
	    	
	    	//generate file
	    	from("foodSupplyCruise-jms:queue:advancedConvertedReports.queue")
		    	.process(new ReportToFileProcessor())
	    		.to("file://target/upload");
	    	
	        //configure properties component
	        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
	        pc.setLocation("classpath:ftp.properties");

	        //lets shutdown faster in case of in-flight messages stack up
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

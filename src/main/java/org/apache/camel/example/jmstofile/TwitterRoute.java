package org.apache.camel.example.jmstofile;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.TwitterComponent;

public class TwitterRoute extends RouteBuilder {

	private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
    
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
	
	@Override
	public void configure() throws Exception {
		//setup for the Twitter Component
		TwitterComponent tc = getContext().getComponent("twitter", TwitterComponent.class);
		tc.setAccessToken(accessToken);
		tc.setAccessTokenSecret(accessTokenSecret);
		tc.setConsumerKey(consumerKey);
		tc.setConsumerSecret(consumerSecret);
		
		//push to timeline
		from("direct:tweet").to("twitter://timeline/user");
	}

}

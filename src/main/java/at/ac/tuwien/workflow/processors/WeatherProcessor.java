package at.ac.tuwien.workflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.weather.*;

import com.google.gson.Gson;

public class WeatherProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		
		Gson gson = new Gson();
		WeatherData weatherData = gson.fromJson(msg.getBody(String.class), WeatherData.class);
		
//		System.out.println(weatherData.getName());
//		
//		System.out.println(weatherData.getClouds().getAll().intValue());
//		System.out.println(weatherData.getMain().getHumidity().intValue());
//		System.out.println(weatherData.getMain().getPressure().intValue());
		if(weatherData.getClouds().getAll().intValue() > 50 && weatherData.getMain().getHumidity().intValue() > 90 && weatherData.getMain().getPressure().intValue() < 1100)
		{
			msg.setBody("It's going to rain. Do something about");
			msg.setHeader("Rain", true);
		}
		else 
			msg.setHeader("Rain", false);
	}
}

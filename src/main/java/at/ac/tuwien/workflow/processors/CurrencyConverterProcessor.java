package at.ac.tuwien.workflow.processors;

import java.text.DecimalFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.currencyconverter.CurrencyConverter;
import at.ac.tuwien.workflow.dao.AccountancyReport;

public class CurrencyConverterProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		AccountancyReport report = exchange.getIn().getBody(AccountancyReport.class);
		
		//The accountancy converts the total amount of the invoice into default currency and 
		//adds it to the report.
		CurrencyConverter converter = new CurrencyConverter();
		converter.convertCurrency(report);
		
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Converted Total Amount: " + df.format(report.getConvertedTotalAmount()) + " " + converter.DEFAULT_CURRENCY);
		
		exchange.getOut().setBody(report);

	}

}

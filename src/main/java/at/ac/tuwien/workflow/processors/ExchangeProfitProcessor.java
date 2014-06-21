package at.ac.tuwien.workflow.processors;

import java.text.DecimalFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.AccountancyReport;

public class ExchangeProfitProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		AccountancyReport report = exchange.getIn().getBody(AccountancyReport.class);
		
		//Then the accountancy calculates the exchangeGain between the date of invoice and the
		//day of payment. (method gives 1 % gain for testing purposes)
		report.calculateExchangeProfit();
		
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Exchange Profit: " + df.format(report.getExchangeProfit()) + " " + report.getConvertedTotalAmountCurrency());
		
		exchange.getIn().setBody(report);

	}

}

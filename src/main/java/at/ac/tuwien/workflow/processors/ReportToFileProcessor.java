package at.ac.tuwien.workflow.processors;

import java.text.DecimalFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.AccountancyReport;
import at.ac.tuwien.workflow.dao.Meal;

public class ReportToFileProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		AccountancyReport report = exchange.getIn().getBody(AccountancyReport.class);		
		
		DecimalFormat df2 = new DecimalFormat("#.##");
		DecimalFormat df4 = new DecimalFormat("#.####");
		String output = "Accountancy report for invoice from " + report.getInvoice().getInvoiceDate() + "\n\n";
		
		output += "Meals:\n";
		for (Meal m : report.getInvoice().getMeals()) {
			output += " - " + m.getName() + "\n";
		}
		
		output += "\n";
		
		output += "Total Amount: " + df2.format(report.getInvoice().getTotalAmount()) + " " +
				report.getInvoice().getTotalAmountCurrency() + "\n";
		
		output += "Date of payment: " + report.getPaymentDate() + "\n";
		
		output += "Exchange Rate " + report.getConvertedTotalAmountCurrency() + "/" + 
				report.getInvoice().getTotalAmountCurrency() + " per invoice date: " +
				df4.format(1/report.getInvoice().getRateOfInvoiceDate()) + "\n";
		
		output += "Exchange Rate " + report.getConvertedTotalAmountCurrency() + "/" + 
				report.getInvoice().getTotalAmountCurrency() + " per payment date: " +
				df4.format(report.getRateOfPaymentDate()) + "\n";
		
		output += "Converted Total Amount: " + df2.format(report.getConvertedTotalAmount()) + "\n";
		
		output += "Exchange Profit: " + df2.format(report.getExchangeProfit()) + "\n";
		
		exchange.getIn().setBody(output);

	}

}

package at.ac.tuwien.workflow.processors;

import java.text.DecimalFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.AccountancyReport;
import at.ac.tuwien.workflow.dao.Invoice;

public class GenerateReportProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Invoice invoice = exchange.getIn().getBody(Invoice.class);
		
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Invoice Total Amount: " + df.format(invoice.getTotalAmount()) + " " + invoice.getTotalAmountCurrency());
		
		//The invoice will get paid by us after some time and we create a report for the accountancy,
		//including the settled invoice. The settle() saves the date of payment and the actual exchange rate.
		AccountancyReport report = new AccountancyReport(invoice);
		report.settleInvoice();
		
		exchange.getIn().setBody(report);
	}

}

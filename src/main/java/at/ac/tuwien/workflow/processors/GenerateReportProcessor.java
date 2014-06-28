package at.ac.tuwien.workflow.processors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
		
		//read and increment actual sequence number
		report.setSequNr(getCurrentSequenceNumber());
		
		report.settleInvoice();
		
		exchange.getIn().setBody(report);
	}

	public static int getCurrentSequenceNumber() throws IOException  {
		String path = "target/reportTemplate/sequenceNumber.txt";
		Charset encoding = StandardCharsets.UTF_8;
		
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		int sequNr = Integer.parseInt(new String(encoded, encoding));
		
		incrementSequenceNumber(sequNr);
		
		return sequNr;
	}
	
	public static void incrementSequenceNumber(int actSequNr) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("target/reportTemplate/sequenceNumber.txt", "UTF-8");
		int newSequNr = actSequNr + 1;
		writer.print(newSequNr);
		writer.close();
	}
	
}

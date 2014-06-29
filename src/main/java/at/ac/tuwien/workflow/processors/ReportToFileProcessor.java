package at.ac.tuwien.workflow.processors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import at.ac.tuwien.workflow.dao.AccountancyReport;
import at.ac.tuwien.workflow.dao.Meal;

public class ReportToFileProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		AccountancyReport report = exchange.getIn().getBody(AccountancyReport.class);		
		
		DecimalFormat df2 = new DecimalFormat("#.00");
		DecimalFormat df4 = new DecimalFormat("#.0000");
		String output = "Accountancy report Nr " + report.getSequNr() + "for invoice from " + 
		report.getInvoice().getCompanyName() + ", " + report.getInvoice().getInvoiceDate() + "\n\n";
		
		String mealsStr = "";
		for (Meal m : report.getInvoice().getMeals()) {
			mealsStr += "- " + m.getName() + "\n";
		}
		
		output += "Meals:\n" + mealsStr;

		
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
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		DocumentTemplate template = documentTemplateFactory.getTemplate(new File("config/reportTemplate.odt"));
		Map<String, String> data = new HashMap<String, String>();
		data.put("sequNr", String.valueOf(report.getSequNr()));
		data.put("reportDate", dateFormat.format(report.getCreationDate()));
		data.put("companyName", report.getInvoice().getCompanyName());
		data.put("invoiceDate", report.getInvoice().getInvoiceDate().toString());
		data.put("meals", mealsStr);
		data.put("totalInvAmount", df2.format(report.getInvoice().getTotalAmount()) + " " +
				report.getInvoice().getTotalAmountCurrency());
		data.put("dateOfPayment", report.getPaymentDate().toString());
		data.put("currencies", report.getConvertedTotalAmountCurrency() + "/" + 
				report.getInvoice().getTotalAmountCurrency());
		data.put("exchRateInv", df4.format(1/report.getInvoice().getRateOfInvoiceDate()));
		data.put("exchRatePay", df4.format(report.getRateOfPaymentDate()));
		data.put("convInvAmount", df2.format(report.getConvertedTotalAmount() + report.getExchangeProfit()) + " " + report.getConvertedTotalAmountCurrency());
		data.put("convAmountPayed", df2.format(report.getConvertedTotalAmount()) + " " + report.getConvertedTotalAmountCurrency());
		data.put("exchProfit", df2.format(report.getExchangeProfit()) + " " + report.getConvertedTotalAmountCurrency());
		
		template.createDocument(data, new FileOutputStream("target/tempReportOdts/report-" + report.getSequNr() + ".odt"));
		
		File inputFile = new File("target/tempReportOdts/report-" + report.getSequNr() + ".odt");
		File outputFile = new File("target/reportPdfs/report-" + report.getSequNr() + ".pdf");
		 
		// connect to an OpenOffice.org instance running on port 8100
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
		 
		// convert
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		converter.convert(inputFile, outputFile);
		 
		// close the connection
		connection.disconnect();

		exchange.getIn().setBody(output);

	}
	
	public void moveFile(String quelle, String ziel){
		 
		try {
			File quellDatei = new File(quelle);
			File zielDatei = new File(ziel);
			quellDatei.renameTo(zielDatei);
		} catch (Exception e) {
			e.printStackTrace();}
	}

}

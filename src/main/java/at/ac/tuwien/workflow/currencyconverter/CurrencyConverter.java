package at.ac.tuwien.workflow.currencyconverter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import at.ac.tuwien.workflow.dao.AccountancyReport;
 
public class CurrencyConverter {
	
	public final String DEFAULT_CURRENCY = "EUR";
	
    public double getRate(String from, String to) {
        try {
        	//TODO -> component
            URL url = new URL("http://quote.yahoo.com/d/quotes.csv?f=l1&s=" + from + to + "=X");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            if (line.length() > 0) {
                return Double.parseDouble(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
 
        return 0;
    }
    
	public double getRate(String to) {
    	return getRate(DEFAULT_CURRENCY, to);
    }
    
    public double convertCurrency(String from, String to, double amount) {
    	return amount * getRate(from, to);
    }
    
    public void convertCurrency(AccountancyReport rp) {
    	
    	String invoiceCurrency = rp.getInvoice().getTotalAmountCurrency();
    	double invoiceAmount = rp.getInvoice().getTotalAmount();
    	
    	rp.setConvertedTotalAmount(invoiceAmount * getRate(invoiceCurrency,DEFAULT_CURRENCY));
    	rp.setConvertedTotalAmountCurrency(DEFAULT_CURRENCY);
    }
 
}
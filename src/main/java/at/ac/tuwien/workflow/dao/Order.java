package at.ac.tuwien.workflow.dao;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

	private static final long serialVersionUID = -121152686779480700L;
	private String orderNr = "";
	private Date dateCreated = new Date();
	private Date dateReceived = new Date();
	private PurchaseList list;
	
	public String getOrderNr() {
		return orderNr;
	}
	public void setOrderNr(String orderNr) {
		this.orderNr = orderNr;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public PurchaseList getList() {
		return list;
	}
	public void setList(PurchaseList list) {
		this.list = list;
	}
	
	
}

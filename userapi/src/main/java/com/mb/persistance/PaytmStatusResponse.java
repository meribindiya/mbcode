package com.mb.persistance;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="tbl_paytm_payment_status")
public class PaytmStatusResponse {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ID;
	private String ORDERID;
	private String TXNID;
	private String BANKTXNID;
	private String TXNAMOUNT;
	private String STATUS;
	private String TXNTYPE;
	private String GATEWAYNAME;
	private String RESPCODE;
	private String RESPMSG;
	private String BANKNAME;
	private String MID;
	private String PAYMENTMODE;
	private String REFUNDAMT;
	private String TXNDATE;
	private Timestamp CREATED_AT;
	private Long USER_ID;
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	public String getTXNID() {
		return TXNID;
	}
	public void setTXNID(String tXNID) {
		TXNID = tXNID;
	}
	public String getBANKTXNID() {
		return BANKTXNID;
	}
	public void setBANKTXNID(String bANKTXNID) {
		BANKTXNID = bANKTXNID;
	}
	public String getTXNAMOUNT() {
		return TXNAMOUNT;
	}
	public void setTXNAMOUNT(String tXNAMOUNT) {
		TXNAMOUNT = tXNAMOUNT;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getTXNTYPE() {
		return TXNTYPE;
	}
	public void setTXNTYPE(String tXNTYPE) {
		TXNTYPE = tXNTYPE;
	}
	public String getGATEWAYNAME() {
		return GATEWAYNAME;
	}
	public void setGATEWAYNAME(String gATEWAYNAME) {
		GATEWAYNAME = gATEWAYNAME;
	}
	public String getRESPCODE() {
		return RESPCODE;
	}
	public void setRESPCODE(String rESPCODE) {
		RESPCODE = rESPCODE;
	}
	public String getRESPMSG() {
		return RESPMSG;
	}
	public void setRESPMSG(String rESPMSG) {
		RESPMSG = rESPMSG;
	}
	public String getBANKNAME() {
		return BANKNAME;
	}
	public void setBANKNAME(String bANKNAME) {
		BANKNAME = bANKNAME;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getPAYMENTMODE() {
		return PAYMENTMODE;
	}
	public void setPAYMENTMODE(String pAYMENTMODE) {
		PAYMENTMODE = pAYMENTMODE;
	}
	public String getREFUNDAMT() {
		return REFUNDAMT;
	}
	public void setREFUNDAMT(String rEFUNDAMT) {
		REFUNDAMT = rEFUNDAMT;
	}
	public String getTXNDATE() {
		return TXNDATE;
	}
	public void setTXNDATE(String tXNDATE) {
		TXNDATE = tXNDATE;
	}
	
	public Timestamp getCREATED_AT() {
		return CREATED_AT;
	}
	public void setCREATED_AT(Timestamp cREATED_AT) {
		CREATED_AT = cREATED_AT;
	}
	public Long getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(Long uSER_ID) {
		USER_ID = uSER_ID;
	}
	@Override
	public String toString() {
		return "PaytmStatusResponse [ID=" + ID + ", ORDERID=" + ORDERID + ", TXNID=" + TXNID + ", BANKTXNID="
				+ BANKTXNID + ", TXNAMOUNT=" + TXNAMOUNT + ", STATUS=" + STATUS + ", TXNTYPE=" + TXNTYPE
				+ ", GATEWAYNAME=" + GATEWAYNAME + ", RESPCODE=" + RESPCODE + ", RESPMSG=" + RESPMSG + ", BANKNAME="
				+ BANKNAME + ", MID=" + MID + ", PAYMENTMODE=" + PAYMENTMODE + ", REFUNDAMT=" + REFUNDAMT + ", TXNDATE="
				+ TXNDATE + ", CREATED_AT=" + CREATED_AT + ", USER_ID=" + USER_ID + "]";
	}
}

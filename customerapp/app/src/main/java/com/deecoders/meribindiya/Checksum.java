package com.deecoders.meribindiya;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Checksum {
    @Expose
    @SerializedName("WEBSITE")
    private String WEBSITE;
    @Expose
    @SerializedName("TXN_AMOUNT")
    private String TXN_AMOUNT;
    @Expose
    @SerializedName("ORDER_ID")
    private String ORDER_ID;
    @Expose
    @SerializedName("MOBILE_NO")
    private String MOBILE_NO;
    @Expose
    @SerializedName("MID")
    private String MID;
    @Expose
    @SerializedName("INDUSTRY_TYPE_ID")
    private String INDUSTRY_TYPE_ID;
    @Expose
    @SerializedName("EMAIL")
    private String EMAIL;
    @Expose
    @SerializedName("CUST_ID")
    private String CUST_ID;
    @Expose
    @SerializedName("CHECKSUMHASH")
    private String CHECKSUMHASH;
    @Expose
    @SerializedName("CHANNEL_ID")
    private String CHANNEL_ID;
    @Expose
    @SerializedName("CALLBACK_URL")
    private String CALLBACK_URL;

    public String getWEBSITE() {
        return WEBSITE;
    }

    public void setWEBSITE(String WEBSITE) {
        this.WEBSITE = WEBSITE;
    }

    public String getTXN_AMOUNT() {
        return TXN_AMOUNT;
    }

    public void setTXN_AMOUNT(String TXN_AMOUNT) {
        this.TXN_AMOUNT = TXN_AMOUNT;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getINDUSTRY_TYPE_ID() {
        return INDUSTRY_TYPE_ID;
    }

    public void setINDUSTRY_TYPE_ID(String INDUSTRY_TYPE_ID) {
        this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCUST_ID() {
        return CUST_ID;
    }

    public void setCUST_ID(String CUST_ID) {
        this.CUST_ID = CUST_ID;
    }

    public String getCHECKSUMHASH() {
        return CHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String CHECKSUMHASH) {
        this.CHECKSUMHASH = CHECKSUMHASH;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getCALLBACK_URL() {
        return CALLBACK_URL;
    }

    public void setCALLBACK_URL(String CALLBACK_URL) {
        this.CALLBACK_URL = CALLBACK_URL;
    }
}

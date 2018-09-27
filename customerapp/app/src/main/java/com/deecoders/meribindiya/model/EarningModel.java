package com.deecoders.meribindiya.model;

import java.util.Date;

public class EarningModel {

    /**
     *  "id": 1,
     "userId": 359,
     "orderId": 150,
     "amount": 3,
     "createdat": "2018-09-18T13:57:18.000+0000",
     "missed": true
     */
    private int id;
    private int userId;
    private int orderId;
    private double amount;
    private Date createdat;
    private boolean missed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public boolean isMissed() {
        return missed;
    }

    public void setMissed(boolean missed) {
        this.missed = missed;
    }
}

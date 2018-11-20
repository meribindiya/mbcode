package com.mb.persistance;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tbl_user_wallet_transaction")
public class UserWalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long walletId;
    private Double amount;
    private String walletTransactionType;

    @CreationTimestamp
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdat;

    @UpdateTimestamp
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getWalletTransactionType() {
        return walletTransactionType;
    }

    public void setWalletTransactionType(String walletTransactionType) {
        this.walletTransactionType = walletTransactionType;
    }

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    @Override
    public String toString() {
        return "UserWalletTransaction{" +
                "id=" + id +
                ", walletId=" + walletId +
                ", amount=" + amount +
                ", walletTransactionType='" + walletTransactionType + '\'' +
                ", createdat=" + createdat +
                ", updatedat=" + updatedat +
                '}';
    }
}

package com.mb.persistance;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tbl_user_wallet")
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private Long userId;

    private Double amount = 0.0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "walletId")
    private List<UserWalletTransaction> userWalletTransactions;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<UserWalletTransaction> getUserWalletTransactions() {
        return userWalletTransactions;
    }

    public void setUserWalletTransactions(List<UserWalletTransaction> userWalletTransactions) {
        this.userWalletTransactions = userWalletTransactions;
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
        return "UserWallet{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", userWalletTransactions=" + userWalletTransactions +
                ", createdat=" + createdat +
                ", updatedat=" + updatedat +
                '}';
    }
}

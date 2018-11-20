package com.mb.persistance;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tbl_user_earning",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"userId", "orderId"},
                        name="uk_user_order"
                )
        })
public class UserEarnings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long orderId;

    private Double amount;

    @CreationTimestamp
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdat;

    @Column(columnDefinition = "bit default 1")
    private boolean isMissed; // set to false if user has active account.


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

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public boolean isMissed() {
        return isMissed;
    }

    public void setMissed(boolean missed) {
        isMissed = missed;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "UserEarnings{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", createdat=" + createdat +
                ", isMissed=" + isMissed +
                '}';
    }
}

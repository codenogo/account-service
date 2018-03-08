package com.codenogo.accountservice.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Arnold Kizzitoh
 */
@Entity
@Table(name = "account_data")
public class AccountData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "AMOUNT", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "BALANCE", precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Column(name = "TNX_REF_NO", length = 100)
    private String refNo;

    @Column(name = "TRANS_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tnx_time;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Date getTnx_time() {
        return tnx_time;
    }

    public void setTnx_time(Date tnx_time) {
        this.tnx_time = tnx_time;
    }

}

package com.couponkick.svc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "trading_name")
    private String tradingName;

    @Column(name = "abn")
    private String abn;

    @Column(name = "contact")
    private String contact;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "last_logon_date")
    private ZonedDateTime lastLogonDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Address billingAddress;

    @OneToOne
    @JoinColumn(unique = true)
    private Address shippingAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ZonedDateTime getLastLogonDate() {
        return lastLogonDate;
    }

    public void setLastLogonDate(ZonedDateTime lastLogonDate) {
        this.lastLogonDate = lastLogonDate;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address address) {
        this.billingAddress = address;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address address) {
        this.shippingAddress = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Merchant merchant = (Merchant) o;
        if(merchant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, merchant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + id +
            ", merchantId='" + merchantId + "'" +
            ", tradingName='" + tradingName + "'" +
            ", abn='" + abn + "'" +
            ", contact='" + contact + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            ", lastLogonDate='" + lastLogonDate + "'" +
            '}';
    }
}

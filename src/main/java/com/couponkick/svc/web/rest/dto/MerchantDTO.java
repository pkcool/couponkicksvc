package com.couponkick.svc.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Merchant entity.
 */
public class MerchantDTO implements Serializable {

    private Long id;

    private Long merchantId;

    private String tradingName;

    private String abn;

    private String contact;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private ZonedDateTime lastLogonDate;


    private Long billingAddressId;
    
    private Long shippingAddressId;
    
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

    public Long getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Long addressId) {
        this.billingAddressId = addressId;
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long addressId) {
        this.shippingAddressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MerchantDTO merchantDTO = (MerchantDTO) o;

        if ( ! Objects.equals(id, merchantDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MerchantDTO{" +
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

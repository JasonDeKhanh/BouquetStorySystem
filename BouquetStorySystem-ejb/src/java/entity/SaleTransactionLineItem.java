/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author msipc
 */
@Entity
public class SaleTransactionLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTranscationLineItemId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer serialNumber;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice;

    public SaleTransactionLineItem() {
    }

    public SaleTransactionLineItem(Integer serialNumber, Integer quantity, BigDecimal unitPrice) {
    
        this();
        
        this.serialNumber = serialNumber;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    

    public Long getSaleTranscationLineItemId() {
        return saleTranscationLineItemId;
    }

    public void setSaleTranscationLineItemId(Long saleTranscationLineItemId) {
        this.saleTranscationLineItemId = saleTranscationLineItemId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTranscationLineItemId != null ? saleTranscationLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleTransactionLineItem)) {
            return false;
        }
        SaleTransactionLineItem other = (SaleTransactionLineItem) object;
        if ((this.saleTranscationLineItemId == null && other.saleTranscationLineItemId != null) || (this.saleTranscationLineItemId != null && !this.saleTranscationLineItemId.equals(other.saleTranscationLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SaleTransactionLineItem[ id=" + saleTranscationLineItemId + " ]";
    }
    
}

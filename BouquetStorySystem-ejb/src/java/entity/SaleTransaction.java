/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;

/**
 *
 * @author msipc
 */
@Entity
public class SaleTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalLineItem;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalQuantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalAmount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDateTime;
    @Column(nullable = false)
    @NotNull
    private Boolean voidRefund;
    @Column(nullable = false)
    @NotNull
    private Boolean isPreorder;
    
    @OneToMany
    private List<SaleTransactionLineItem> saleTransactionLineItems;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Customer customerEntity;

    
    public SaleTransaction() {
        this.voidRefund = false;
        this.isPreorder = false;
        saleTransactionLineItems = new ArrayList<>();
        
    }

    public SaleTransaction(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, Boolean voidRefund, Boolean isPreorder) {
        
        this();
        
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.voidRefund = voidRefund;
        this.isPreorder = isPreorder;
    }
    
    public SaleTransaction(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, List<SaleTransactionLineItem> saleTransactionLineItems, Boolean voidRefund, Boolean isPreorder)
    {
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.saleTransactionLineItems = saleTransactionLineItems;        
        this.voidRefund = voidRefund;
        this.isPreorder = isPreorder;        
    }
    
    
    

    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Boolean getVoidRefund() {
        return voidRefund;
    }

    public void setVoidRefund(Boolean voidRefund) {
        this.voidRefund = voidRefund;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransactionId != null ? saleTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleTransaction)) {
            return false;
        }
        SaleTransaction other = (SaleTransaction) object;
        if ((this.saleTransactionId == null && other.saleTransactionId != null) || (this.saleTransactionId != null && !this.saleTransactionId.equals(other.saleTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SaleTransaction[ id=" + saleTransactionId + " ]";
    }
    
    public void addSaleTransactionLineItem(SaleTransactionLineItem saleTransactionLineItemEntity) throws EntityInstanceExistsInCollectionException
    {
        if(!this.saleTransactionLineItems.contains(saleTransactionLineItemEntity))
        {
            this.saleTransactionLineItems.add(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Sale Transaction Line Item already exist");
        }
    }
    
    
    
    public void removeSaleTransactionLineItemEntity(SaleTransactionLineItem saleTransactionLineItemEntity) throws EntityInstanceMissingInCollectionException
    {
        if(this.saleTransactionLineItems.contains(saleTransactionLineItemEntity))
        {
            this.saleTransactionLineItems.remove(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Sale Transaction Line Item missing");
        }
    }

    /**
     * @return the saleTransactionLineItems
     */
    public List<SaleTransactionLineItem> getSaleTransactionLineItems() {
        return saleTransactionLineItems;
    }

    /**
     * @param saleTransactionLineItems the saleTransactionLineItems to set
     */
    public void setSaleTransactionLineItems(List<SaleTransactionLineItem> saleTransactionLineItems) {
        this.saleTransactionLineItems = saleTransactionLineItems;
    }

    /**
     * @return the customerEntity
     */
    public Customer getCustomerEntity() {
        return customerEntity;
    }

    /**
     * @param customerEntity the customerEntity to set
     */
    public void setCustomerEntity(Customer customerEntity) {
        this.customerEntity = customerEntity;
    }
    
}

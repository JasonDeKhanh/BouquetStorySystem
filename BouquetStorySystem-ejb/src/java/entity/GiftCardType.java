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
import javax.validation.constraints.Size;

/**
 *
 * @author msipc
 */
@Entity
public class GiftCardType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giftCardTypeId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(nullable = false)
    @NotNull
    private String imgAddress;
    @Column(nullable = false)
    @NotNull
    private String sizeDimensions;
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer quantityOnHand;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer reorderQuantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice;   
    @Column(nullable = false)
    @NotNull
    private Boolean isOnDisplay;



    public GiftCardType() {
    }

    public GiftCardType(String name, String imgAddress, String sizeDimensions, String description, Integer quantityOnHand, Integer reorderQuantity, BigDecimal unitPrice, Boolean isOnDisplay) {
        
        this();
        
        this.name = name;
        this.imgAddress = imgAddress;
        this.sizeDimensions = sizeDimensions;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.unitPrice = unitPrice;
        this.isOnDisplay = isOnDisplay;
    }
    
    
    
    public Long getGiftCardTypeId() {
        return giftCardTypeId;
    }

    public void setGiftCardTypeId(Long giftCardTypeId) {
        this.giftCardTypeId = giftCardTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public String getSizeDimensions() {
        return sizeDimensions;
    }

    public void setSizeDimensions(String sizeDimensions) {
        this.sizeDimensions = sizeDimensions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getIsOnDisplay() {
        return isOnDisplay;
    }

    public void setIsOnDisplay(Boolean isOnDisplay) {
        this.isOnDisplay = isOnDisplay;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (giftCardTypeId != null ? giftCardTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GiftCardType)) {
            return false;
        }
        GiftCardType other = (GiftCardType) object;
        if ((this.giftCardTypeId == null && other.giftCardTypeId != null) || (this.giftCardTypeId != null && !this.giftCardTypeId.equals(other.giftCardTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GiftCardType[ id=" + giftCardTypeId + " ]";
    }
    
}

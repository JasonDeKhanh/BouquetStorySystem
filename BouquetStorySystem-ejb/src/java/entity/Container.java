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
public class Container implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long containerId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String color;
    @Column(nullable = false)
    @NotNull
    private String imgAddress;
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
    @Min(0)
    private Integer flowerLimit;
    @Column(nullable = false)
    @NotNull
    private Boolean isOnDisplay;

    public Container() {
        this.isOnDisplay = false;
        
    }

    public Container(String color, String imgAddress, String description, Integer quantityOnHand, Integer reorderQuantity, BigDecimal unitPrice, Integer flowerLimit) {
        
        this();
        
        this.color = color;
        this.imgAddress = imgAddress;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.unitPrice = unitPrice;
        this.flowerLimit = flowerLimit;
    }

    
    
    
    
    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
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

    public Integer getFlowerLimit() {
        return flowerLimit;
    }

    public void setFlowerLimit(Integer flowerLimit) {
        this.flowerLimit = flowerLimit;
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
        hash += (containerId != null ? containerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Container)) {
            return false;
        }
        Container other = (Container) object;
        if ((this.containerId == null && other.containerId != null) || (this.containerId != null && !this.containerId.equals(other.containerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Container[ id=" + containerId + " ]";
    }
    
}

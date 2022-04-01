/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.FlowerColorEnum;
import util.enumeration.OccasionEnum;

/**
 *
 * @author msipc
 */
@Entity
public class Flower implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flowerId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(nullable = false)
    @NotNull
    private String imgAddress;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private FlowerColorEnum flowerColor;
//    private List<Occasion> occasions; // bean validation?
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
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private FlowerType flowerType;
    
    
    public Flower() {
//        this.occasions = new ArrayList<Occasion>();
        this.isOnDisplay = false;
    }

    public Flower(String name, String imgAddress, FlowerColorEnum flowerColor, String description, Integer quantityOnHand, Integer reorderQuantity,BigDecimal unitPrice, Boolean isOnDisplay) {
        
        this();
        
        this.name = name;
        this.imgAddress = imgAddress;
        this.flowerColor = flowerColor;
//        this.occasions = occasions;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.unitPrice = unitPrice;
        this.isOnDisplay = isOnDisplay;
    }
    

    
    
    public Long getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(Long flowerId) {
        this.flowerId = flowerId;
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

    public FlowerColorEnum getFlowerColor() {
        return flowerColor;
    }

    public void setFlowerColor(FlowerColorEnum flowerColor) {
        this.flowerColor = flowerColor;
    }

//    public List<Occasion> getOccasions() {
//        return occasions;
//    }
//
//    public void setOccasions(List<Occasion> occasions) {
//        this.occasions = occasions;
//    }

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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
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
        hash += (flowerId != null ? flowerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flower)) {
            return false;
        }
        Flower other = (Flower) object;
        if ((this.flowerId == null && other.flowerId != null) || (this.flowerId != null && !this.flowerId.equals(other.flowerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flower[ id=" + flowerId + " ]";
    }

    /**
     * @return the flowerType
     */
    public FlowerType getFlowerType() {
        return flowerType;
    }

    /**
     * @param flowerType the flowerType to set
     */
    public void setFlowerType(FlowerType flowerType) {
        this.flowerType = flowerType;
    }
    
}

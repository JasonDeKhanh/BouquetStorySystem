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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.OccasionEnum;

/**
 *
 * @author msipc
 */
@Entity
public class PremadeBouquet extends Bouquet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(nullable = false)
    @NotNull
    private String imgAddress;
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal bouquetPrice;   
    private List<OccasionEnum> occasions; // bean validation?
    @Column(nullable = false)
    @NotNull
    private Boolean isOnDisplay;

    public PremadeBouquet() {
        super();
        this.occasions = new ArrayList<>();
        this.isOnDisplay = false;
    }

    public PremadeBouquet(String creatorName, String name, String imgAddress, String description, BigDecimal bouquetPrice, Boolean isOnDisplay) {
        super(creatorName);
        this.name = name;
        this.imgAddress = imgAddress;
        this.description = description;
        this.bouquetPrice = bouquetPrice;
        this.isOnDisplay = isOnDisplay;
    }

    
    // added method
    public Integer getTotalNumOfFlowers()
    {
        Integer total = 0;
        for(Flower flower : this.getFlowers())
        {
            total += this.getFlowerQuantities().get(flower);
        }
        return total;
    }
    
    // added method
    public Integer getTotalNumOfDecorations()
    {
        Integer total = 0;
        for(Decoration decoration : this.getDecorations())
        {
            total += this.getDecorationQuantities().get(decoration);
        }
        
        return total;
    }
    
    @Override
    public String getType() {
        return "PremadeBouquet";
    }
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
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

    public BigDecimal getBouquetPrice() {
        return bouquetPrice;
    }

    public void setBouquetPrice(BigDecimal bouquetPrice) {
        this.bouquetPrice = bouquetPrice;
    }

    public List<OccasionEnum> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<OccasionEnum> occasions) {
        this.occasions = occasions;
    }

    public Boolean getIsOnDisplay() {
        return isOnDisplay;
    }

    public void setIsOnDisplay(Boolean isOnDisplay) {
        this.isOnDisplay = isOnDisplay;
    }
    
    @Override
    public BigDecimal getUnitPrice() {
        return getBouquetPrice();
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PremadeBouquet)) {
            return false;
        }
        PremadeBouquet other = (PremadeBouquet) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PremadeBouquet[ id=" + itemId + " ]";
    }
    
}

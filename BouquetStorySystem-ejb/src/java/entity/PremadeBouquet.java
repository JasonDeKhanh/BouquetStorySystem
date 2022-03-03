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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Column(nullable = false)
    @NotNull
    private Boolean isOnDisplay;

    public PremadeBouquet() {
        super();
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

    public Boolean getIsOnDisplay() {
        return isOnDisplay;
    }

    public void setIsOnDisplay(Boolean isOnDisplay) {
        this.isOnDisplay = isOnDisplay;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bouquetId != null ? bouquetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PremadeBouquet)) {
            return false;
        }
        PremadeBouquet other = (PremadeBouquet) object;
        if ((this.bouquetId == null && other.bouquetId != null) || (this.bouquetId != null && !this.bouquetId.equals(other.bouquetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PremadeBouquet[ id=" + bouquetId + " ]";
    }
    
}

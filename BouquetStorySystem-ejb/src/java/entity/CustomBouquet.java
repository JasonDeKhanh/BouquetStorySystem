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

/**
 *
 * @author msipc
 */
@Entity
public class CustomBouquet extends Bouquet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalPriceAmount;

    public CustomBouquet() {
        super();
    }

    public CustomBouquet(String creatorName, BigDecimal totalPriceAmount) {
        super(creatorName);
        this.totalPriceAmount = totalPriceAmount;
    }
    
    

    public BigDecimal getTotalPriceAmount() {
        return totalPriceAmount;
    }

    public void setTotalPriceAmount(BigDecimal totalPriceAmount) {
        this.totalPriceAmount = totalPriceAmount;
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
        if (!(object instanceof CustomBouquet)) {
            return false;
        }
        CustomBouquet other = (CustomBouquet) object;
        if ((this.bouquetId == null && other.bouquetId != null) || (this.bouquetId != null && !this.bouquetId.equals(other.bouquetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomBouquet[ id=" + bouquetId + " ]";
    }
    
}

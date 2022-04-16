/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author msipc
 */
@Entity
public class Bundle extends Item implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    @Column(nullable = false)
    @NotNull
    private String imgAddress;
    @ManyToOne(optional = true)
    private Promotion promotion;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products;
    private Map<Product, Integer> productQuantities;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalPrice;
    private String description;
    @Column(nullable = false)
    @NotNull
    private Boolean isOnDisplay;

    public Bundle() {
        super();
        this.productQuantities = new HashMap<>();
    }

    public Bundle(String name) {
        super();
        this.name = name;
    }

    public String getType() {
        return "Bundle";
    }
    
    public void updateUnitPrice() {
        totalPrice = new BigDecimal(0);
        for (Map.Entry<Product, Integer> entry:productQuantities.entrySet()) {
            BigDecimal subTotal = entry.getKey().getUnitPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            totalPrice = totalPrice.add(subTotal);
        }
        unitPrice = totalPrice;
        if (promotion != null) {
            unitPrice = totalPrice.multiply(new BigDecimal(100).subtract(new BigDecimal(promotion.getDiscountPercent())))
                    .divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        }
    }
    
    @Override
    public Integer getQuantityOnHand() {
        return null;
    }
    
    @Override
    public void setQuantityOnHand(Integer qty) {
        
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

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
        updateUnitPrice();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Map<Product, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<Product, Integer> productQuantities) {
        this.productQuantities = productQuantities;
        updateUnitPrice();
    }
    
    @Override
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bundle)) {
            return false;
        }
        Bundle other = (Bundle) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bundle[ id=" + itemId + " ]";
    }
    
}

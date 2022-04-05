/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author msipc
 */
@Entity
public class Bundle extends Item implements Serializable {

    private static final long serialVersionUID = 1L;
    private String bundleName;
    @ManyToOne(optional = true)
    private Promotion promotion;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products;

    public Bundle() {
        super();
    }

    public Bundle(String bundleName) {
        super();
        this.bundleName = bundleName;
    }

    
    
    
     public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    @Override
    public BigDecimal getUnitPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (Product product:products) {
            totalPrice = totalPrice.add(product.getUnitPrice());
        }
        return totalPrice;
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

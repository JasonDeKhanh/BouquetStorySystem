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
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author msipc
 */
@Entity
public class GiftCard extends Product implements Serializable {

    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long giftCardId;
    @Column(length = 256)
    @Size(max = 256)
    private String message;
    private String photoImgAddress; // can be null if user doesn't want to upload a photo
    // also, drag and drop position of photo???? how to do? Confirm can but might need to save extra attributes
    @ManyToOne(optional = false)
    private GiftCardType giftCardType;

    
    
    public GiftCard() {
        super();
    }

    public GiftCard(String message, String photoImgAddress) {
        super();
        this.message = message;
        this.photoImgAddress = photoImgAddress;
    }

    @Override
    public String getName() {
        return giftCardType.getName();
    }
    
    @Override
    public String getType() {
        return "GiftCard";
    }
    
    @Override
    public Integer getQuantityOnHand() {
        return giftCardType.getQuantityOnHand();
    }
    @Override
    public void setQuantityOnHand(Integer qty) {
        giftCardType.setQuantityOnHand(qty);
    }
    
    @Override
    public String getImgAddress() {
        return giftCardType.getImgAddress();
    }
    
//    public Long getGiftCardId() {
//        return giftCardId;
//    }
//
//    public void setGiftCardId(Long giftCardId) {
//        this.giftCardId = giftCardId;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhotoImgAddress() {
        return photoImgAddress;
    }

    public void setPhotoImgAddress(String photoImgAddress) {
        this.photoImgAddress = photoImgAddress;
    }

    public GiftCardType getGiftCardType() {
        return giftCardType;
    }

    public void setGiftCardType(GiftCardType giftCardType) {
        this.giftCardType = giftCardType;
    }
    
    @Override
    public BigDecimal getUnitPrice() {
        return giftCardType.getUnitPrice();
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
        if (!(object instanceof GiftCard)) {
            return false;
        }
        GiftCard other = (GiftCard) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GiftCard[ id=" + itemId + " ]";
    }
    
}

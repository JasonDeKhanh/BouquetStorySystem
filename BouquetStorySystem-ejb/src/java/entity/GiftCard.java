/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    
    
    public GiftCard() {
        super();
    }

    public GiftCard(String message, String photoImgAddress) {
        super();
        this.message = message;
        this.photoImgAddress = photoImgAddress;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GiftCard)) {
            return false;
        }
        GiftCard other = (GiftCard) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GiftCard[ id=" + productId + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;
import util.security.GlassFishCryptographicHelper;

/**
 *
 * @author msipc
 */
@Entity
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardId;
    // Bean Validation for this!!
    // also how to encrypt
    @Column(nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    private String ccNum;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String ccHolderName;
    @Column(nullable = false, length = 2)
    @NotNull
    @Size(max = 2)
    private String ccExpiryMonth;
    @Column(nullable = false, length = 2)
    @NotNull
    @Size(max = 2)
    private String ccExpiryYear;

    public CreditCard() {
    }

    public CreditCard(String ccNum, String ccHolderName, String ccExpiryMonth, String ccExpiryYear) {
        this();
       
        this.ccHolderName = ccHolderName;
        this.ccExpiryMonth = ccExpiryMonth;
        this.ccExpiryYear = ccExpiryYear;
        setCcNum(ccNum);
    }
    
    
    public Long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCcNum() {
        CryptographicHelper cryptographicHelper = CryptographicHelper.getInstance();
        GlassFishCryptographicHelper glassFishCryptographicHelper = GlassFishCryptographicHelper.getInstanceOf();
        return cryptographicHelper.doAESDecryption(ccNum, glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionKey(), glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionIv());
       
    }

    public void setCcNum(String ccNum) {
        CryptographicHelper cryptographicHelper = CryptographicHelper.getInstance();
        GlassFishCryptographicHelper glassFishCryptographicHelper = GlassFishCryptographicHelper.getInstanceOf();
        try {
            System.out.println("!!!!!!!!!!!!!!!CCNUM" + new String(cryptographicHelper.doAESEncryption(ccNum, glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionKey(), glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionIv()), cryptographicHelper.getDEFAULT_CHARSET_NAME()));
            this.ccNum = new String(cryptographicHelper.doAESEncryption(ccNum, glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionKey(), glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionIv()), cryptographicHelper.getDEFAULT_CHARSET_NAME());
        } catch(UnsupportedEncodingException ex){
        }
        
    }

    public String getCcHolderName() {
        return ccHolderName;
    }

    public void setCcHolderName(String ccHolderName) {
        this.ccHolderName = ccHolderName;
    }

    public String getCcExpiryMonth() {
        return ccExpiryMonth;
    }

    public void setCcExpiryMonth(String ccExpiryMonth) {
        this.ccExpiryMonth = ccExpiryMonth;
    }

    public String getCcExpiryYear() {
        return ccExpiryYear;
    }

    public void setCcExpiryYear(String ccExpiryYear) {
        this.ccExpiryYear = ccExpiryYear;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardId != null ? creditCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.creditCardId == null && other.creditCardId != null) || (this.creditCardId != null && !this.creditCardId.equals(other.creditCardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCard[ id=" + creditCardId + " ]";
    }
    
}

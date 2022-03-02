/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String ccNum;
    private String ccHolderName;
    private String ccExpiryMonth;
    private String ccExpiryYear;

    public CreditCard() {
    }

    public CreditCard(String ccNum, String ccHolderName, String ccExpiryMonth, String ccExpiryYear) {
        
        this();
        
        this.ccNum = ccNum;
        this.ccHolderName = ccHolderName;
        this.ccExpiryMonth = ccExpiryMonth;
        this.ccExpiryYear = ccExpiryYear;
    }
    
    
    public Long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCcNum() {
        return ccNum;
    }

    public void setCcNum(String ccNum) {
        this.ccNum = ccNum;
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

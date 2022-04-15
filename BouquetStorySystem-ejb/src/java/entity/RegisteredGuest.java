/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

//import com.sun.istack.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;

/**
 *
 * @author msipc
 */
@Entity
public class RegisteredGuest extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;
    // ask prof if we need to include this in UML diagram
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt; 
    
//    @JoinColumn(nullable = false)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addresses;
    
    @JoinColumn(nullable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CreditCard> creditCards;

    public RegisteredGuest() {
        super();
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public RegisteredGuest(String firstName, String lastName, String email,String password ) {
        super(firstName, lastName, email);
        addresses = new ArrayList<>();
        creditCards = new ArrayList<>();
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        setPassword(password);
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password != null) 
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.getSalt()));
        }
        else
        {
            this.password = null;
        }
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegisteredGuest)) {
            return false;
        }
        RegisteredGuest other = (RegisteredGuest) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RegisteredGuest[ id=" + customerId + " ]";
    }

}

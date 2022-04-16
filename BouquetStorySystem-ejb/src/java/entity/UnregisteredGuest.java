/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author msipc
 */
@Entity
public class UnregisteredGuest extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    public UnregisteredGuest() {
        super();
    }
    
    public UnregisteredGuest(String firstName, String lastName, String email){
        super(firstName, lastName, email);
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
        if (!(object instanceof UnregisteredGuest)) {
            return false;
        }
        UnregisteredGuest other = (UnregisteredGuest) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UnregisteredGuest[ id=" + customerId + " ]";
    }
    
}

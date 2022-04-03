/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Address;
import entity.RegisteredGuest;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddressNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdressException;

/**
 *
 * @author xqy11
 */
@Local
public interface AddressSessionBeanLocal {

    public List<Address> retrieveAllAddressesByCustomerId(RegisteredGuest customer);

    public void updateAdress(Address address) throws AddressNotFoundException, UpdateAdressException, InputDataValidationException;

    public Address retrieveAdressByAdressId(Long addressId) throws AddressNotFoundException;

    public Address createNewAddress(Address newAdress) throws UnknownPersistenceException, InputDataValidationException;
    
}

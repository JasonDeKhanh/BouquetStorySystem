/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.RegisteredGuest;
import javax.ejb.Local;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author matt_
 */
@Local
public interface RegisteredGuestSessionBeanLocal {

    public Long createNewRegisteredGuest(RegisteredGuest newRegisteredGuest) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException;

    public RegisteredGuest retrieveRegisteredGuestByCustomerId(Long customerId) throws CustomerNotFoundException;

    public RegisteredGuest retrieveRegisteredGuestByEmail(String email) throws CustomerNotFoundException;

    public RegisteredGuest registeredGuestLogin(String email, String password) throws InvalidLoginCredentialException;

    public void updateRegisteredGuest(RegisteredGuest registeredGuest) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;
    
    
}

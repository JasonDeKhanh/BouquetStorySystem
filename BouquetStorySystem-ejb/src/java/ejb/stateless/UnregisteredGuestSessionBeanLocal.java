/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.UnregisteredGuest;
import javax.ejb.Local;
import util.exception.CustomerEmailExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author matt_
 */
@Local
public interface UnregisteredGuestSessionBeanLocal {

    public Long createNewUnregisteredGuest(UnregisteredGuest newUnregisteredGuest) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException;
    
}

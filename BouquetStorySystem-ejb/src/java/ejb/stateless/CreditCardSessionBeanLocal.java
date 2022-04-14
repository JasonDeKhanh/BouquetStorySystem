/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CreditCard;
import javax.ejb.Local;
import util.exception.CreditCardExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author matt_
 */
@Local
public interface CreditCardSessionBeanLocal {

    public CreditCard createNewCreditCard(Long registeredGuestId, CreditCard newCreditCard) throws CustomerNotFoundException, CreditCardExistException, UnknownPersistenceException, InputDataValidationException;
    
}

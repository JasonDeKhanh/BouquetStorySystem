/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CreditCard;
import entity.Customer;
import entity.RegisteredGuest;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreditCardExistException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;
import util.security.GlassFishCryptographicHelper;

/**
 *
 * @author matt_
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @EJB(name = "RegisteredGuestSessionBeanLocal")
    private RegisteredGuestSessionBeanLocal registeredGuestSessionBeanLocal;

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CreditCardSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public CreditCard createNewCreditCard(Long registeredGuestId, CreditCard newCreditCard) throws CustomerNotFoundException, CreditCardExistException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<CreditCard>>constraintViolations = validator.validate(newCreditCard);
        if(constraintViolations.isEmpty())
        {
            try 
            {   
                RegisteredGuest registeredGuest = registeredGuestSessionBeanLocal.retrieveRegisteredGuestByCustomerId(registeredGuestId);
                registeredGuest.getCreditCards().add(newCreditCard);
                em.persist(newCreditCard);
                em.flush();
                
                return newCreditCard;
            } 
            catch (PersistenceException ex) 
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreditCardExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } 
        else 
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        

    }
    @Override
    public CreditCard retrieveCreditCard(Long cardId) throws CreditCardNotFoundException {
        CreditCard card = em.find(CreditCard.class, cardId);

        if (card != null) {
            return card;
        } else {
            throw new CreditCardNotFoundException("Credit Card ID " + cardId + " does not exist!");
        }
        
    }
    
    @Override
    public List<CreditCard> retrieveRegisteredGuestCreditCards(Long registeredGuestId) throws CustomerNotFoundException {
        RegisteredGuest customer = (RegisteredGuest)em.find(Customer.class, registeredGuestId);
        
        if (customer != null) {
            return customer.getCreditCards();
        } else {
            throw new CustomerNotFoundException("Customer ID " + registeredGuestId + " does not exist!");
        }
        
    }
    
    @Override
    public void deleteCreditCard(Long creditCardId, Long registeredGuestId) throws CreditCardNotFoundException {
        RegisteredGuest customer = (RegisteredGuest)em.find(Customer.class, registeredGuestId);
        CreditCard cardToDelete = retrieveCreditCard(creditCardId);
        customer.getCreditCards().remove(cardToDelete);
        em.remove(cardToDelete);
        
    }
    
    @Override
    public String decryptCcNum(String cipherText){
        CryptographicHelper cryptographicHelper = CryptographicHelper.getInstance();
        GlassFishCryptographicHelper glassFishCryptographicHelper = GlassFishCryptographicHelper.getInstanceOf();
        return cryptographicHelper.doAESDecryption(cipherText, glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionKey(), glassFishCryptographicHelper.getGlassFishDefaultSymmetricEncryptionIv());
       
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCard>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
        
    
   
    
    

}

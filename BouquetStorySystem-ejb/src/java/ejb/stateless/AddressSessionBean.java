/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Address;
import entity.Admin;
import entity.RegisteredGuest;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AdminUsernameExistException;
import util.exception.AddressNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdressException;

/**
 *
 * @author xqy11
 */
@Stateless
public class AddressSessionBean implements AddressSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public AddressSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Address createNewAddress(Address newAdress) throws UnknownPersistenceException, InputDataValidationException{
        Set<ConstraintViolation<Address>>constraintViolations = validator.validate(newAdress);
         if(constraintViolations.isEmpty())
        {
            try 
            {
                em.persist(newAdress);
                em.flush();
                
                return newAdress;
            } 
            catch (PersistenceException ex) 
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } 
        else 
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    // retrieve all
    @Override
    public List<Address> retrieveAllAddressesByCustomerId(RegisteredGuest customer)
    {
        Query query = em.createQuery("SELECT a FROM Address a WHERE a.customer = :inCustomer");
        query.setParameter("inCustomer", customer);
        
        return query.getResultList();
    }
    
    @Override
    public Address retrieveAdressByAdressId(Long addressId) throws AddressNotFoundException
    {
        Address address = em.find(Address.class, addressId);
        
        if(address != null)
        {
            return address;
        }
        else
        {
            throw new AddressNotFoundException("Adress ID " + addressId + " does not exist!");
        }
    }
    
    @Override
    public void updateAdress(Address address) throws AddressNotFoundException, UpdateAdressException, InputDataValidationException
    {
        if(address != null && address.getAddressId()!= null)
        {
            Set<ConstraintViolation<Address>>constraintViolations = validator.validate(address);
        
            if(constraintViolations.isEmpty())
            {
                Address addressToUpdate = retrieveAdressByAdressId(address.getAddressId());
                
                if(addressToUpdate.getAddressId().equals(address.getAddressId()))
                {
                    addressToUpdate.setLine(address.getLine());
                    addressToUpdate.setPostCode(address.getPostCode());
                }
                else
                {
                    throw new UpdateAdressException("ID of address record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new AddressNotFoundException("Address ID not provided for admin to be updated");
        }
        
    }
    
    @Override
    public void deleteAddress(Long addressId) throws AddressNotFoundException
    {
        Address addOnEntityToRemove = retrieveAdressByAdressId(addressId);
        
        em.remove(addOnEntityToRemove);
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Address>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}

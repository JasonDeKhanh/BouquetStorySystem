/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomBouquet;
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
import util.exception.CustomBouquetNotFoundException;
import util.exception.DeleteCustomBouquetException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author msipc
 */
@Stateless
public class CustomBouquetSessionBean implements CustomBouquetSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomBouquetSessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    // Create New CustomBouquet
    public CustomBouquet createNewCustomBouquet(CustomBouquet newCustomBouquet) throws CreateNewCustomBouquetException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<CustomBouquet>>constraintViolations = validator.validate(newCustomBouquet);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newCustomBouquet);
                em.flush();
                
                return newCustomBouquet;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewCustomBouquetException();
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
    
    
    // RetrieveAllCustomBouquets
    public List<CustomBouquet> retrieveAllCustomBouquets()
    {
        Query query = em.createQuery("SELECT cb FROM CustomBouquet cb ORDER BY cb.itemId ASC");
        List<CustomBouquet> customBouquets = query.getResultList();
        
        return customBouquets;
    }
    
    
    // Retrieve Customer Bouquet by Item Id
    public CustomBouquet retrieveCustomBouquetByItemId(Long itemId) throws CustomBouquetNotFoundException
    {
        CustomBouquet customBouquet = em.find(CustomBouquet.class, itemId);
        
        if(customBouquet != null)
        {
            return customBouquet;
        }
        else
        {
            throw new CustomBouquetNotFoundException("Custom Bouquet ID " + itemId + " does not exist!");
        }
    }
    
    
    // Update Custom Bouquet
    public void updateCustomBouquet(CustomBouquet customBouquet) throws CustomBouquetNotFoundException, InputDataValidationException
    {
        if(customBouquet != null && customBouquet.getItemId()!= null)
        {
            Set<ConstraintViolation<CustomBouquet>>constraintViolations = validator.validate(customBouquet);
        
            if(constraintViolations.isEmpty())
            {
                // Do Update
                CustomBouquet customBouquetToUpdate = retrieveCustomBouquetByItemId(customBouquet.getItemId());

                customBouquetToUpdate.setCreatorName(customBouquet.getCreatorName());
                customBouquetToUpdate.setTotalPriceAmount(customBouquet.getTotalPriceAmount());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CustomBouquetNotFoundException("Custom Bouquet ID not provided for giftCard to be updated");
        }
    }
    
    
    
    public void deleteCustomBouquet(Long itemId) throws CustomBouquetNotFoundException, DeleteCustomBouquetException
    {
        CustomBouquet customBouquetToRemove = retrieveCustomBouquetByItemId(itemId);
        
        // check if can delete
        em.remove(customBouquetToRemove);
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomBouquet>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

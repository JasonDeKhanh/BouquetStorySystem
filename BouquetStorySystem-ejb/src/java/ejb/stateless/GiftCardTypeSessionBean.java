/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.GiftCardType;
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
import util.exception.CreateNewGiftCardTypeException;
import util.exception.DeleteGiftCardTypeException;
import util.exception.GiftCardTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateGiftCardTypeException;

/**
 *
 * @author JORD-SSD
 */
@Stateless
public class GiftCardTypeSessionBean implements GiftCardTypeSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public GiftCardTypeSessionBean()
    {
       validatorFactory = Validation.buildDefaultValidatorFactory();
       validator = validatorFactory.getValidator();
    }
    
    @Override
    public GiftCardType createNewGiftCardType(GiftCardType newGiftCardTypeEntity) throws CreateNewGiftCardTypeException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<GiftCardType>>constraintViolations = validator.validate(newGiftCardTypeEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newGiftCardTypeEntity);
                em.flush();
                
                return newGiftCardTypeEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewGiftCardTypeException();
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
    public List<GiftCardType> retrieveAllGiftCardTypes()
    {
        Query query = em.createQuery("SELECT g FROM GiftCardType g ORDER BY g.name ASC");
        List<GiftCardType> giftCardTypeEntities = query.getResultList();
        
        return giftCardTypeEntities;
    }
    
    @Override
    public GiftCardType retrieveGiftCardTypeByGiftCardTypeId(Long giftCardTypeId) throws GiftCardTypeNotFoundException
    {
        GiftCardType giftCardTypeEntity = em.find(GiftCardType.class, giftCardTypeId);
        
        if(giftCardTypeEntity != null)
        {
            return giftCardTypeEntity;
        }
        else
        {
            throw new GiftCardTypeNotFoundException("GiftCardType ID " + giftCardTypeId + " does not exist!");
        }  
    }

    @Override
    public void updateGiftCardType(GiftCardType giftCardTypeEntity) throws GiftCardTypeNotFoundException, UpdateGiftCardTypeException, InputDataValidationException
    {
        if(giftCardTypeEntity != null && giftCardTypeEntity.getGiftCardTypeId()!= null)
        {
            Set<ConstraintViolation<GiftCardType>>constraintViolations = validator.validate(giftCardTypeEntity);
        
            if(constraintViolations.isEmpty())
            {
                GiftCardType giftCardTypeEntityToUpdate = retrieveGiftCardTypeByGiftCardTypeId(giftCardTypeEntity.getGiftCardTypeId());

                giftCardTypeEntityToUpdate.setName(giftCardTypeEntity.getName());
                giftCardTypeEntityToUpdate.setImgAddress(giftCardTypeEntity.getImgAddress());
                giftCardTypeEntityToUpdate.setSizeDimensions(giftCardTypeEntity.getSizeDimensions());
                giftCardTypeEntityToUpdate.setDescription(giftCardTypeEntity.getDescription());
                giftCardTypeEntityToUpdate.setQuantityOnHand(giftCardTypeEntity.getQuantityOnHand());
                giftCardTypeEntityToUpdate.setReorderQuantity(giftCardTypeEntity.getReorderQuantity());
                giftCardTypeEntityToUpdate.setUnitPrice(giftCardTypeEntity.getUnitPrice());
                giftCardTypeEntityToUpdate.setIsOnDisplay(giftCardTypeEntity.getIsOnDisplay());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new GiftCardTypeNotFoundException("GiftCardType ID not provided for giftCardType to be updated");
        }
    }
    
    @Override
    public void deleteGiftCardType(Long giftCardTypeId) throws GiftCardTypeNotFoundException, DeleteGiftCardTypeException
    {
        GiftCardType giftCardTypeEntityToRemove = retrieveGiftCardTypeByGiftCardTypeId(giftCardTypeId);
        
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<GiftCardType>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

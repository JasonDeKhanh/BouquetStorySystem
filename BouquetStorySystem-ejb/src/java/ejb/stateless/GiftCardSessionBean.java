package ejb.stateless;

import entity.GiftCard;
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
import util.exception.CreateNewGiftCardException;
import util.exception.DeleteGiftCardException;
import util.exception.GiftCardNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateGiftCardException;

/**
 *
 * @author JORD-SSD
 */
@Stateless
public class GiftCardSessionBean implements GiftCardSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public GiftCardSessionBean()
    {
       validatorFactory = Validation.buildDefaultValidatorFactory();
       validator = validatorFactory.getValidator();
    }
    
    @Override
    public GiftCard createNewGiftCard(GiftCard newGiftCardEntity) throws CreateNewGiftCardException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<GiftCard>>constraintViolations = validator.validate(newGiftCardEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newGiftCardEntity);
                em.flush();
                
                return newGiftCardEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewGiftCardException();
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
    public List<GiftCard> retrieveAllGiftCards()
    {
        Query query = em.createQuery("SELECT g FROM GiftCard g ORDER BY g.itemId ASC");
        List<GiftCard> giftCardEntities = query.getResultList();
        
        return giftCardEntities;
    }
    
    @Override
    public GiftCard retrieveGiftCardByGiftCardId(Long itemId) throws GiftCardNotFoundException
    {
        GiftCard giftCardEntity = em.find(GiftCard.class, itemId);
        
        if(giftCardEntity != null)
        {
            return giftCardEntity;
        }
        else
        {
            throw new GiftCardNotFoundException("GiftCard ID " + itemId + " does not exist!");
        }
    }
    
    @Override
    public void updateGiftCard(GiftCard giftCardEntity) throws GiftCardNotFoundException, UpdateGiftCardException, InputDataValidationException
    {
        if(giftCardEntity != null && giftCardEntity.getItemId()!= null)
        {
            Set<ConstraintViolation<GiftCard>>constraintViolations = validator.validate(giftCardEntity);
        
            if(constraintViolations.isEmpty())
            {
                GiftCard giftCardEntityToUpdate = retrieveGiftCardByGiftCardId(giftCardEntity.getItemId());


                giftCardEntityToUpdate.setMessage(giftCardEntity.getMessage());
                giftCardEntityToUpdate.setPhotoImgAddress(giftCardEntity.getPhotoImgAddress());
                giftCardEntityToUpdate.setGiftCardType(giftCardEntity.getGiftCardType());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new GiftCardNotFoundException("GiftCard ID not provided for giftCard to be updated");
        }
    }
    
    @Override
    public void deleteGiftCardDecoration(Long itemId) throws GiftCardNotFoundException, DeleteGiftCardException
    {
        GiftCard giftCardEntityToRemove = retrieveGiftCardByGiftCardId(itemId);
        
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<GiftCard>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

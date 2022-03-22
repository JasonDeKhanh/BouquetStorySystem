package ejb.stateless;

import entity.Promotion;
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
import util.exception.CreateNewPromotionException;
import util.exception.DeletePromotionException;
import util.exception.InputDataValidationException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePromotionException;

@Stateless
public class PromotionSessionBean implements PromotionSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PromotionSessionBean()
    {
       validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Promotion createNewPromotion(Promotion newPromotionEntity) throws CreateNewPromotionException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Promotion>>constraintViolations = validator.validate(newPromotionEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newPromotionEntity);
                em.flush();
                
                return newPromotionEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewPromotionException();
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
    public List<Promotion> retrieveAllPromotions()
    {
        Query query = em.createQuery("SELECT p FROM Promotion p ORDER BY p.name ASC");
        List<Promotion> promotionEntities = query.getResultList();
        
        return promotionEntities;
    }
    
    @Override
    public Promotion retrievePromotionByPromotionId(Long promotionId) throws PromotionNotFoundException
    {
        Promotion promotionEntity = em.find(Promotion.class, promotionId);
        
        if(promotionEntity != null)
        {
            return promotionEntity;
        }
        else
        {
            throw new PromotionNotFoundException("Promotion ID " + promotionId + " does not exist!");
        }  
    }
    
    @Override
    public void updatePromotion(Promotion promotionEntity) throws PromotionNotFoundException, UpdatePromotionException, InputDataValidationException
    {
        if(promotionEntity != null && promotionEntity.getPromotionId()!= null)
        {
            Set<ConstraintViolation<Promotion>>constraintViolations = validator.validate(promotionEntity);
        
            if(constraintViolations.isEmpty())
            {
                Promotion promotionEntityToUpdate = retrievePromotionByPromotionId(promotionEntity.getPromotionId());


                promotionEntityToUpdate.setName(promotionEntity.getName());
                promotionEntityToUpdate.setDiscountPercent(promotionEntity.getDiscountPercent());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new PromotionNotFoundException("Promotion ID not provided for promotion to be updated");
        }
    }
    
    @Override
    public void deletePromotionDecoration(Long promotionId) throws PromotionNotFoundException, DeletePromotionException
    {
        Promotion promotionEntityToRemove = retrievePromotionByPromotionId(promotionId);
        
//        List<Bundle> bundleEntities = bundleEntitySessionBeanLocal.retrieveBundlesByPromotionId(promotionId);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Promotion>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}

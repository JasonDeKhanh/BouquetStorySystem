package ejb.stateless;

import entity.AddOn;
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
import util.exception.AddOnNotFoundException;
import util.exception.CreateNewAddOnException;
import util.exception.DeleteAddOnException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddOnException;

/**
 *
 * @author JORD-SSD
 */
@Stateless
public class AddOnSessionBean implements AddOnSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AddOnSessionBean()
    {
       validatorFactory = Validation.buildDefaultValidatorFactory();
       validator = validatorFactory.getValidator();
    }
    
    @Override
    public AddOn createNewAddOn(AddOn newAddOnEntity) throws CreateNewAddOnException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<AddOn>>constraintViolations = validator.validate(newAddOnEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newAddOnEntity);
                em.flush();
                
                return newAddOnEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewAddOnException();
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
    public List<AddOn> retrieveAllAddOns()
    {
        Query query = em.createQuery("SELECT a FROM AddOn a ORDER BY a.name ASC");
        List<AddOn> addOnEntities = query.getResultList();
        
        return addOnEntities;
    }
    
    @Override
    public AddOn retrieveAddOnByAddOnId(Long itemId) throws AddOnNotFoundException
    {
        AddOn addOnEntity = em.find(AddOn.class, itemId);
        
        if(addOnEntity != null)
        {
            return addOnEntity;
        }
        else
        {
            throw new AddOnNotFoundException("AddOn ID " + itemId + " does not exist!");
        }
    }
    
    @Override
    public void updateAddOn(AddOn addOnEntity) throws AddOnNotFoundException, UpdateAddOnException, InputDataValidationException
    {
        if(addOnEntity != null && addOnEntity.getItemId()!= null)
        {
            Set<ConstraintViolation<AddOn>>constraintViolations = validator.validate(addOnEntity);
        
            if(constraintViolations.isEmpty())
            {
                AddOn addOnEntityToUpdate = retrieveAddOnByAddOnId(addOnEntity.getItemId());


                addOnEntityToUpdate.setName(addOnEntity.getName());
                addOnEntityToUpdate.setImgAddress(addOnEntity.getImgAddress());
                addOnEntityToUpdate.setDescription(addOnEntity.getDescription());
                addOnEntityToUpdate.setQuantityOnHand(addOnEntity.getQuantityOnHand());
                addOnEntityToUpdate.setReorderQuantity(addOnEntity.getReorderQuantity());
                addOnEntityToUpdate.setUnitPrice(addOnEntity.getUnitPrice());
                addOnEntityToUpdate.setIsOnDisplay(addOnEntity.getIsOnDisplay());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new AddOnNotFoundException("AddOn ID not provided for addOn to be updated");
        }
    }
    
    @Override
    public void deleteAddOnDecoration(Long itemId) throws AddOnNotFoundException, DeleteAddOnException
    {
        AddOn addOnEntityToRemove = retrieveAddOnByAddOnId(itemId);
        
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AddOn>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

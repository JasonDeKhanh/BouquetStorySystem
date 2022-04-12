package ejb.stateless;

import entity.Bundle;
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
import util.exception.BundleNotFoundException;
import util.exception.CreateNewBundleException;
import util.exception.DeleteBundleException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateBundleException;

/**
 *
 * @author JORD-SSD
 */
@Stateless
public class BundleSessionBean implements BundleSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public BundleSessionBean()
    {
       validatorFactory = Validation.buildDefaultValidatorFactory();
       validator = validatorFactory.getValidator();
    }
    
    @Override
    public Bundle createNewBundle(Bundle newBundleEntity) throws CreateNewBundleException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Bundle>>constraintViolations = validator.validate(newBundleEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newBundleEntity);
                em.flush();
                
                return newBundleEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewBundleException();
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
    public List<Bundle> retrieveAllBundles()
    {
        Query query = em.createQuery("SELECT b FROM Bundle b ORDER BY b.name ASC");
        List<Bundle> bundleEntities = query.getResultList();
        
        return bundleEntities;
    }
    
    @Override
    public Bundle retrieveBundleByItemId(Long itemId) throws BundleNotFoundException
    {
        Bundle bundleEntity = em.find(Bundle.class, itemId);
        
        if(bundleEntity != null)
        {
            return bundleEntity;
        }
        else
        {
            throw new BundleNotFoundException("Bundle ID " + itemId + " does not exist!");
        }  
    }
    
    @Override
    public void updateBundle(Bundle bundleEntity) throws BundleNotFoundException, UpdateBundleException, InputDataValidationException
    {
        if(bundleEntity != null && bundleEntity.getItemId()!= null)
        {
            Set<ConstraintViolation<Bundle>>constraintViolations = validator.validate(bundleEntity);
        
            if(constraintViolations.isEmpty())
            {
                Bundle bundleEntityToUpdate = retrieveBundleByItemId(bundleEntity.getItemId());

                bundleEntityToUpdate.setName(bundleEntity.getName());
                bundleEntityToUpdate.setPromotion(bundleEntity.getPromotion());
                bundleEntityToUpdate.setProductQuantities(bundleEntity.getProductQuantities());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new BundleNotFoundException("Item ID not provided for bundle to be updated");
        }
    }
    
    @Override
    public void deleteBundle(Long itemId) throws BundleNotFoundException, DeleteBundleException
    {
        Bundle bundleEntityToRemove = retrieveBundleByItemId(itemId);
        
        em.remove(bundleEntityToRemove);

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Bundle>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

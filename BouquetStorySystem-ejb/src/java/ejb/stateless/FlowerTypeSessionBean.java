/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Flower;
import entity.FlowerType;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewFlowerTypeException;
import util.exception.DeleteFlowerTypeException;
import util.exception.UpdateFlowerTypeException;
import util.exception.FlowerTypeNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author xqy11
 */
@Stateless
public class FlowerTypeSessionBean implements FlowerTypeSessionBeanLocal {

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public FlowerTypeSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public FlowerType createNewFlowerType(FlowerType newFlowerType) throws InputDataValidationException, CreateNewFlowerTypeException {
        Set<ConstraintViolation<FlowerType>>constraintViolations = validator.validate(newFlowerType);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newFlowerType);
                em.flush();

                return newFlowerType;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewFlowerTypeException("Flower type with same type name already exist");
                }
                else
                {
                    throw new CreateNewFlowerTypeException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewFlowerTypeException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<FlowerType> retrieveAllCategories()
    {
        Query query = em.createQuery("SELECT ft FROM FlowerType ft ORDER BY ft.name ASC");
        List<FlowerType> flowerTypeEntities = query.getResultList();
        
        return flowerTypeEntities;
    }
    
    @Override
    public FlowerType retrieveFlowerTypeByFlowerTypeId(Long flowerTypeId) throws FlowerTypeNotFoundException
    {
        FlowerType flowerTypeEntity = em.find(FlowerType.class, flowerTypeId);
        
        if(flowerTypeEntity != null)
        {
            return flowerTypeEntity;
        }
        else
        {
            throw new FlowerTypeNotFoundException("Flower Type ID " + flowerTypeId + " does not exist!");
        }               
    }
    
    @Override
    public void updateCategory(FlowerType flowerType) throws InputDataValidationException, FlowerTypeNotFoundException, UpdateFlowerTypeException
    {
        Set<ConstraintViolation<FlowerType>>constraintViolations = validator.validate(flowerType);
        
        if(constraintViolations.isEmpty())
        {
            if(flowerType.getFlowerTypeId()!= null)
            {
                FlowerType flowerTypeEntityToUpdate = retrieveFlowerTypeByFlowerTypeId(flowerType.getFlowerTypeId());
                
                Query query = em.createQuery("SELECT c FROM FlowerType c WHERE c.name = :inName AND c.flowerTypeId <> :inTypeId");
                query.setParameter("inName", flowerType.getName());
                query.setParameter("inTypeId", flowerType.getFlowerTypeId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateFlowerTypeException("The name of the flower type to be updated is duplicated");
                }
                
                flowerTypeEntityToUpdate.setName(flowerType.getName());
                flowerTypeEntityToUpdate.setDescription(flowerType.getDescription());                               
             
            }
            else
            {
                throw new FlowerTypeNotFoundException("Category ID not provided for category to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public void deleteFlowerType(Long flowerTypeId) throws FlowerTypeNotFoundException, DeleteFlowerTypeException
    {
        FlowerType categoryEntityToRemove = retrieveFlowerTypeByFlowerTypeId(flowerTypeId);
        
        Query query = em.createQuery("SELECT p FROM Flower p WHERE p.flowerType.flowerTypeId = :inFlowerTypeId ORDER BY p.name ASC");
        query.setParameter("inFlowerTypeId", flowerTypeId);
        List<Flower> flowerEntities = query.getResultList();
        
        if(!flowerEntities.isEmpty())
        {
            throw new DeleteFlowerTypeException("Flower Type ID " + flowerTypeId + " is associated with existing flowers and cannot be deleted!");
        }
        else
        {   
            em.remove(categoryEntityToRemove);
        }                
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlowerType>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }
}

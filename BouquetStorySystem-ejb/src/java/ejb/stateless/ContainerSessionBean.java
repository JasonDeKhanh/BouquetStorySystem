/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Container;
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
import util.exception.ContainerNotFoundException;
import util.exception.CreateNewContainerException;
import util.exception.DeleteContainerException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author msipc
 */
@Stateless
public class ContainerSessionBean implements ContainerSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ContainerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    // Create
    public Container createNewContainer(Container newContainerEntity) throws CreateNewContainerException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Container>>constraintViolations = validator.validate(newContainerEntity);
        
        if(constraintViolations.isEmpty())
        {
            try 
            {
                em.persist(newContainerEntity);
                em.flush();
                
                return newContainerEntity;
            } 
            catch (PersistenceException ex) 
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewContainerException();
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
    
    
    // Retrieve All Containers
    public List<Container> retrieveAllContainers()
    {
        Query query = em.createQuery("SELECT c FROM Container c ORDER BY c.containerId ASC");
        List<Container> containers = query.getResultList();
        
        // Lazy loading here!!
        
        return containers;
    }

    
    // Retrieve Container By Id
    public Container retrieveContainerByContainerId(Long containerId) throws ContainerNotFoundException
    {
        Container containerEntity = em.find(Container.class, containerId);
        
        if(containerEntity != null)
        {
            // Lazy Loading here
            return containerEntity;
        }
        else
        {
            throw new ContainerNotFoundException("Container ID " + containerId + " does not exist!");
        }
    }
    
    
    public void updateContainer(Container containerEntity) throws ContainerNotFoundException,InputDataValidationException
    {
        if(containerEntity != null && containerEntity.getContainerId()!= null)
        {
            Set<ConstraintViolation<Container>>constraintViolations = validator.validate(containerEntity);
        
            if(constraintViolations.isEmpty())
            {
                // Do Update
                Container containerEntityToUpdate = retrieveContainerByContainerId(containerEntity.getContainerId());
                
                containerEntityToUpdate.setColor(containerEntity.getColor());
                containerEntityToUpdate.setImgAddress(containerEntity.getImgAddress());
                containerEntityToUpdate.setDescription(containerEntity.getDescription());
                containerEntityToUpdate.setQuantityOnHand(containerEntity.getQuantityOnHand());
                containerEntityToUpdate.setReorderQuantity(containerEntity.getReorderQuantity());
                containerEntityToUpdate.setUnitPrice(containerEntity.getUnitPrice());
                containerEntityToUpdate.setFlowerLimit(containerEntity.getFlowerLimit());
                containerEntityToUpdate.setIsOnDisplay(containerEntity.getIsOnDisplay());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ContainerNotFoundException("Container ID not provided for giftCard to be updated");
        }
    }
            
    
    public void deleteContainer(Long containerId) throws ContainerNotFoundException, DeleteContainerException
    {
        Container containerEntityToDelete = retrieveContainerByContainerId(containerId);
    
        // check can delete or not
        em.remove(containerEntityToDelete);
    }
    
    

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Container>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Container;
import entity.ContainerType;
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
import util.exception.ContainerNotFoundException;
import util.exception.ContainerTypeNotFoundException;
import util.exception.CreateNewContainerException;
import util.exception.DeleteContainerException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateContainerException;

/**
 *
 * @author msipc
 */
@Stateless
public class ContainerSessionBean implements ContainerSessionBeanLocal {

    @EJB
    private ContainerTypeSessionBeanLocal containerTypeSessionBeanLocal;
    
    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ContainerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    // Create
    @Override
    public Container createNewContainer(Container newContainerEntity, Long containerTypeId) throws CreateNewContainerException, UnknownPersistenceException, InputDataValidationException, ContainerTypeNotFoundException
    {
        Set<ConstraintViolation<Container>>constraintViolations = validator.validate(newContainerEntity);
        
        if(constraintViolations.isEmpty())
        {
            try 
            {
                if(containerTypeId == null)
                {
                    throw new CreateNewContainerException("The new container must be associated with a container type!");
                }
                
                ContainerType containerType = containerTypeSessionBeanLocal.retrieveContainerTypeByContainerTypeId(containerTypeId);
                
                em.persist(newContainerEntity);
                newContainerEntity.setContainerType(containerType);
                
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
    @Override
    public List<Container> retrieveAllContainers()
    {
        Query query = em.createQuery("SELECT c FROM Container c ORDER BY c.containerId ASC");
        List<Container> containers = query.getResultList();
        
        // Lazy loading here!!
//        for(Container container : containers)
//        {
//            container.getContainerType();
//        }
        
        return containers;
    }

    
    // Retrieve Container By Id
    @Override
    public Container retrieveContainerByContainerId(Long containerId) throws ContainerNotFoundException
    {
        Container containerEntity = em.find(Container.class, containerId);
        
        if(containerEntity != null)
        {
            // Lazy Loading here
//            containerEntity.getContainerType();
            
            return containerEntity;
        }
        else
        {
            throw new ContainerNotFoundException("Container ID " + containerId + " does not exist!");
        }
    }
    
    @Override
    public void updateContainer(Container containerEntity, Long containerTypeId) throws ContainerNotFoundException,InputDataValidationException, ContainerTypeNotFoundException, UpdateContainerException
    {
        System.out.println("containerEntity toString: " + containerEntity.toString());
        System.out.println("container Entity ID: " + containerEntity.getContainerId());
        System.out.println("containerTypeId: " + containerTypeId);
        System.out.println("");
        System.out.println("containerEntity containerType id: " + containerEntity.getContainerType().getContainerTypeId());
        if(containerEntity != null && containerEntity.getContainerId()!= null)
        {
            Set<ConstraintViolation<Container>>constraintViolations = validator.validate(containerEntity);
        
            if(constraintViolations.isEmpty())
            {
                // Do Update
                Container containerEntityToUpdate = retrieveContainerByContainerId(containerEntity.getContainerId());
                
                if(containerTypeId != null && (!containerEntityToUpdate.getContainerType().getContainerTypeId().equals(containerTypeId)))
                {
                    ContainerType containerTypeToUpdate = containerTypeSessionBeanLocal.retrieveContainerTypeByContainerTypeId(containerTypeId);
                    
                    containerEntityToUpdate.setContainerType(containerTypeToUpdate);
                }
                
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
            
    @Override
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

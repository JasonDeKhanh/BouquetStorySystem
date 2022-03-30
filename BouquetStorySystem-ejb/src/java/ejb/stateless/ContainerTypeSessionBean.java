/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ContainerType;
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
import util.exception.ContainerTypeNotFoundException;
import util.exception.CreateNewContainerTypeException;
import util.exception.DeleteContainerTypeException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author msipc
 */
@Stateless
public class ContainerTypeSessionBean implements ContainerTypeSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ContainerTypeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    // Create New Container Type
    @Override
    public ContainerType createNewContainerType(ContainerType newContainerType) throws CreateNewContainerTypeException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<ContainerType>>constraintViolations = validator.validate(newContainerType);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newContainerType);
                em.flush();
                
                return newContainerType;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewContainerTypeException();
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


    
    // Retrieve All Container Types
    @Override
    public List<ContainerType> retrieveAllContainerTypes()
    {
        Query query = em.createQuery("SELECT ct FROM ContainerType ct ORDER BY ct.containerTypeId ASC");
        List<ContainerType> containerTypes = query.getResultList();
        
        // Lazy loading here!!
        
        return containerTypes;
    }
    
    // Retrieve Container By Id
    @Override
    public ContainerType retrieveContainerTypeByContainerTypeId(Long containerTypeId) throws ContainerTypeNotFoundException
    {
        ContainerType containerTypeEntity = em.find(ContainerType.class, containerTypeId);
        
        if(containerTypeEntity != null)
        {
            // Lazy loading here!!
            
            return containerTypeEntity;
        }
        else
        {
            throw new ContainerTypeNotFoundException("Container Type ID " + containerTypeId + " does not exist!");
        }
    }
    
    
    // Update Container Type
    @Override
    public void updateContainerType(ContainerType containerTypeEntity) throws ContainerTypeNotFoundException,InputDataValidationException
    {
        if(containerTypeEntity != null && containerTypeEntity.getContainerTypeId()!= null)
        {
            Set<ConstraintViolation<ContainerType>>constraintViolations = validator.validate(containerTypeEntity);
        
            if(constraintViolations.isEmpty())
            {
                // Do Update
                ContainerType containerTypeEntityToUpdate = retrieveContainerTypeByContainerTypeId(containerTypeEntity.getContainerTypeId());
                
                containerTypeEntityToUpdate.setName(containerTypeEntity.getName());
                containerTypeEntityToUpdate.setDimensions(containerTypeEntity.getDimensions());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ContainerTypeNotFoundException("Container Type ID not provided for giftCard to be updated");
        }
    }
    
    @Override
    public void deleteContainerType(Long containerTypeId) throws ContainerTypeNotFoundException, DeleteContainerTypeException
    {
        ContainerType containerEntityToDelete = retrieveContainerTypeByContainerTypeId(containerTypeId);
    
        // check can delete or not
        em.remove(containerEntityToDelete);
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ContainerType>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

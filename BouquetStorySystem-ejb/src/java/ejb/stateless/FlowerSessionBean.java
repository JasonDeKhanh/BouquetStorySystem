/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Flower;
import entity.FlowerType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import util.enumeration.Occasion;
import util.exception.CreateNewFlowerException;
import util.exception.DeleteFlowerException;
import util.exception.FlowerInsufficientQuantityOnHandException;
import util.exception.FlowerNotFoundException;
import util.exception.FlowerTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlowerException;

/**
 *
 * @author xqy11
 */
@Stateless
public class FlowerSessionBean implements FlowerSessionBeanLocal {

    @EJB
    private FlowerTypeSessionBeanLocal flowerTypeSessionBeanLocal;

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public FlowerSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Flower createNewFlower(Flower newFlowerEntity,  Long flowerTypeId) throws CreateNewFlowerException, FlowerTypeNotFoundException, UnknownPersistenceException, InputDataValidationException{
        Set<ConstraintViolation<Flower>>constraintViolations = validator.validate(newFlowerEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if(flowerTypeId == null)
                {
                    throw new CreateNewFlowerException("The new product must be associated a leaf category");
                }
                
                FlowerType flowerTypeEntity = flowerTypeSessionBeanLocal.retrieveFlowerTypeByFlowerTypeId(flowerTypeId);
                

                em.persist(newFlowerEntity);
                newFlowerEntity.setFlowerType(flowerTypeEntity);
               
                em.flush();

                return newFlowerEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewFlowerException();
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
            catch(FlowerTypeNotFoundException ex)
            {
                throw new CreateNewFlowerException("An error has occurred while creating the new flower: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        
    }
    
    @Override
    public List<Flower> retrieveAllFlowers()
    {
        Query query = em.createQuery("SELECT f FROM Flower f ORDER BY f.name ASC");
        List<Flower> flowerEntities = query.getResultList();
        
        for(Flower flowerEntity:flowerEntities)
        {
            flowerEntity.getFlowerType();
        }
        
        return flowerEntities;
    }
    
    @Override
    public List<Flower> searchFlowersByName(String searchString)
    {
        Query query = em.createQuery("SELECT p FROM Flower p WHERE p.name LIKE :inSearchString ORDER BY p.name ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<Flower> flowerEntities = query.getResultList();
        
        for(Flower flowerEntity:flowerEntities)
        {
            flowerEntity.getFlowerType();
        }
        
        return flowerEntities;
    }
    
    @Override
    public List<Flower> filterFlowersByCategory(Long fowerTypeId) throws FlowerTypeNotFoundException
    {
        List<Flower> flowerEntities = new ArrayList<>();
        FlowerType flowerTypeEntity = flowerTypeSessionBeanLocal.retrieveFlowerTypeByFlowerTypeId(fowerTypeId);
        
        flowerEntities = flowerTypeEntity.getFlowerEntities();            
       
        
        for(Flower flowerEntity:flowerEntities)
        {
            flowerEntity.getFlowerType();
        }
        
        Collections.sort(flowerEntities, new Comparator<Flower>()
            {
                public int compare(Flower pe1, Flower pe2) {
                    return pe1.getName().compareTo(pe2.getName());
                }
            });

        return flowerEntities;
    }
    
    @Override
    public Flower retrieveFlowerByFlowerId(Long flowerId) throws FlowerNotFoundException
    {
        Flower flowerEntity = em.find(Flower.class, flowerId);
        
        if(flowerEntity != null)
        {
            flowerEntity.getFlowerType();
            
            return flowerEntity;
        }
        else
        {
            throw new FlowerNotFoundException("Flower Type ID " + flowerId + " does not exist!");
        }               
    }
    
    @Override
    public void updateFlower(Flower flowerEntity, Long flowerTypeId) throws FlowerNotFoundException, FlowerTypeNotFoundException, UpdateFlowerException, InputDataValidationException
    {
        if(flowerEntity != null && flowerEntity.getFlowerId()!= null)
        {
            Set<ConstraintViolation<Flower>>constraintViolations = validator.validate(flowerEntity);
        
            if(constraintViolations.isEmpty())
            {
                Flower flowerEntityToUpdate = retrieveFlowerByFlowerId(flowerEntity.getFlowerId());

                if(flowerTypeId != null && (!flowerEntityToUpdate.getFlowerType().getFlowerTypeId().equals(flowerTypeId)))
                {
                    FlowerType flowerTypeToUpdate = flowerTypeSessionBeanLocal.retrieveFlowerTypeByFlowerTypeId(flowerTypeId);

                    flowerEntityToUpdate.setFlowerType(flowerTypeToUpdate);
                }

                flowerEntityToUpdate.setName(flowerEntity.getName());
                flowerEntityToUpdate.setImgAddress(flowerEntity.getImgAddress());
                flowerEntityToUpdate.setFlowerColor(flowerEntity.getFlowerColor());
                flowerEntityToUpdate.setOccasions(flowerEntity.getOccasions());
                flowerEntityToUpdate.setDescription(flowerEntity.getDescription());
                flowerEntityToUpdate.setQuantityOnHand(flowerEntity.getQuantityOnHand());
                flowerEntityToUpdate.setReorderQuantity(flowerEntity.getReorderQuantity());
                flowerEntityToUpdate.setUnitPrice(flowerEntity.getUnitPrice());
                flowerEntityToUpdate.setIsOnDisplay(flowerEntity.getIsOnDisplay());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new FlowerNotFoundException("Flower ID not provided for flower to be updated");
        }
    }
    
    @Override
    public void deleteFlower(Long flowerId) throws FlowerNotFoundException, DeleteFlowerException, FlowerTypeNotFoundException
    {
        Flower flowerEntityToRemove = retrieveFlowerByFlowerId(flowerId);
        
//        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = saleTransactionEntitySessionBeanLocal.retrieveSaleTransactionLineItemsByProductId(productId);
//        
//        if(saleTransactionLineItemEntities.isEmpty())
//        {
//            productEntityToRemove.getCategoryEntity().getProductEntities().remove(productEntityToRemove);
//            
//            for(TagEntity tagEntity:productEntityToRemove.getTagEntities())
//            {
//                tagEntity.getProductEntities().remove(productEntityToRemove);
//            }
//            
//            productEntityToRemove.getTagEntities().clear();
//            
//            entityManager.remove(productEntityToRemove);
//        }
//        else
//        {
//            throw new DeleteFlowerException("Flower ID " + flowerId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
//        }
    }
    
    @Override
    public void debitQuantityOnHand(Long flowerId, Integer quantityToDebit) throws FlowerNotFoundException, FlowerInsufficientQuantityOnHandException, FlowerTypeNotFoundException
    {
        Flower flowerEntity = retrieveFlowerByFlowerId(flowerId);
        
        if(flowerEntity.getQuantityOnHand() >= quantityToDebit)
        {
            flowerEntity.setQuantityOnHand(flowerEntity.getQuantityOnHand() - quantityToDebit);
        }
        else
        {
            throw new FlowerInsufficientQuantityOnHandException("Flower " + flowerEntity.getName() + " quantity on hand is " + flowerEntity.getQuantityOnHand() + " versus quantity to debit of " + quantityToDebit);
        }
    }
    
    @Override
    public void creditQuantityOnHand(Long flowerId, Integer quantityToCredit) throws FlowerNotFoundException, FlowerTypeNotFoundException
    {
        Flower flowerEntity = retrieveFlowerByFlowerId(flowerId);
        flowerEntity.setQuantityOnHand(flowerEntity.getQuantityOnHand() + quantityToCredit);
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Flower>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}

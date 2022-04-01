/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Bouquet;
import entity.Decoration;
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
import util.exception.CreateNewDecorationException;
import util.exception.DecorationInsufficientQuantityOnHandException;
import util.exception.DecorationNotFoundException;
import util.exception.DeleteDecorationException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDecorationException;

/**
 *
 * @author xqy11
 */
@Stateless
public class DecorationSessionBean implements DecorationSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public DecorationSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Decoration createNewDecoration(Decoration newDecorationEntity) throws CreateNewDecorationException, UnknownPersistenceException, InputDataValidationException{
        Set<ConstraintViolation<Decoration>>constraintViolations = validator.validate(newDecorationEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newDecorationEntity);
                em.flush();

                return newDecorationEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewDecorationException();
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
    public List<Decoration> retrieveAllDecorations()
    {
        Query query = em.createQuery("SELECT f FROM Decoration f");
        List<Decoration> decorationEntities = query.getResultList();
        
        return decorationEntities;
    }
    
    @Override
    public Decoration retrieveDecorationByDecorationId(Long decorationId) throws DecorationNotFoundException
    {
        Decoration decorationEntity = em.find(Decoration.class, decorationId);
        
        if(decorationEntity != null)
        {
            return decorationEntity;
        }
        else
        {
            throw new DecorationNotFoundException("Decoraton ID " + decorationId + " does not exist!");
        }               
    }
    
    @Override
    public void updateDecoration(Decoration decorationEntity) throws DecorationNotFoundException, UpdateDecorationException, InputDataValidationException
    {
        if(decorationEntity != null && decorationEntity.getDecorationId()!= null)
        {
            Set<ConstraintViolation<Decoration>>constraintViolations = validator.validate(decorationEntity);
        
            if(constraintViolations.isEmpty())
            {
                Decoration decorationEntityToUpdate = retrieveDecorationByDecorationId(decorationEntity.getDecorationId());


                decorationEntityToUpdate.setName(decorationEntity.getName());
                decorationEntityToUpdate.setImgAddress(decorationEntity.getImgAddress());
                decorationEntityToUpdate.setDescription(decorationEntity.getDescription());
                decorationEntityToUpdate.setQuantityOnHand(decorationEntity.getQuantityOnHand());
                decorationEntityToUpdate.setReorderQuantity(decorationEntity.getReorderQuantity());
                decorationEntityToUpdate.setUnitPrice(decorationEntity.getUnitPrice());
                decorationEntityToUpdate.setIsOnDisplay(decorationEntity.getIsOnDisplay());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new DecorationNotFoundException("Decoration ID not provided for flower to be updated");
        }
    }
    
    @Override
    public void deleteDecoration(Long decorationId) throws DecorationNotFoundException, DeleteDecorationException
    {
        Decoration decorationEntityToRemove = retrieveDecorationByDecorationId(decorationId);
//        
//        Query query = em.createQuery("SELECT b FROM bouquet b JOIN decoration d WHERE b.:inDecorationId");
//        //SELECT e FROM Employee e JOIN e.projects p JOIN e.projects p2 WHERE p.name = :p1 and p2.name = :p2
//        query.setParameter("inDecorationId", decorationId);
//        
//        if(query.getResultList().isEmpty()) {
            em.remove(decorationEntityToRemove);
//        }
//        else
//        {
//            throw new DeleteDecorationException("Decoration ID " + decorationId + " is associated with existing bouquet(s) and cannot be deleted!");
//        }

// List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = saleTransactionEntitySessionBeanLocal.retrieveSaleTransactionLineItemsByProductId(productId);
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
    public void debitQuantityOnHand(Long decorationId, Integer quantityToDebit) throws DecorationNotFoundException, DecorationInsufficientQuantityOnHandException
    {
        Decoration decorationEntity = retrieveDecorationByDecorationId(decorationId);
        
        if(decorationEntity.getQuantityOnHand() >= quantityToDebit)
        {
            decorationEntity.setQuantityOnHand(decorationEntity.getQuantityOnHand() - quantityToDebit);
        }
        else
        {
            throw new DecorationInsufficientQuantityOnHandException("Decoration " + decorationEntity.getName() + " quantity on hand is " + decorationEntity.getQuantityOnHand() + " versus quantity to debit of " + quantityToDebit);
        }
    }
    
    @Override
    public void creditQuantityOnHand(Long flowerId, Integer quantityToCredit) throws DecorationNotFoundException
    {
        Decoration decorationEntity = retrieveDecorationByDecorationId(flowerId);
        decorationEntity.setQuantityOnHand(decorationEntity.getQuantityOnHand() + quantityToCredit);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Decoration>>constraintViolations)
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
}

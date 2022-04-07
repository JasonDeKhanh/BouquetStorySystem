/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Container;
import entity.Decoration;
import entity.Flower;
import entity.PremadeBouquet;
import java.util.ArrayList;
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
import util.exception.CreateNewPremadeBouquetException;
import util.exception.DecorationNotFoundException;
import util.exception.DeletePremadeBouquetException;
import util.exception.FlowerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PremadeBouquetNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePremadeBouquetException;

/**
 *
 * @author msipc
 */
@Stateless
public class PremadeBouquetSessionBean implements PremadeBouquetSessionBeanLocal {

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PremadeBouquetSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    // Create new Premade Bouquet
//    @Override
//    public PremadeBouquet createNewPremadeBouquet(PremadeBouquet premadeBouquet) throws CreateNewPremadeBouquetException, UnknownPersistenceException, InputDataValidationException
//    {
//        Set<ConstraintViolation<PremadeBouquet>>constraintViolations = validator.validate(premadeBouquet);
//        
//        if(constraintViolations.isEmpty())
//        {
//            try
//            {
//                em.persist(premadeBouquet);
//                em.flush();
//                
//                return premadeBouquet;
//            }
//            catch(PersistenceException ex)
//            {
//                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
//                {
//                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
//                    {
//                        throw new CreateNewPremadeBouquetException();
//                    }
//                    else
//                    {
//                        throw new UnknownPersistenceException(ex.getMessage());
//                    }
//                }
//                else
//                {
//                    throw new UnknownPersistenceException(ex.getMessage());
//                }
//            }
//        }
//        else
//        {
//            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
//        }
//    }
    
    // new proper create
    @Override
    public PremadeBouquet createNewPremadeBouquet(PremadeBouquet premadeBouquet, Long containerId, List<Decoration> decorations, List<Flower> flowers) throws CreateNewPremadeBouquetException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<PremadeBouquet>>constraintViolations = validator.validate(premadeBouquet);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if(containerId == null)
                {
                    throw new CreateNewPremadeBouquetException("Container ID not provided or is erroneous");
                }
                Container containerEntity = containerSessionBeanLocal.retrieveContainerByContainerId(containerId);
                
                em.persist(premadeBouquet);
                
                premadeBouquet.setContainer(containerEntity);
                
//                List<Decoration> newDecorations = new ArrayList<>();
                // associate with decorations
                for(Decoration decoration : decorations)
                {
                    Decoration decorationEntity = decorationSessionBeanLocal.retrieveDecorationByDecorationId(decoration.getDecorationId());
                    premadeBouquet.getDecorations().add(decorationEntity);
                }
                
                // associate with flowers
                for(Flower flower : flowers) 
                {
                    Flower flowerEntity = flowerSessionBeanLocal.retrieveFlowerByFlowerId(flower.getFlowerId());
                    premadeBouquet.getFlowers().add(flower);
                }
                //
                em.flush();
                
                return premadeBouquet;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewPremadeBouquetException();
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
            catch(DecorationNotFoundException | FlowerNotFoundException | ContainerNotFoundException ex)
            {
                throw new CreateNewPremadeBouquetException("An error has occurred while creating the new premade bouquet: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    // retrieve all premade bouquets
    @Override
    public List<PremadeBouquet> retrieveAllPremadeBouquets()
    {
        Query query = em.createQuery("SELECT pb FROM PremadeBouquet pb ORDER BY pb.itemId ASC");
        List<PremadeBouquet> giftCardEntities = query.getResultList();
        
        return giftCardEntities;
    }
    
    
    
    // retrieve premade bouquet by item id
    @Override
    public PremadeBouquet retrievePremadeBouquetByItemId(Long itemId) throws PremadeBouquetNotFoundException
    {
        PremadeBouquet premadeBouquet = em.find(PremadeBouquet.class, itemId);
        
        if(premadeBouquet != null)
        {
            return premadeBouquet;
        }
        else
        {
            throw new PremadeBouquetNotFoundException("Premade Bouquet ID " + itemId + " does not exist!");
        }
    }
    
    
    @Override
    public void updatePremadeBouquet(PremadeBouquet premadeBouquet) throws PremadeBouquetNotFoundException, InputDataValidationException, UpdatePremadeBouquetException
    {
        if(premadeBouquet != null && premadeBouquet.getItemId()!= null)
        {
            Set<ConstraintViolation<PremadeBouquet>>constraintViolations = validator.validate(premadeBouquet);
        
            if(constraintViolations.isEmpty())
            {
                // Do Update
                PremadeBouquet premadeBouquetToUpdate = retrievePremadeBouquetByItemId(premadeBouquet.getItemId());

                premadeBouquetToUpdate.setName(premadeBouquet.getName());
                premadeBouquetToUpdate.setImgAddress(premadeBouquet.getImgAddress());
                premadeBouquetToUpdate.setDescription(premadeBouquet.getDescription());
                premadeBouquetToUpdate.setBouquetPrice(premadeBouquet.getBouquetPrice());
                premadeBouquetToUpdate.setIsOnDisplay(premadeBouquet.getIsOnDisplay());
                premadeBouquetToUpdate.setCreatorName(premadeBouquet.getCreatorName());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new PremadeBouquetNotFoundException("Premade Bouquet ID not provided for giftCard to be updated");
        }
    }
    
    
    @Override
    public void deletePremadeBouquet(Long itemId) throws PremadeBouquetNotFoundException, DeletePremadeBouquetException
    {
        PremadeBouquet premadeBouquetToRemove = retrievePremadeBouquetByItemId(itemId);
        
        // check if can delete
        em.remove(premadeBouquetToRemove);
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PremadeBouquet>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

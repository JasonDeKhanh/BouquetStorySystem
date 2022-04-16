/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.AddOn;
import entity.Bundle;
import entity.Container;
import entity.CustomBouquet;
import entity.Customer;
import entity.Decoration;
import entity.Flower;
import entity.GiftCard;
import entity.Item;
import entity.PremadeBouquet;
import entity.Product;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ContainerNotFoundException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DecorationNotFoundException;
import util.exception.FlowerNotFoundException;
import util.exception.InsufficientQuantityException;
import util.exception.ItemNotFoundException;
import util.exception.MarkIsCompletedException;
import util.exception.SaleTransactionAlreadyCompleted;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author matt_
 */
@Stateless
public class SaleTransactionSessionBean implements SaleTransactionSessionBeanLocal {

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "ProductSessionBeanLocal")
    private ProductSessionBeanLocal productSessionBeanLocal;

    @EJB(name = "ItemSessionBeanLocal")
    private ItemSessionBeanLocal itemSessionBeanLocal;

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    @Resource
    private EJBContext eJBContext;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @Override
    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransaction) throws CustomerNotFoundException, CreateNewSaleTransactionException {
        if (newSaleTransaction != null) {
            try {
                System.out.println("==============inside createNewSaleTransaction seesion bean=======");
                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                System.out.println(customer.getEmail());
                
                newSaleTransaction.setCustomer(customer);
                customer.getSaleTransactions().add(newSaleTransaction);
          
//                System.out.println("============one=====================");
                if (newSaleTransaction.getIsPreorder() == false) {

                    for (SaleTransactionLineItem saleTransactionLineItem : newSaleTransaction.getSaleTransactionLineItems()) {
                        System.out.println("======+++>>>>"+saleTransactionLineItem.getItemEntity().getItemId());
                        debitQuantityOnHand(saleTransactionLineItem.getItemEntity(), saleTransactionLineItem.getQuantity());
                        System.out.println("in for loop! weee");
                        System.out.println("saleTransactionLineItem in for loop: " + saleTransactionLineItem.getSerialNumber());
                        System.out.println("saleTransactionLineItem in for loop: " + saleTransactionLineItem.getQuantity());
                        System.out.println("saleTransactionLineItem in for loop: " + saleTransactionLineItem.getUnitPrice());
                       // System.out.println("saleTransactionLineItem in for loop: " + saleTransactionLineItem.getSaleTranscationLineItemId());
                        
//                        em.persist(saleTransactionLineItem);
//                        System.out.println("============two====================="); 
//                        System.out.println("" + );
//                        List<SaleTransactionLineItem> lineItems = new ArrayList<>();
                        if(saleTransactionLineItem.getItemEntity() instanceof CustomBouquet) {
//                            em.persist(saleTransactionLineItem.getItemEntity());
//                            em.persist(saleTransactionLineItem);
                        } else {
//                            em.persist(saleTransactionLineItem);
                        }
                    }
                    newSaleTransaction.setIsCompleted(true);
                }
                
                System.out.println("============================================================jrfvdm cxerkdm,");
                System.out.println(newSaleTransaction.getIsSelfPickup());
                System.out.println(newSaleTransaction.getTotalAmount());
//                System.out.println(newSaleTransaction.getCollectionDateTime());
                
                em.persist(newSaleTransaction);
                
                
                System.out.println("newSaleTransaction.lineItem.items: " + newSaleTransaction.getSaleTransactionLineItems());
              //  System.out.println("newsaleTransaction lineItem[0].item.name:" + newSaleTransaction.getSaleTransactionLineItems().get(0).getItemEntity());
                
                
                em.flush();

                return newSaleTransaction;
            } catch (ContainerNotFoundException | DecorationNotFoundException | FlowerNotFoundException | ItemNotFoundException | InsufficientQuantityException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        } else {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");
        }
    }

    //maybe can use the getName to check item type instead
    @Override
    public void debitQuantityOnHand(Item currItem, Integer quantityToDebit) throws ContainerNotFoundException, DecorationNotFoundException, FlowerNotFoundException, ItemNotFoundException, InsufficientQuantityException {

        if (currItem instanceof Bundle) {
            Bundle item = (Bundle) itemSessionBeanLocal.retrieveItemByItemId(currItem.getItemId());
            for (Map.Entry<Product, Integer> entry : item.getProductQuantities().entrySet()) {
                Product updatedProduct = productSessionBeanLocal.retrieveProductByItemId(entry.getKey().getItemId());

                if (updatedProduct.getQuantityOnHand() >= (quantityToDebit * entry.getValue())) {
                    updatedProduct.setQuantityOnHand(updatedProduct.getQuantityOnHand() - (quantityToDebit * entry.getValue()));
                } else {
                    throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
                }

            }

        } else if (currItem instanceof AddOn) {

            AddOn item = (AddOn) itemSessionBeanLocal.retrieveItemByItemId(currItem.getItemId());
            if (item.getQuantityOnHand() >= quantityToDebit) {
                item.setQuantityOnHand(item.getQuantityOnHand() - quantityToDebit);
            } else {
                throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
            }

        } else if (currItem instanceof GiftCard) {
            GiftCard item = (GiftCard) itemSessionBeanLocal.retrieveItemByItemId(currItem.getItemId());

            if (item.getGiftCardType().getQuantityOnHand() >= quantityToDebit) {
                item.getGiftCardType().setQuantityOnHand(item.getGiftCardType().getQuantityOnHand() - quantityToDebit);
            } else {
                throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
            }

        } else if (currItem instanceof CustomBouquet) {
            CustomBouquet item = (CustomBouquet) itemSessionBeanLocal.retrieveItemByItemId(currItem.getItemId());

            for (Map.Entry<Flower, Integer> entry : item.getFlowerQuantities().entrySet()) {
                Flower updatedFlower = flowerSessionBeanLocal.retrieveFlowerByFlowerId(entry.getKey().getFlowerId());

                if (updatedFlower.getQuantityOnHand() >= (quantityToDebit * entry.getValue())) {
                    updatedFlower.setQuantityOnHand(updatedFlower.getQuantityOnHand() - (quantityToDebit * entry.getValue()));
                } else {
                    throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
                }
            }

            for (Map.Entry<Decoration, Integer> entry : item.getDecorationQuantities().entrySet()) {
                Decoration updatedDecoration = decorationSessionBeanLocal.retrieveDecorationByDecorationId(entry.getKey().getDecorationId());

                if (updatedDecoration.getQuantityOnHand() >= (quantityToDebit * entry.getValue())) {
                    updatedDecoration.setQuantityOnHand(updatedDecoration.getQuantityOnHand() - (quantityToDebit * entry.getValue()));
                } else {
                    throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
                }
            }

            if (item.getContainer().getQuantityOnHand() >= quantityToDebit) {
                item.getContainer().setQuantityOnHand(item.getContainer().getQuantityOnHand() - quantityToDebit);
            } else {
                throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
            }

        } else if (currItem instanceof PremadeBouquet) {
            PremadeBouquet item = (PremadeBouquet) currItem;

            for (Map.Entry<Flower, Integer> entry : item.getFlowerQuantities().entrySet()) {
                Flower updatedFlower = flowerSessionBeanLocal.retrieveFlowerByFlowerId(entry.getKey().getFlowerId());

                if (updatedFlower.getQuantityOnHand() >= (quantityToDebit * entry.getValue())) {
                    updatedFlower.setQuantityOnHand(updatedFlower.getQuantityOnHand() - (quantityToDebit * entry.getValue()));
                } else {
                    throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
                }
            }

            for (Map.Entry<Decoration, Integer> entry : item.getDecorationQuantities().entrySet()) {
                Decoration updatedDecoration = decorationSessionBeanLocal.retrieveDecorationByDecorationId(entry.getKey().getDecorationId());

                if (updatedDecoration.getQuantityOnHand() >= (quantityToDebit * entry.getValue())) {
                    updatedDecoration.setQuantityOnHand(updatedDecoration.getQuantityOnHand() - (quantityToDebit * entry.getValue()));
                } else {
                    throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
                }
            }
            Container updatedContainer = containerSessionBeanLocal.retrieveContainerByContainerId(item.getContainer().getContainerId());
            if (updatedContainer.getQuantityOnHand() >= quantityToDebit) {
                updatedContainer.setQuantityOnHand(item.getContainer().getQuantityOnHand() - quantityToDebit);
            } else {
                throw new InsufficientQuantityException("Insufficient quantity, Please select PreOrder!");
            }

        }

    }

    @Override
    public void markIsCompleted(SaleTransaction saleTransaction) throws SaleTransactionAlreadyCompleted, SaleTransactionNotFoundException, MarkIsCompletedException {
        if (saleTransaction != null && saleTransaction.getSaleTransactionId() != null) {
            SaleTransaction saleTransactionToComplete = retrieveSaleTransactionBySaleTransactionId(saleTransaction.getSaleTransactionId());
            try {
                if (saleTransaction.getIsCompleted() == false) {
                    
                    for (SaleTransactionLineItem saleTransactionLineItem : saleTransactionToComplete.getSaleTransactionLineItems()) {
                        debitQuantityOnHand(saleTransactionLineItem.getItemEntity(), saleTransactionLineItem.getQuantity());
                        //em.persist(saleTransactionLineItemEntity);
                    }

                    saleTransactionToComplete.setIsCompleted(true);
                } else {
                    throw new SaleTransactionAlreadyCompleted("Sale Transaction already completed");
                }
            } catch (ContainerNotFoundException | DecorationNotFoundException | FlowerNotFoundException | ItemNotFoundException ex) {
                eJBContext.setRollbackOnly();
                throw new MarkIsCompletedException(ex.getMessage());
            }
            catch (InsufficientQuantityException ex) {
                eJBContext.setRollbackOnly();
                throw new MarkIsCompletedException("Insufficient quantity");
            }
        } else {
            throw new SaleTransactionNotFoundException("Sale Transaction ID not provided");
        }
    }

    @Override
    public List<SaleTransaction> retrieveAllSaleTransactions() {
        Query query = em.createQuery("SELECT st FROM SaleTransaction st");

        return query.getResultList();
    }
    
    @Override
    public List<SaleTransaction> retrieveAllSaleTransactionsByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT st FROM SaleTransaction st WHERE st.customer.customerId = :inCustomerId");
        

        query.setParameter("inCustomerId", customerId);

        return query.getResultList();
    }

    @Override
    public SaleTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException {
        SaleTransaction saleTransaction = em.find(SaleTransaction.class, saleTransactionId);

        if (saleTransaction != null) {
            saleTransaction.getSaleTransactionLineItems().size();

            return saleTransaction;
        } else {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }
    }

    @Override
    public void updateSaleTransaction(SaleTransaction saleTransactionEntity) {
        em.merge(saleTransactionEntity);
    }

    @Override
    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException {
        SaleTransaction saleTransaction = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);

        if (!saleTransaction.getVoidRefund()) {
            for (SaleTransactionLineItem saleTransactionLineItemEntity : saleTransaction.getSaleTransactionLineItems()) {/*
                try
                {
                    productEntitySessionBeanLocal.creditQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
                }
                catch(ProductNotFoundException ex)
                {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }
                 */
            }

            saleTransaction.setVoidRefund(true);
        } else {
            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
        }
    }
    @Override
    public void updateDeliveryDate(Long saleTransactionId, LocalDateTime newDate) {
        SaleTransaction saleTransaction = em.find(SaleTransaction.class, saleTransactionId);
        Instant instant = newDate.atZone(ZoneId.systemDefault()).toInstant();
	Date date = Date.from(instant);
        saleTransaction.setCollectionDateTime(date);
        
    }
}

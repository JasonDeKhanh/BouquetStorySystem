/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import entity.Item;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.InsufficientQuantityException;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author matt_
 */
@Stateless
public class SaleTransactionSessionBean implements SaleTransactionSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;
    @Resource
    private EJBContext eJBContext;
    
    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    
    
    @Override
    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransaction) throws CustomerNotFoundException, CreateNewSaleTransactionException
    {
        if(newSaleTransaction != null)
        {
            try
            {
                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                newSaleTransaction.setCustomer(customer);
                customer.getSaleTransactions().add(newSaleTransaction);

                em.persist(newSaleTransaction);
                
                if(newSaleTransaction.getIsPreorder() == false) {
                    
                    for(SaleTransactionLineItem saleTransactionLineItem:newSaleTransaction.getSaleTransactionLineItems()) {
                    debitQuantityOnHand(saleTransactionLineItem.getItemEntity().getItemId(), saleTransactionLineItem.getQuantity());
                    //em.persist(saleTransactionLineItemEntity);
                }
                    newSaleTransaction.setIsCompleted(true);
                }

               

                em.flush();

                return newSaleTransaction;
            }
            catch(InsufficientQuantityException ex)
            {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();
                
                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");
        }
    }
    //method not completed yet
    public void debitQuantityOnHand(Long itemId, int quantity) throws InsufficientQuantityException {
        if(true){
        }
        else{
        // do a bunch of if checks to see the instance of
        throw new InsufficientQuantityException("Item insufficient qty");
        }
    }


   @Override
    public List<SaleTransaction> retrieveAllSaleTransactions()
    {
        Query query = em.createQuery("SELECT st FROM SaleTransaction st");
        
        return query.getResultList();
    }
    
     @Override
    public SaleTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException
    {
        SaleTransaction saleTransaction = em.find(SaleTransaction.class, saleTransactionId);
        
        if(saleTransaction != null)
        {
            saleTransaction.getSaleTransactionLineItems().size();
            
            return saleTransaction;
        }
        else
        {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }                
    }
    
    @Override
    public void updateSaleTransaction(SaleTransaction saleTransactionEntity)
    {
        em.merge(saleTransactionEntity);
    }
    
     @Override
    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException
    {
        SaleTransaction saleTransaction = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
        
        if(!saleTransaction.getVoidRefund())
        {
            for(SaleTransactionLineItem saleTransactionLineItemEntity:saleTransaction.getSaleTransactionLineItems())
            {/*
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
        }
        else
        {
            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
        }
    }
}

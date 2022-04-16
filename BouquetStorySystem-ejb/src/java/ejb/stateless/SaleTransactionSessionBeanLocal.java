/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Item;
import entity.SaleTransaction;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface SaleTransactionSessionBeanLocal {

    public List<SaleTransaction> retrieveAllSaleTransactions();

    public SaleTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException;

    public void updateSaleTransaction(SaleTransaction saleTransactionEntity);

    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException;

    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransaction) throws CustomerNotFoundException, CreateNewSaleTransactionException;

    public void markIsCompleted(SaleTransaction saleTransaction) throws SaleTransactionAlreadyCompleted, SaleTransactionNotFoundException, MarkIsCompletedException;

    public void debitQuantityOnHand(Item currItem, Integer quantityToDebit) throws ContainerNotFoundException, DecorationNotFoundException, FlowerNotFoundException, ItemNotFoundException, InsufficientQuantityException;

    public List<SaleTransaction> retrieveAllSaleTransactionsByCustomerId(Long customerId);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.SaleTransaction;
import java.util.List;
import javax.ejb.Local;
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
    
}

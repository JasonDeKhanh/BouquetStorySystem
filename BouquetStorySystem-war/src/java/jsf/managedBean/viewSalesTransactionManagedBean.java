/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.SaleTransactionSessionBeanLocal;
import entity.SaleTransaction;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author matt_
 */
@Named(value = "viewSalesTransactionManagedBean")
@ViewScoped
public class viewSalesTransactionManagedBean implements Serializable {


    @EJB(name = "SaleTransactionSessionBeanLocal")
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;
    

    private List<SaleTransaction> saleTransactionEntities;
    
    public viewSalesTransactionManagedBean() {
    }
    
    public List<SaleTransaction> getSaleTransactionEntities() {
        // should we link sale transaction to admin??
        //CustomerEntity currentCustomerEntity = (CustomerEntity)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        //CustomerEntity updatedCustomerEntity = customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerEntity.getCustomerId());
        saleTransactionEntities = saleTransactionSessionBeanLocal.retrieveAllSaleTransactions();
        return saleTransactionEntities;
    }
    
    /**
     * @param saleTransactionEntities the saleTransactionEntities to set
     */
    public void setSaleTransactionEntities(List<SaleTransaction> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }
}

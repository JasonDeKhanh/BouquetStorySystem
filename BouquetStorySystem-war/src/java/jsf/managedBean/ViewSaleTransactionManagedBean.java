/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.SaleTransaction;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author matt_
 */
@Named(value = "viewSaleTransactionManagedBean")
@ViewScoped
public class ViewSaleTransactionManagedBean implements Serializable {
    
    private SaleTransaction saleTransactionToView;

    /**
     * Creates a new instance of ViewSaleTransactionManagedBean
     */
    public ViewSaleTransactionManagedBean() {
        saleTransactionToView = new SaleTransaction();
    }

    /**
     * @return the saleTransactionToView
     */
    public SaleTransaction getSaleTransactionToView() {
        return saleTransactionToView;
    }

    /**
     * @param saleTransactionToView the saleTransactionToView to set
     */
    public void setSaleTransactionToView(SaleTransaction saleTransactionToView) {
        this.saleTransactionToView = saleTransactionToView;
    }
    
}

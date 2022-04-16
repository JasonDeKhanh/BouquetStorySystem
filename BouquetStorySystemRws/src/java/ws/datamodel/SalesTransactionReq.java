/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.SaleTransaction;
import entity.SaleTransactionLineItem;

/**
 *
 * @author xqy11
 */
public class SalesTransactionReq {
    private String username;
    private SaleTransaction saleTransaction;
    private SaleTransactionLineItem[] saleTransactionLineItems;

    public SalesTransactionReq(String username, SaleTransaction saleTransaction, SaleTransactionLineItem[] saleTransactionLineItems) {
        this.username = username;
        this.saleTransaction = saleTransaction;
        this.saleTransactionLineItems = saleTransactionLineItems;
    }

    public SalesTransactionReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SaleTransaction getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(SaleTransaction saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    public SaleTransactionLineItem[] getSaleTransactionLineItems() {
        return saleTransactionLineItems;
    }

    public void setSaleTransactionLineItems(SaleTransactionLineItem[] saleTransactionLineItems) {
        this.saleTransactionLineItems = saleTransactionLineItems;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.SaleTransactionSessionBeanLocal;
import entity.AddOn;
import entity.Bundle;
import entity.CustomBouquet;
import entity.GiftCard;
import entity.GiftCardType;
import entity.Item;
import entity.PremadeBouquet;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author matt_
 */
@Named(value = "viewSaleTransactionManagedBean")
@ViewScoped
public class ViewSaleTransactionManagedBean implements Serializable {

    @EJB(name = "SaleTransactionSessionBeanLocal")
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;
    

    private SaleTransaction saleTransactionToView;
    private Item currItem;
    private AddOn currAddOn;
    private Bundle currBundle;
    private GiftCard currGiftCard;
    private GiftCardType currGiftCardType;
    private String itemType;
    private Long saleTransactionId;

    /**
     * Creates a new instance of ViewSaleTransactionManagedBean
     */
    public ViewSaleTransactionManagedBean() {
        saleTransactionToView = new SaleTransaction();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            System.out.println("Inside try block post construct");
            saleTransactionId = Long.valueOf(
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("saleTransactionId"));
            saleTransactionToView = saleTransactionSessionBeanLocal.retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
        } catch (SaleTransactionNotFoundException ex) {
            System.out.println("inside catch error block postconstruct");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Sale Transaction ID not provided", null));
        }
    }

    /**
     * @return the saleTransactionToView
     */
    public SaleTransaction getSaleTransactionToView() {
        return saleTransactionToView;
    }

    // method to check itemtype and name
    public String checkItemType() {
        if (currItem instanceof Bundle) {
            Bundle item = (Bundle) currItem;
            currItem = item;
            return "Bundle:" + item.getName();
        } else if (currItem instanceof AddOn) {
            AddOn item = (AddOn) currItem;
            currItem = item;
            return "AddOn:" + item.getName();
        } else if (currItem instanceof GiftCard) {
            GiftCard item = (GiftCard) currItem;
            currItem = item;
            return "GiftCard:" + item.getGiftCardType().getName();
        } else if (currItem instanceof CustomBouquet) {
            CustomBouquet item = (CustomBouquet) currItem;
            currItem = item;
            return "Custom Bouquet:" + item.getName();
        } else if (currItem instanceof PremadeBouquet) {
            PremadeBouquet item = (PremadeBouquet) currItem;
            currItem = item;
            return "Premade Bouquet:" + item.getName();
        } else {
            return "this method has error";
        }

    }

    public GiftCardType getGiftCardType() {
        GiftCard item = (GiftCard)currItem;
        return item.getGiftCardType();

    }

    public Boolean isBundle() {
        if (currItem instanceof Bundle) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isAddOn() {
        if (currItem instanceof AddOn) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isGiftCard() {
        if (currItem instanceof GiftCard) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isCustomBouquet() {
        if (currItem instanceof CustomBouquet) {
            return true;
        } else {
            return false;
        }
    }
    
    public void convertItemToAddOn(ActionEvent event) {
        Item tempItem = (Item) event.getComponent().getAttributes().get("itemEntity");
        currAddOn = (AddOn) tempItem;
    }
    
    public void convertItemToBundle(ActionEvent event) {
        Item tempItem = (Item) event.getComponent().getAttributes().get("itemEntity");
        currBundle = (Bundle) tempItem;
    }
    
    public void convertItemToGiftCard(ActionEvent event) {
        Item tempItem = (Item) event.getComponent().getAttributes().get("itemEntity");
        currGiftCard = (GiftCard) tempItem;
        currGiftCardType = currGiftCard.getGiftCardType();
        
        
    }

    public void back(ActionEvent event) throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().redirect("viewSaleTransactions.xhtml");

    }

    /**
     * @param saleTransactionToView the saleTransactionToView to set
     */
    public void setSaleTransactionToView(SaleTransaction saleTransactionToView) {
        this.saleTransactionToView = saleTransactionToView;
    }

    /**
     * @return the saleTransactionId
     */
    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    /**
     * @param saleTransactionId the saleTransactionId to set
     */
    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
    }

    /**
     * @return the currItem
     */
    public Item getCurrItem() {
        return currItem;
    }

    /**
     * @param currItem the currItem to set
     */
    public void setCurrItem(Item currItem) {
        this.currItem = currItem;
    }

    /**
     * @return the itemType
     */
    public String getItemType() {

        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the currAddOn
     */
    public AddOn getCurrAddOn() {
        return currAddOn;
    }

    /**
     * @param currAddOn the currAddOn to set
     */
    public void setCurrAddOn(AddOn currAddOn) {
        this.currAddOn = currAddOn;
    }

    /**
     * @return the currBundle
     */
    public Bundle getCurrBundle() {
        return currBundle;
    }

    /**
     * @param currBundle the currBundle to set
     */
    public void setCurrBundle(Bundle currBundle) {
        this.currBundle = currBundle;
    }

    /**
     * @return the currGiftCard
     */
    public GiftCard getCurrGiftCard() {
        return currGiftCard;
    }

    /**
     * @param currGiftCard the currGiftCard to set
     */
    public void setCurrGiftCard(GiftCard currGiftCard) {
        this.currGiftCard = currGiftCard;
    }

    /**
     * @return the currGiftCardType
     */
    public GiftCardType getCurrGiftCardType() {
        return currGiftCardType;
    }

    /**
     * @param currGiftCardType the currGiftCardType to set
     */
    public void setCurrGiftCardType(GiftCardType currGiftCardType) {
        this.currGiftCardType = currGiftCardType;
    }

}

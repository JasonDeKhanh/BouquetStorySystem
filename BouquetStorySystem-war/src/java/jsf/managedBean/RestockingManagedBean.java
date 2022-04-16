/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.AddOnSessionBeanLocal;
import ejb.stateless.ContainerSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.FlowerSessionBeanLocal;
import ejb.stateless.GiftCardTypeSessionBeanLocal;
import ejb.stateless.SaleTransactionSessionBeanLocal;
import entity.AddOn;
import entity.Bouquet;
import entity.Container;
import entity.Decoration;
import entity.Flower;
import entity.GiftCard;
import entity.GiftCardType;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;


/**
 *
 * @author matt_
 */
@Named(value = "restockingManagedBean")
@ViewScoped
public class RestockingManagedBean implements Serializable {

    @EJB(name = "SaleTransactionSessionBeanLocal")
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    @EJB(name = "GiftCardTypeSessionBeanLocal")
    private GiftCardTypeSessionBeanLocal giftCardTypeSessionBeanLocal;

    @EJB(name = "AddOnSessionBeanLocal")
    private AddOnSessionBeanLocal addOnSessionBeanLocal;

    private List<AddOn> addOnList;

    private List<GiftCardType> giftCardTypeList;

    private List<Decoration> decorationList;
    private List<Container> containerList;
    private List<Flower> flowerList;
    private List<SaleTransaction> allSaleTransactionList;

    private List<SaleTransaction> comingSaleTransactionList;
    private Map<Flower, Integer> flowersMap;

    /**
     * Creates a new instance of RestockingManagedBean
     */
    public RestockingManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        addOnList = addOnSessionBeanLocal.retrieveAllAddOns();
        giftCardTypeList = giftCardTypeSessionBeanLocal.retrieveAllGiftCardTypes();
        decorationList = decorationSessionBeanLocal.retrieveAllDecorations();
        containerList = containerSessionBeanLocal.retrieveAllContainers();
        flowerList = flowerSessionBeanLocal.retrieveAllFlowers();
        allSaleTransactionList = saleTransactionSessionBeanLocal.retrieveAllSaleTransactions();
        comingSaleTransactionList = new ArrayList<>();
        flowersMap = new HashMap<>();

    }

    /**
     * @return the addOnList
     */
    public List<AddOn> getAddOnList() {
        for (AddOn addOn : addOnList) {
            if (addOn.getQuantityOnHand() > addOn.getReorderQuantity()) {
                addOnList.remove(addOn);
            }
        }
        return addOnList;
    }

    /**
     * @param addOnList the addOnList to set
     */
    public void setAddOnList(List<AddOn> addOnList) {
        this.addOnList = addOnList;
    }

    /**
     * @return the giftCardTypeList
     */
    public List<GiftCardType> getGiftCardTypeList() {
        for (GiftCardType giftCardType : giftCardTypeList) {
            if (giftCardType.getQuantityOnHand() > giftCardType.getReorderQuantity()) {
                giftCardTypeList.remove(giftCardType);
            }
        }
        return giftCardTypeList;
    }

    /**
     * @return the decorationList
     */
    public List<Decoration> getDecorationList() {
        for (Decoration deco : decorationList) {
            if (deco.getQuantityOnHand() > deco.getReorderQuantity()) {
                decorationList.remove(deco);
            }
        }
        return decorationList;
    }

    /**
     * @param decorationList the decorationList to set
     */
    public void setDecorationList(List<Decoration> decorationList) {
        this.decorationList = decorationList;
    }

    /**
     * @return the containerList
     */
    public List<Container> getContainerList() {
        for (Container container : containerList) {
            if (container.getQuantityOnHand() > container.getReorderQuantity()) {
                containerList.remove(container);
            }
        }
        return containerList;
    }

    /**
     * @param containerList the containerList to set
     */
    public void setContainerList(List<Container> containerList) {
        this.containerList = containerList;
    }

    /**
     * @return the flowerList
     */
    public List<Flower> getFlowerList() {
        for (Flower flower : flowerList) {
            if (flower.getQuantityOnHand() > flower.getReorderQuantity()) {
                flowerList.remove(flower);
            }
        }
        return flowerList;
    }

    /**
     * @param flowerList the flowerList to set
     */
    public void setFlowerList(List<Flower> flowerList) {
        this.flowerList = flowerList;
    }

    /**
     * @return the comingSaleTransactionList
     */
    public List<SaleTransaction> getComingSaleTransactionList() {
        
            return comingSaleTransactionList;
        
    }
        /**
         * @param comingSaleTransactionList the comingSaleTransactionList to set
         */
    public void setComingSaleTransactionList(List<SaleTransaction> comingSaleTransactionList) {
        this.comingSaleTransactionList = comingSaleTransactionList;
    }

    /**
     * @return the flowersMap
     */
    public Map<Flower, Integer> getFlowersMap() {
        
        for (SaleTransaction saleTransaction : allSaleTransactionList) {
            Date dateNow = new Date();
            LocalDate newDate = LocalDate.now().plusDays(7);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date datePlus7 = Date.from(newDate.atStartOfDay(defaultZoneId).toInstant());
            
            if((saleTransaction.getCollectionDateTime().after(dateNow) && saleTransaction.getCollectionDateTime().before(datePlus7)) && saleTransaction.getIsPreorder()) {
                comingSaleTransactionList.add(saleTransaction);
            }          
        }
        
        for (SaleTransaction saleTransaction : comingSaleTransactionList) {
            for(SaleTransactionLineItem lineItem: saleTransaction.getSaleTransactionLineItems()) {
                if( lineItem.getItemEntity() instanceof Bouquet) {
                    Bouquet bouquet = (Bouquet)lineItem.getItemEntity();
                    
                    for (Map.Entry<Flower, Integer> entry : bouquet.getFlowerQuantities().entrySet()) {
                        if (flowersMap.containsKey(entry.getKey())) {
                            Integer qty = flowersMap.get(entry.getKey()) + entry.getValue();
                            flowersMap.put(entry.getKey(), qty);
                        } else {
                            flowersMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

        }
            
        return flowersMap;
    }

    /**
     * @param flowersMap the flowersMap to set
     */
    public void setFlowersMap(Map<Flower, Integer> flowersMap) {
        this.flowersMap = flowersMap;
    }

    /**
     * @return the allSaleTransactionList
     */
    public List<SaleTransaction> getAllSaleTransactionList() {
        return allSaleTransactionList;
    }

    /**
     * @param allSaleTransactionList the allSaleTransactionList to set
     */
    public void setAllSaleTransactionList(List<SaleTransaction> allSaleTransactionList) {
        this.allSaleTransactionList = allSaleTransactionList;
    }

}

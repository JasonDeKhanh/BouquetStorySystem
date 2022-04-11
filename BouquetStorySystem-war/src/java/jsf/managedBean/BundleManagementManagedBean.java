package jsf.managedBean;

import ejb.stateless.AddOnSessionBeanLocal;
import ejb.stateless.BundleSessionBeanLocal;
import ejb.stateless.GiftCardSessionBeanLocal;
import ejb.stateless.PremadeBouquetSessionBeanLocal;
import ejb.stateless.PromotionSessionBeanLocal;
import entity.AddOn;
import entity.Bundle;
import entity.PremadeBouquet;
import entity.Product;
import entity.Promotion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.BundleNotFoundException;
import util.exception.CreateNewBundleException;
import util.exception.DeleteBundleException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateBundleException;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "bundleManagementManagedBean")
@ViewScoped
public class BundleManagementManagedBean implements Serializable {

    @EJB
    private PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal;
    @EJB
    private GiftCardSessionBeanLocal giftCardSessionBeanLocal;
    @EJB
    private AddOnSessionBeanLocal addOnSessionBeanLocal;
    @EJB
    private PromotionSessionBeanLocal promotionSessionBeanLocal;
    @EJB
    private BundleSessionBeanLocal bundleSessionBeanLocal;
    
    private List<PremadeBouquet> premadeBouquetEntities;
    private List<AddOn> addOnEntities;
    private List<Promotion> promotionEntities;
    private Map<Product, Integer> products;
    private List<Bundle> bundleEntities;
    private List<Bundle> filteredBundleEntities;
    
    private Bundle newBundleEntity;
    private Bundle selectedBundleEntityToUpdate;
    private Product productToAdd;
    private Integer quantityToSet;
    
    public BundleManagementManagedBean() {
        newBundleEntity = new Bundle();
        selectedBundleEntityToUpdate = new Bundle();
        products = new HashMap<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        setAddOnEntities(addOnSessionBeanLocal.retrieveAllAddOns());
        setPromotionEntities(promotionSessionBeanLocal.retrieveAllPromotions());
        setPremadeBouquetEntities(premadeBouquetSessionBeanLocal.retrieveAllPremadeBouquets());
        setBundleEntities(bundleSessionBeanLocal.retrieveAllBundles());
    }
    
    public void deleteBundle(ActionEvent event) {
        try
        {
            Bundle bundleEntityToDelete = (Bundle)event.getComponent().getAttributes().get("bundleEntityToDelete");
            bundleSessionBeanLocal.deleteBundle(bundleEntityToDelete.getItemId());
            
            getBundleEntities().remove(bundleEntityToDelete);
            
            if (getFilteredBundleEntities() != null)
            {
                getFilteredBundleEntities().remove(bundleEntityToDelete);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bundle deleted successfully", null));
        }
        catch(BundleNotFoundException | DeleteBundleException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Bundle: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateBundle(ActionEvent event) {
        selectedBundleEntityToUpdate = (Bundle)event.getComponent().getAttributes().get("bundleEntityToUpdate");
        products = selectedBundleEntityToUpdate.getProductQuantities();
        
    }
    
    public void updateBundle(ActionEvent event) {
        selectedBundleEntityToUpdate.setProductQuantities(products);
        try
        {
            bundleSessionBeanLocal.updateBundle(selectedBundleEntityToUpdate);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bundle updated successfully", null));
        }
        catch(BundleNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Bundle: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdateBundleException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void addProductToMap(ActionEvent event) {
        products.put(productToAdd, quantityToSet);
    }
    
    public void removeProductFromMap(ActionEvent event)
    {
        Product productToRemove = (Product)event.getComponent().getAttributes().get("productToRemove");
        products.remove(productToRemove);
    }

    public List<PremadeBouquet> getPremadeBouquetEntities() {
        return premadeBouquetEntities;
    }

    public void setPremadeBouquetEntities(List<PremadeBouquet> premadeBouquetEntities) {
        this.premadeBouquetEntities = premadeBouquetEntities;
    }

    public List<AddOn> getAddOnEntities() {
        return addOnEntities;
    }

    public void setAddOnEntities(List<AddOn> addOnEntities) {
        this.addOnEntities = addOnEntities;
    }

    public List<Promotion> getPromotionEntities() {
        return promotionEntities;
    }

    public void setPromotionEntities(List<Promotion> promotionEntities) {
        this.promotionEntities = promotionEntities;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public List<Bundle> getBundleEntities() {
        return bundleEntities;
    }

    public void setBundleEntities(List<Bundle> bundleEntities) {
        this.bundleEntities = bundleEntities;
    }
    
    public List<Bundle> getFilteredBundleEntities() {
        return filteredBundleEntities;
    }

    public void setFilteredBundleEntities(List<Bundle> filteredBundleEntities) {
        this.filteredBundleEntities = filteredBundleEntities;
    }

    public Bundle getNewBundleEntity() {
        return newBundleEntity;
    }

    public void setNewBundleEntity(Bundle newBundleEntity) {
        this.newBundleEntity = newBundleEntity;
    }

    public Bundle getSelectedBundleEntityToUpdate() {
        return selectedBundleEntityToUpdate;
    }

    public void setSelectedBundleEntityToUpdate(Bundle selectedBundleEntityToUpdate) {
        this.selectedBundleEntityToUpdate = selectedBundleEntityToUpdate;
    }

    public Product getProductToAdd() {
        return productToAdd;
    }

    public void setProductToAdd(Product productToAdd) {
        this.productToAdd = productToAdd;
    }

    public Integer getQuantityToSet() {
        return quantityToSet;
    }

    public void setQuantityToSet(Integer quantityToSet) {
        this.quantityToSet = quantityToSet;
    }
    
}

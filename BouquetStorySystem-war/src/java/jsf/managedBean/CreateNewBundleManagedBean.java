/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import util.exception.AddOnNotFoundException;
import util.exception.CreateNewBundleException;
import util.exception.InputDataValidationException;
import util.exception.PremadeBouquetNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "createNewBundleManagedBean")
@ViewScoped
public class CreateNewBundleManagedBean implements Serializable {

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
    private Map<Product, Integer> productQuantities;
    private List<Product> products;
    
    private Bundle newBundleEntity;
    private Long promotionIdToSet;
    private Long premadeBouquetIdToAdd;
    private Long addOnIdToAdd;
    private Integer quantityToSet;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    
    public CreateNewBundleManagedBean() {
        newBundleEntity = new Bundle();
        productQuantities = new HashMap<>();
        products = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        setAddOnEntities(addOnSessionBeanLocal.retrieveAllAddOns());
        setPromotionEntities(promotionSessionBeanLocal.retrieveAllPromotions());
        setPremadeBouquetEntities(premadeBouquetSessionBeanLocal.retrieveAllPremadeBouquets());
    }
    
    public void createNewBundle(ActionEvent event) {
        try
        {
            newBundleEntity.setProductQuantities(getProductQuantities());
            newBundleEntity.setProducts(products);
            if (promotionIdToSet != null) {
                for(Promotion promotion:promotionEntities) {
                    if (promotion.getPromotionId().equals(premadeBouquetIdToAdd)) {
                        newBundleEntity.setPromotion(promotion);
                        break;
                    }
                }
            }
            Bundle bundle = bundleSessionBeanLocal.createNewBundle(getNewBundleEntity());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New bundle created successfully (GiftCardType ID: " + bundle.getItemId() + ")", null));
        }
        catch (InputDataValidationException | CreateNewBundleException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Bundle: " + ex.getMessage(), null));
        }
    }
    
    public PremadeBouquet getPremadeBouquetById() {
        for(PremadeBouquet premadeBouquet:premadeBouquetEntities) {
            if (premadeBouquet.getItemId().equals(premadeBouquetIdToAdd)) {
                return premadeBouquet;
            }
        }
        return null;
    }
    
    public AddOn getAddOnById() {
        for(AddOn addOn:addOnEntities) {
            if (addOn.getItemId().equals(addOnIdToAdd)) {
                return addOn;
            }
        }
        return null;
    }
    
    public void addPremadeBouquetToMap(ActionEvent event) {
        try {
            Product productToAdd = premadeBouquetSessionBeanLocal.retrievePremadeBouquetByItemId(premadeBouquetIdToAdd);
            productQuantities.put(productToAdd, quantityToSet);
            if (!products.contains(productToAdd)) {
                products.add(productToAdd);
            }
        } catch(PremadeBouquetNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the Premade Bouquet: " + ex.getMessage(), null));
        }
    }
    
    public void addAddOnToMap(ActionEvent event) {
        try {
            Product productToAdd = addOnSessionBeanLocal.retrieveAddOnByAddOnId(addOnIdToAdd);
            productQuantities.put(productToAdd, quantityToSet);
            if (!products.contains(productToAdd)) {
                products.add(productToAdd);
            }
        } catch(AddOnNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the Add On: " + ex.getMessage(), null));
        }
    }
    
    public void removeProductFromMap(ActionEvent event)
    {
        Product productToRemove = (Product)event.getComponent().getAttributes().get("productToRemove");
        productQuantities.remove(productToRemove);
        products.remove(productToRemove);
    }
    
    public void handleImageUploadNew(FileUploadEvent event) {
        // from Prof's Lecture 6 Demo 3
        try {
//            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_2") + System.getProperty("file.separator") + event.getFile().getFileName();
            String test = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_2");
            String test2 = System.getProperty("file.separator");
            String test3 = event.getFile().getFileName();
            System.out.println("test: " + test);
            System.out.println("test2: " + test2);
            System.out.println("test3: " + test3);
            String newFilePath = test + test2 + test3;

            System.err.println("********** Bouquet Story System: File name: " + event.getFile().getFileName());
            System.err.println("********** Bouquet Story System: newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            setShowUploadedFile((Boolean) true);

            // Would it be correct to put setImgAddress(uploadedFilePath) here??
            newBundleEntity.setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
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

    public Map<Product, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<Product, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Bundle getNewBundleEntity() {
        return newBundleEntity;
    }

    public void setNewBundleEntity(Bundle newBundleEntity) {
        this.newBundleEntity = newBundleEntity;
    }

    public Long getPromotionIdToSet() {
        return promotionIdToSet;
    }

    public void setPromotionIdToSet(Long promotionIdToSet) {
        this.promotionIdToSet = promotionIdToSet;
    }

    public Long getPremadeBouquetIdToAdd() {
        return premadeBouquetIdToAdd;
    }

    public void setPremadeBouquetIdToAdd(Long premadeBouquetIdToAdd) {
        this.premadeBouquetIdToAdd = premadeBouquetIdToAdd;
    }

    public Long getAddOnIdToAdd() {
        return addOnIdToAdd;
    }

    public void setAddOnIdToAdd(Long addOnIdToAdd) {
        this.addOnIdToAdd = addOnIdToAdd;
    }

    public Integer getQuantityToSet() {
        return quantityToSet;
    }

    public void setQuantityToSet(Integer quantityToSet) {
        this.quantityToSet = quantityToSet;
    }

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public Boolean getShowUploadedFile() {
        return showUploadedFile;
    }

    public void setShowUploadedFile(Boolean showUploadedFile) {
        this.showUploadedFile = showUploadedFile;
    }
    
}

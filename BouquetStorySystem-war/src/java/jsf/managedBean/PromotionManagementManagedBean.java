/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.PromotionSessionBeanLocal;
import entity.Promotion;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import util.exception.CreateNewPromotionException;
import util.exception.DeletePromotionException;
import util.exception.InputDataValidationException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePromotionException;

/**
 *
 * @author matt_
 */
@Named(value = "promotionManagementManagedBean")
@ViewScoped
public class PromotionManagementManagedBean implements Serializable {

    @EJB(name = "PromotionSessionBeanLocal")
    private PromotionSessionBeanLocal promotionSessionBeanLocal;
    
    private List<Promotion> promotionEntities;
    private List<Promotion> filteredPromotionEntities;
    
    private Promotion newPromotionEntity;
    
    private Promotion selectedPromotionEntityToUpdate;
    
    //private String uploadedFilePath;
    //private Boolean showUploadedFile;
    
    

    
    public PromotionManagementManagedBean() {
        newPromotionEntity = new Promotion();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setPromotionEntities(promotionSessionBeanLocal.retrieveAllPromotions());
    }
    
    
    public void createNewPromotion(ActionEvent event)
    {  
        try
        {
            Promotion pe = promotionSessionBeanLocal.createNewPromotion(newPromotionEntity);
            promotionEntities.add(pe);
            
            if(filteredPromotionEntities != null) {
                filteredPromotionEntities.add(pe);
            }
            
            newPromotionEntity = new Promotion();
            //setUploadedFilePath(null);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Promotion created successfully (Promotion ID: " + pe.getPromotionId()+ ")", null));
        }
        catch(InputDataValidationException | CreateNewPromotionException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new promotion: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdatePromotion(ActionEvent event)
    {
        selectedPromotionEntityToUpdate = (Promotion)event.getComponent().getAttributes().get("promotionEntityToUpdate");

    }
    public void updatePromotion(ActionEvent event)
    {        
        try
        {
            promotionSessionBeanLocal.updatePromotion(selectedPromotionEntityToUpdate);
                        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion updated successfully", null));
        }
        catch(PromotionNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating promotion: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdatePromotionException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deletePromotion(ActionEvent event)
    {
        try
        {
            Promotion promotionEntityToDelete = (Promotion)event.getComponent().getAttributes().get("promotionEntityToDelete");
            promotionSessionBeanLocal.deletePromotionDecoration(promotionEntityToDelete.getPromotionId());
            
            getPromotionEntities().remove(promotionEntityToDelete);
            
            if(getFilteredPromotionEntities() != null)
            {
                getFilteredPromotionEntities().remove(promotionEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion deleted successfully", null));
        }
        catch(PromotionNotFoundException | DeletePromotionException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting promotion: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
   /* public void handleImageUploadNew(FileUploadEvent event)
    {
        // from Prof's Lecture 6 Demo 3
        try
        {
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

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
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
            getNewPromotionEntity().setImgAddress(uploadedFilePath);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleImageUploadUpdate(FileUploadEvent event)
    {
        // from Prof's Lecture 6 Demo 3
        try
        {
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

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
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
            getSelectedDecorationEntityToUpdate().setImgAddress(uploadedFilePath);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
*/
    /**
     * @return the promotionEntities
     */
    public List<Promotion> getPromotionEntities() {
        return promotionEntities;
    }

    /**
     * @param promotionEntities the promotionEntities to set
     */
    public void setPromotionEntities(List<Promotion> promotionEntities) {
        this.promotionEntities = promotionEntities;
    }

    /**
     * @return the filteredPromotionEntities
     */
    public List<Promotion> getFilteredPromotionEntities() {
        return filteredPromotionEntities;
    }

    /**
     * @param filteredPromotionEntities the filteredPromotionEntities to set
     */
    public void setFilteredPromotionEntities(List<Promotion> filteredPromotionEntities) {
        this.filteredPromotionEntities = filteredPromotionEntities;
    }

    /**
     * @return the newPromotionEntity
     */
    public Promotion getNewPromotionEntity() {
        return newPromotionEntity;
    }

    /**
     * @param newPromotionEntity the newPromotionEntity to set
     */
    public void setNewPromotionEntity(Promotion newPromotionEntity) {
        this.newPromotionEntity = newPromotionEntity;
    }

    /**
     * @return the selectedPromotionEntityToUpdate
     */
    public Promotion getSelectedPromotionEntityToUpdate() {
        return selectedPromotionEntityToUpdate;
    }

    /**
     * @param selectedPromotionEntityToUpdate the selectedPromotionEntityToUpdate to set
     */
    public void setSelectedPromotionEntityToUpdate(Promotion selectedPromotionEntityToUpdate) {
        this.selectedPromotionEntityToUpdate = selectedPromotionEntityToUpdate;
    }

    /**
     * @return the uploadedFilePath
     */
    //public String getUploadedFilePath() {
    //    return uploadedFilePath;
   // }

    
   // public void setUploadedFilePath(String uploadedFilePath) {
    //    this.uploadedFilePath = uploadedFilePath;
   // }

    
   /* public Boolean getShowUploadedFile() {
        return showUploadedFile;
    }

    
    public void setShowUploadedFile(Boolean showUploadedFile) {
        this.showUploadedFile = showUploadedFile;
    }
    */
}

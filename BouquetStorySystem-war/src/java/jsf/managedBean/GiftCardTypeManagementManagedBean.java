package jsf.managedBean;

import ejb.stateless.GiftCardTypeSessionBeanLocal;
import entity.GiftCardType;
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
import util.exception.CreateNewGiftCardTypeException;
import util.exception.DeleteGiftCardTypeException;
import util.exception.GiftCardTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateGiftCardTypeException;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "giftCardTypeManagementManagedBean")
@ViewScoped
public class GiftCardTypeManagementManagedBean implements Serializable {

    @EJB
    private GiftCardTypeSessionBeanLocal giftCardTypeSessionBeanLocal;
    
    private List<GiftCardType> giftCardTypeEntities;
    private List<GiftCardType> filteredGiftCardTypeEntities;
    
    private GiftCardType newGiftCardTypeEntity;
    
    private GiftCardType selectedGiftCardTypeEntityToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    
    public GiftCardTypeManagementManagedBean() {
        newGiftCardTypeEntity = new GiftCardType();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setGiftCardTypeEntities(giftCardTypeSessionBeanLocal.retrieveAllGiftCardTypes());
    }
    
    public void viewGiftCardTypeDetails(ActionEvent event) throws IOException
    {
        Long giftCardTypeIdToView = (Long)event.getComponent().getAttributes().get("giftCardTypeId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("giftCardTypeIdToView", giftCardTypeIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewGiftCardTypeDetails.xhtml");
    }
    
    public void createNewGiftCardType(ActionEvent event)
    {  
        try
        {
            GiftCardType gct = giftCardTypeSessionBeanLocal.createNewGiftCardType(getNewGiftCardTypeEntity());
            getGiftCardTypeEntities().add(gct);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New gift card type created successfully (GiftCardType ID: " + gct.getGiftCardTypeId()+ ")", null));
        }
        catch(InputDataValidationException | CreateNewGiftCardTypeException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new giftCardType: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateGiftCardType(ActionEvent event)
    {
        selectedGiftCardTypeEntityToUpdate = (GiftCardType)event.getComponent().getAttributes().get("giftCardTypeEntityToUpdate");
    }
    
    public void updateGiftCardType(ActionEvent event)
    {        
        try
        {
            
            giftCardTypeSessionBeanLocal.updateGiftCardType(selectedGiftCardTypeEntityToUpdate);
                        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Gift card type updated successfully", null));
        }
        catch(GiftCardTypeNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating gift card type: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdateGiftCardTypeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteGiftCardType(ActionEvent event)
    {
        try
        {
            GiftCardType giftCardTypeEntityToDelete = (GiftCardType)event.getComponent().getAttributes().get("giftCardTypeEntityToDelete");
            giftCardTypeSessionBeanLocal.deleteGiftCardType(giftCardTypeEntityToDelete.getGiftCardTypeId());
            
            getGiftCardTypeEntities().remove(giftCardTypeEntityToDelete);
            
            if(getFilteredGiftCardTypeEntities() != null)
            {
                getFilteredGiftCardTypeEntities().remove(giftCardTypeEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Gift card type deleted successfully", null));
        }
        catch(GiftCardTypeNotFoundException | DeleteGiftCardTypeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting gift card type: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void handleImageUploadNew(FileUploadEvent event)
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
            getNewGiftCardTypeEntity().setImgAddress(uploadedFilePath);
            
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
            getSelectedGiftCardTypeEntityToUpdate().setImgAddress(uploadedFilePath);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }

    public List<GiftCardType> getGiftCardTypeEntities() {
        return giftCardTypeEntities;
    }

    public void setGiftCardTypeEntities(List<GiftCardType> giftCardTypeEntities) {
        this.giftCardTypeEntities = giftCardTypeEntities;
    }

    public List<GiftCardType> getFilteredGiftCardTypeEntities() {
        return filteredGiftCardTypeEntities;
    }

    public void setFilteredGiftCardTypeEntities(List<GiftCardType> filteredGiftCardTypeEntities) {
        this.filteredGiftCardTypeEntities = filteredGiftCardTypeEntities;
    }

    public GiftCardType getNewGiftCardTypeEntity() {
        return newGiftCardTypeEntity;
    }

    public void setNewGiftCardTypeEntity(GiftCardType newGiftCardTypeEntity) {
        this.newGiftCardTypeEntity = newGiftCardTypeEntity;
    }

    public GiftCardType getSelectedGiftCardTypeEntityToUpdate() {
        return selectedGiftCardTypeEntityToUpdate;
    }

    public void setSelectedGiftCardTypeEntityToUpdate(GiftCardType selectedGiftCardTypeEntityToUpdate) {
        this.selectedGiftCardTypeEntityToUpdate = selectedGiftCardTypeEntityToUpdate;
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

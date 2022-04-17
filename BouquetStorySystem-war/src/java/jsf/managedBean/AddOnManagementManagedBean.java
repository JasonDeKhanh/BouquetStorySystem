package jsf.managedBean;

import ejb.stateless.AddOnSessionBeanLocal;
import entity.AddOn;
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
import util.exception.AddOnNotFoundException;
import util.exception.CreateNewAddOnException;
import util.exception.DeleteAddOnException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddOnException;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "addOnManagementManagedBean")
@ViewScoped
public class AddOnManagementManagedBean implements Serializable {

    @EJB
    private AddOnSessionBeanLocal addOnSessionBeanLocal;
    
    private List<AddOn> addOnEntities;
    private List<AddOn> filteredAddOnEntities;
    
    private AddOn newAddOnEntity;
    
    private AddOn selectedAddOnEntityToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    
    public AddOnManagementManagedBean() {
        newAddOnEntity = new AddOn();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setAddOnEntities(addOnSessionBeanLocal.retrieveAllAddOns());
    }
    
    public void viewAddOnDetails(ActionEvent event) throws IOException
    {
        Long itemIdToView = (Long)event.getComponent().getAttributes().get("itemId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("itemIdToView", itemIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAddOnDetails.xhtml");
    }
    
    public void createNewAddOn(ActionEvent event)
    {  
        try
        {
            AddOn gct = addOnSessionBeanLocal.createNewAddOn(getNewAddOnEntity());
            getAddOnEntities().add(gct);

            newAddOnEntity = new AddOn();
            uploadedFilePath = null;
            showUploadedFile = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New add on created successfully (AddOn ID: " + gct.getItemId()+ ")", null));
        }
        catch(InputDataValidationException | CreateNewAddOnException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new addOn: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateAddOn(ActionEvent event)
    {
        selectedAddOnEntityToUpdate = (AddOn)event.getComponent().getAttributes().get("addOnEntityToUpdate");
    }
    
    public void updateAddOn(ActionEvent event)
    {        
        try
        {
            
            addOnSessionBeanLocal.updateAddOn(selectedAddOnEntityToUpdate);
                        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Add on type updated successfully", null));
        }
        catch(AddOnNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating add on: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdateAddOnException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteAddOn(ActionEvent event)
    {
        try
        {
            AddOn addOnEntityToDelete = (AddOn)event.getComponent().getAttributes().get("addOnEntityToDelete");
            addOnSessionBeanLocal.deleteAddOn(addOnEntityToDelete.getItemId());
            
            getAddOnEntities().remove(addOnEntityToDelete);
            
            if(getFilteredAddOnEntities() != null)
            {
                getFilteredAddOnEntities().remove(addOnEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Add on type deleted successfully", null));
        }
        catch(AddOnNotFoundException | DeleteAddOnException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting add on: " + ex.getMessage(), null));
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
            getNewAddOnEntity().setImgAddress(uploadedFilePath);
            
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
            getSelectedAddOnEntityToUpdate().setImgAddress(uploadedFilePath);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }

    public List<AddOn> getAddOnEntities() {
        return addOnEntities;
    }

    public void setAddOnEntities(List<AddOn> addOnEntities) {
        this.addOnEntities = addOnEntities;
    }

    public List<AddOn> getFilteredAddOnEntities() {
        return filteredAddOnEntities;
    }

    public void setFilteredAddOnEntities(List<AddOn> filteredAddOnEntities) {
        this.filteredAddOnEntities = filteredAddOnEntities;
    }

    public AddOn getNewAddOnEntity() {
        return newAddOnEntity;
    }

    public void setNewAddOnEntity(AddOn newAddOnEntity) {
        this.newAddOnEntity = newAddOnEntity;
    }

    public AddOn getSelectedAddOnEntityToUpdate() {
        return selectedAddOnEntityToUpdate;
    }

    public void setSelectedAddOnEntityToUpdate(AddOn selectedAddOnEntityToUpdate) {
        this.selectedAddOnEntityToUpdate = selectedAddOnEntityToUpdate;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.PremadeBouquetSessionBeanLocal;
import entity.PremadeBouquet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.OccasionEnum;
import util.exception.DeletePremadeBouquetException;
import util.exception.InputDataValidationException;
import util.exception.PremadeBouquetNotFoundException;
import util.exception.UpdatePremadeBouquetException;

/**
 *
 * @author msipc
 */
@Named(value = "premadeBouquetManagementManagedBean")
@ViewScoped
public class PremadeBouquetManagementManagedBean implements Serializable {

    @EJB(name = "PremadeBouquetSessionBeanLocal")
    private PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal;

    private PremadeBouquet newPremadeBouquetEntity;
    private List<PremadeBouquet> premadeBouquetEntities;
    private List<PremadeBouquet> filtPremadeBouquetEntities;
    private List<OccasionEnum> occasionEnums;

    private PremadeBouquet selectedPremadeBouquetEntityToUpdate;
    private List<OccasionEnum> occasionEnumsToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    
    public PremadeBouquetManagementManagedBean() {
        newPremadeBouquetEntity = new PremadeBouquet();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setPremadeBouquetEntities(premadeBouquetSessionBeanLocal.retrieveAllPremadeBouquets());
        setOccasionEnums(Arrays.asList(OccasionEnum.values()));
    }

//    public void createNewPremadeBouquet(ActionEvent event)
//    {
//        
//    }
    
    public void doUpdatePremadeBouquet(ActionEvent event) 
    {
        selectedPremadeBouquetEntityToUpdate = (PremadeBouquet) event.getComponent().getAttributes().get("premadeBouquetEntityToUpdate");
        occasionEnumsToUpdate = new ArrayList<>();
        
        for(OccasionEnum occasionEnum : selectedPremadeBouquetEntityToUpdate.getOccasions())
        {
            occasionEnumsToUpdate.add(occasionEnum);
        }
    }
    
    public void updatePremadeBouquet(ActionEvent event)
    {
        try 
        {
            selectedPremadeBouquetEntityToUpdate.setOccasions(occasionEnumsToUpdate);
            
            premadeBouquetSessionBeanLocal.updatePremadeBouquet(selectedPremadeBouquetEntityToUpdate);
            setPremadeBouquetEntities(premadeBouquetSessionBeanLocal.retrieveAllPremadeBouquets());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Premade bouquet updated successfully", null));
        } 
        catch (PremadeBouquetNotFoundException | UpdatePremadeBouquetException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating premade bouquet: " + ex.getMessage(), null));
        }
        catch (InputDataValidationException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deletePremadeBouquet(ActionEvent event)
    {
        try 
        {
            PremadeBouquet premadeBouquetEntityToDelete = (PremadeBouquet)event.getComponent().getAttributes().get("premadeBouquetEntityToDelete");
            premadeBouquetSessionBeanLocal.deletePremadeBouquet(premadeBouquetEntityToDelete.getItemId());
        
            getPremadeBouquetEntities().remove(premadeBouquetEntityToDelete);
            
            if(getFiltPremadeBouquetEntities() != null)
            {
                getFiltPremadeBouquetEntities().remove(premadeBouquetEntityToDelete);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Premade bouquet deleted successfully", null));
        } 
        catch (PremadeBouquetNotFoundException | DeletePremadeBouquetException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting premade bouquet: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
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
            getNewPremadeBouquetEntity().setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleImageUploadUpdate(FileUploadEvent event) {
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
            getSelectedPremadeBouquetEntityToUpdate().setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public PremadeBouquet getNewPremadeBouquetEntity() {
        return newPremadeBouquetEntity;
    }

    public void setNewPremadeBouquetEntity(PremadeBouquet newPremadeBouquetEntity) {
        this.newPremadeBouquetEntity = newPremadeBouquetEntity;
    }

    public List<PremadeBouquet> getPremadeBouquetEntities() {
        return premadeBouquetEntities;
    }

    public void setPremadeBouquetEntities(List<PremadeBouquet> premadeBouquetEntities) {
        this.premadeBouquetEntities = premadeBouquetEntities;
    }

    public List<PremadeBouquet> getFiltPremadeBouquetEntities() {
        return filtPremadeBouquetEntities;
    }

    public void setFiltPremadeBouquetEntities(List<PremadeBouquet> filtPremadeBouquetEntities) {
        this.filtPremadeBouquetEntities = filtPremadeBouquetEntities;
    }

    public List<OccasionEnum> getOccasionEnums() {
        return occasionEnums;
    }

    public void setOccasionEnums(List<OccasionEnum> occasionEnums) {
        this.occasionEnums = occasionEnums;
    }

    public PremadeBouquet getSelectedPremadeBouquetEntityToUpdate() {
        return selectedPremadeBouquetEntityToUpdate;
    }

    public void setSelectedPremadeBouquetEntityToUpdate(PremadeBouquet selectedPremadeBouquetEntityToUpdate) {
        this.selectedPremadeBouquetEntityToUpdate = selectedPremadeBouquetEntityToUpdate;
    }

    public List<OccasionEnum> getOccasionEnumsToUpdate() {
        return occasionEnumsToUpdate;
    }

    public void setOccasionEnumsToUpdate(List<OccasionEnum> occasionEnumsToUpdate) {
        this.occasionEnumsToUpdate = occasionEnumsToUpdate;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.DecorationSessionBeanLocal;
import entity.Decoration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import util.exception.CreateNewDecorationException;
import util.exception.DecorationNotFoundException;
import util.exception.DeleteDecorationException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDecorationException;

/**
 *
 * @author xqy11
 */
@Named(value = "decorationManagementManagedBean")
@ViewScoped
public class DecorationManagementManagedBean implements Serializable {

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;
    
    private List<Decoration> decorationEntities;
    private List<Decoration> filteredDecorationEntities;
    
    private Decoration newDecorationEntity;
    
    private Decoration selectedDecorationEntityToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    
    public DecorationManagementManagedBean() {
        newDecorationEntity = new Decoration();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setDecorationEntities(decorationSessionBeanLocal.retrieveAllDecorations());
    }
    
    public void viewDecorationDetails(ActionEvent event) throws IOException
    {
        Long decorationIdToView = (Long)event.getComponent().getAttributes().get("decorationId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("decorationIdToView", decorationIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewDecorationDetails.xhtml");
    }
    
    public void createNewDecoration(ActionEvent event)
    {  
        try
        {
            Decoration pe = decorationSessionBeanLocal.createNewDecoration(getNewDecorationEntity());
            getDecorationEntities().add(pe);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New decoration created successfully (Decoration ID: " + pe.getDecorationId()+ ")", null));
        }
        catch(InputDataValidationException | CreateNewDecorationException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new decoration: " + ex.getMessage(), null));
        }
    }
    
    //doUpdateDecoration
    public void doUpdateDecoration(ActionEvent event)
    {
        selectedDecorationEntityToUpdate = (Decoration)event.getComponent().getAttributes().get("decorationEntityToUpdate");

    }
    public void updateDecoration(ActionEvent event)
    {        
        try
        {
            
            decorationSessionBeanLocal.updateDecoration(selectedDecorationEntityToUpdate);
                        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Decoration updated successfully", null));
        }
        catch(DecorationNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating decoration: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdateDecorationException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteDecoration(ActionEvent event)
    {
        try
        {
            Decoration decorationEntityToDelete = (Decoration)event.getComponent().getAttributes().get("decorationEntityToDelete");
            decorationSessionBeanLocal.deleteDecoration(decorationEntityToDelete.getDecorationId());
            
            getDecorationEntities().remove(decorationEntityToDelete);
            
            if(getFilteredDecorationEntities() != null)
            {
                getFilteredDecorationEntities().remove(decorationEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Decoration deleted successfully", null));
        }
        catch(DecorationNotFoundException | DeleteDecorationException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting decoration: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    
    public void handleImageUpload(FileUploadEvent event)
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
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    /**
     * @return the decorationEntities
     */
    public List<Decoration> getDecorationEntities() {
        return decorationEntities;
    }

    /**
     * @param decorationEntities the decorationEntities to set
     */
    public void setDecorationEntities(List<Decoration> decorationEntities) {
        this.decorationEntities = decorationEntities;
    }

    /**
     * @return the filteredDecorationEntities
     */
    public List<Decoration> getFilteredDecorationEntities() {
        return filteredDecorationEntities;
    }

    /**
     * @param filteredDecorationEntities the filteredDecorationEntities to set
     */
    public void setFilteredDecorationEntities(List<Decoration> filteredDecorationEntities) {
        this.filteredDecorationEntities = filteredDecorationEntities;
    }

    /**
     * @return the newDecorationEntity
     */
    public Decoration getNewDecorationEntity() {
        return newDecorationEntity;
    }

    /**
     * @param newDecorationEntity the newDecorationEntity to set
     */
    public void setNewDecorationEntity(Decoration newDecorationEntity) {
        this.newDecorationEntity = newDecorationEntity;
    }

    /**
     * @return the selectedDecorationEntityToUpdate
     */
    public Decoration getSelectedDecorationEntityToUpdate() {
        return selectedDecorationEntityToUpdate;
    }

    /**
     * @param selectedDecorationEntityToUpdate the selectedDecorationEntityToUpdate to set
     */
    public void setSelectedDecorationEntityToUpdate(Decoration selectedDecorationEntityToUpdate) {
        this.selectedDecorationEntityToUpdate = selectedDecorationEntityToUpdate;
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

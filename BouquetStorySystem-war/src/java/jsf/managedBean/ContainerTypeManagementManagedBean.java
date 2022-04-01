/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.ContainerTypeSessionBeanLocal;
import entity.ContainerType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import util.exception.ContainerTypeNotFoundException;
import util.exception.CreateNewContainerTypeException;
import util.exception.DeleteContainerTypeException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateContainerTypeException;
import util.exception.UpdateDecorationException;

/**
 *
 * @author msipc
 */
@Named(value = "containerTypeManagementManagedBean")
@ViewScoped
public class ContainerTypeManagementManagedBean implements Serializable {

    @EJB(name = "ContainerTypeSessionBeanLocal")
    private ContainerTypeSessionBeanLocal containerTypeSessionBeanLocal;

    private List<ContainerType> containerTypes;
    private List<ContainerType> filteredContainerTypes;
    
    private ContainerType newContainerTypeEntity;
    
    private ContainerType selectedContainerTypeEntityToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    

    public ContainerTypeManagementManagedBean() {
        newContainerTypeEntity = new ContainerType();
    }

    
    public void postConstruct()
    {
        setContainerTypes(containerTypeSessionBeanLocal.retrieveAllContainerTypes());
    }
    
    
//    public void viewContainerTypeDetails(ActionEvent event) throws IOException
//    {
//        Long containerTypeToView = (Long)event.getComponent().getAttributes().get("containerTypeId");
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("containerTypeId", containerTypeToView);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("ViewContainerType.xhtml");
//    }
    
    public void createNewContainerType(ActionEvent event)
    {
        try
        {
            ContainerType ct = containerTypeSessionBeanLocal.createNewContainerType(newContainerTypeEntity);
            getContainerTypes().add(ct);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Container Type created successfully (Container Type ID: " + ct.getContainerTypeId()+ ")", null));
        }
        catch(InputDataValidationException | UnknownPersistenceException | CreateNewContainerTypeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new container type: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateContainerType(ActionEvent event)
    {
        selectedContainerTypeEntityToUpdate = (ContainerType)event.getComponent().getAttributes().get("containerTypeToUpdate");
    }
    
    public void updateContainerType(ActionEvent event)
    {
        try 
        {
            containerTypeSessionBeanLocal.updateContainerType(newContainerTypeEntity);
        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Container Type updated successfully", null));
        } 
        catch (ContainerTypeNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating container type: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdateContainerTypeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    public void deleteContainerType(ActionEvent event)
    {
        try 
        {
            ContainerType containerTypeToDelete = (ContainerType)event.getComponent().getAttributes().get("containerTypeToDelete");
            containerTypeSessionBeanLocal.deleteContainerType(containerTypeToDelete.getContainerTypeId());
            
            getContainerTypes().remove(containerTypeToDelete);
            
            if(getFilteredContainerTypes() != null)
            {
                getFilteredContainerTypes().remove(containerTypeToDelete);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Container Type deleted successfully", null));
        } 
        catch (ContainerTypeNotFoundException | DeleteContainerTypeException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting container type: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    public List<ContainerType> getContainerTypes() {
        return containerTypes;
    }

    public void setContainerTypes(List<ContainerType> containerTypes) {
        this.containerTypes = containerTypes;
    }

    public List<ContainerType> getFilteredContainerTypes() {
        return filteredContainerTypes;
    }

    public void setFilteredContainerTypes(List<ContainerType> filteredContainerTypes) {
        this.filteredContainerTypes = filteredContainerTypes;
    }

    public ContainerType getNewContainerTypeEntity() {
        return newContainerTypeEntity;
    }

    public void setNewContainerTypeEntity(ContainerType newContainerTypeEntity) {
        this.newContainerTypeEntity = newContainerTypeEntity;
    }

    public ContainerType getSelectedContainerTypeEntityToUpdate() {
        return selectedContainerTypeEntityToUpdate;
    }

    public void setSelectedContainerTypeEntityToUpdate(ContainerType selectedContainerTypeEntityToUpdate) {
        this.selectedContainerTypeEntityToUpdate = selectedContainerTypeEntityToUpdate;
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

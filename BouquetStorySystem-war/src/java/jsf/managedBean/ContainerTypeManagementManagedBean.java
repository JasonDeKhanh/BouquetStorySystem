/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.ContainerTypeSessionBeanLocal;
import entity.ContainerType;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;

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

    
    public void doUpdateContainerType(ActionEvent event)
    {
        selectedContainerTypeEntityToUpdate = (ContainerType)event.getComponent().getAttributes().get("containerTypeToUpdate");
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

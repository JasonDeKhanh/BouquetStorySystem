/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.ContainerSessionBeanLocal;
import ejb.stateless.ContainerTypeSessionBeanLocal;
import entity.Container;
import entity.ContainerType;
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
import util.exception.ContainerNotFoundException;
import util.exception.ContainerTypeNotFoundException;
import util.exception.CreateNewContainerException;
import util.exception.DeleteContainerException;
import util.exception.FlowerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateContainerException;

/**
 *
 * @author msipc
 */
@Named(value = "containerManagementManagedBean")
@ViewScoped
public class ContainerManagementManagedBean implements Serializable {

    @EJB(name = "ContainerTypeSessionBeanLocal")
    private ContainerTypeSessionBeanLocal containerTypeSessionBeanLocal;

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    
    
    private Container newContainerEntity;
    private List<Container> containerEntities;
    private List<Container> filteredContainerEntities;
    
    private Long newContainerEntityTypeId;
    private List<ContainerType> containerTypeEntities;
    
    private Container selectedContainerEntityToUpdate;
    private Long containerTypeIdToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;

    public ContainerManagementManagedBean() {
        newContainerEntity = new Container();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setContainerEntities(containerSessionBeanLocal.retrieveAllContainers());
        setContainerTypeEntities(containerTypeSessionBeanLocal.retrieveAllContainerTypes());
    }
    
    public void createNewContainer(ActionEvent event) 
    {
        try 
        {
            Container ce = containerSessionBeanLocal.createNewContainer(newContainerEntity, newContainerEntityTypeId);
            containerEntities.add(ce);
            
            if(filteredContainerEntities != null)
            {
                filteredContainerEntities.add(ce);
            }
            
            newContainerEntity = new Container();
            newContainerEntityTypeId = null;
            uploadedFilePath = null;
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New container created successfully (Container ID: " + ce.getContainerId()+ ")", null));
        } 
        catch (InputDataValidationException | CreateNewContainerException | ContainerTypeNotFoundException | UnknownPersistenceException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new container: " + ex.getMessage(), null));
        }
    }

    public void doUpdateContainer(ActionEvent event)
    {
        selectedContainerEntityToUpdate = (Container)event.getComponent().getAttributes().get("containerEntityToUpdate");
        containerTypeIdToUpdate = selectedContainerEntityToUpdate.getContainerType().getContainerTypeId();
    }
    
    public void updateContainer(ActionEvent event)
    {
        try 
        {
            System.out.println("IN HERE UPDATE CONTAINERR");
            containerSessionBeanLocal.updateContainer(selectedContainerEntityToUpdate, containerTypeIdToUpdate);
            setContainerEntities(containerSessionBeanLocal.retrieveAllContainers());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Container updated successfully", null));
        } 
        catch (ContainerNotFoundException | ContainerTypeNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating container: " + ex.getMessage(), null));
        }
        catch (InputDataValidationException | UpdateContainerException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteContainer(ActionEvent event)
    {
        try 
        {
            Container containerEntityToDelete = (Container)event.getComponent().getAttributes().get("containerEntityToDelete");
            containerSessionBeanLocal.deleteContainer(containerEntityToDelete.getContainerId());
            
            getContainerEntities().remove(containerEntityToDelete);
            
            if(getFilteredContainerEntities() != null)
            {
                getFilteredContainerEntities().remove(containerEntityToDelete);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Container deleted successfully", null));
        } 
        catch (ContainerNotFoundException | DeleteContainerException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting container: " + ex.getMessage(), null));
        }
        catch (Exception ex)
        {
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
            getNewContainerEntity().setImgAddress(uploadedFilePath);

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
            getSelectedContainerEntityToUpdate().setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public Container getNewContainerEntity() {
        return newContainerEntity;
    }

    public void setNewContainerEntity(Container newContainerEntity) {
        this.newContainerEntity = newContainerEntity;
    }

    public List<Container> getContainerEntities() {
        return containerEntities;
    }

    public void setContainerEntities(List<Container> containerEntities) {
        this.containerEntities = containerEntities;
    }

    public List<Container> getFilteredContainerEntities() {
        return filteredContainerEntities;
    }

    public void setFilteredContainerEntities(List<Container> filteredContainerEntities) {
        this.filteredContainerEntities = filteredContainerEntities;
    }

    public Long getNewContainerEntityTypeId() {
        return newContainerEntityTypeId;
    }

    public void setNewContainerEntityTypeId(Long newContainerEntityTypeId) {
        this.newContainerEntityTypeId = newContainerEntityTypeId;
    }

    public List<ContainerType> getContainerTypeEntities() {
        return containerTypeEntities;
    }

    public void setContainerTypeEntities(List<ContainerType> containerTypeEntities) {
        this.containerTypeEntities = containerTypeEntities;
    }

    public Container getSelectedContainerEntityToUpdate() {
        return selectedContainerEntityToUpdate;
    }

    public void setSelectedContainerEntityToUpdate(Container selectedContainerEntityToUpdate) {
        this.selectedContainerEntityToUpdate = selectedContainerEntityToUpdate;
    }

    public Long getContainerTypeIdToUpdate() {
        return containerTypeIdToUpdate;
    }

    public void setContainerTypeIdToUpdate(Long containerTypeIdToUpdate) {
        this.containerTypeIdToUpdate = containerTypeIdToUpdate;
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

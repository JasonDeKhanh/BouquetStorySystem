/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.FlowerSessionBeanLocal;
import ejb.stateless.FlowerTypeSessionBeanLocal;
import entity.Flower;
import entity.FlowerType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.FlowerColorEnum;
import util.exception.CreateNewFlowerException;
import util.exception.DeleteFlowerException;
import util.exception.FlowerNotFoundException;
import util.exception.FlowerTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlowerException;

/**
 *
 * @author xqy11
 */
@Named(value = "flowerManagementManagedBean")
@ViewScoped
public class FlowerManagementManagedBean implements Serializable {

    /**
     * @return the flowerColorUpdate
     */
    public FlowerColorEnum getFlowerColorUpdate() {
        return flowerColorUpdate;
    }

    /**
     * @param flowerColorUpdate the flowerColorUpdate to set
     */
    public void setFlowerColorUpdate(FlowerColorEnum flowerColorUpdate) {
        this.flowerColorUpdate = flowerColorUpdate;
    }

    @EJB(name = "FlowerTypeSessionBeanLocal")
    private FlowerTypeSessionBeanLocal flowerTypeSessionBeanLocal;

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    private Flower newFlowerEntity;
    private List<Flower> flowerEntities;
    private List<Flower> filteredFlowerEntities;
    private Long newFlowerEntityType;
    private List<FlowerType> flowerTypeEntities;
    private List<FlowerColorEnum> flowerColorEnums;

    private Flower selectedFlowerEntityToUpdate;
    private Long flowerTypeIdUpdate;
    private FlowerColorEnum flowerColorUpdate;

    private String uploadedFilePath;
    private Boolean showUploadedFile;

    public FlowerManagementManagedBean() {
        newFlowerEntity = new Flower();
    }

    @PostConstruct
    public void postConstruct() {
        setFlowerEntities(flowerSessionBeanLocal.retrieveAllFlowers());
        setFlowerTypeEntities(flowerTypeSessionBeanLocal.retrieveAllCategories());
        setFlowerColorEnums(Arrays.asList(FlowerColorEnum.values()));
    }

    public void createNewFlower(ActionEvent event) {
        try {
            Flower pe = flowerSessionBeanLocal.createNewFlower(getNewFlowerEntity(), getNewFlowerEntityType());
            flowerEntities.add(pe);
            
            if(filteredFlowerEntities != null) {
                filteredFlowerEntities.add(pe);
            }
            
            newFlowerEntity = new Flower();
            newFlowerEntityType = null;
            uploadedFilePath = null;
            showUploadedFile = false;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New flower created successfully (Flower ID: " + pe.getFlowerId() + ")", null));
        } catch (InputDataValidationException | CreateNewFlowerException | FlowerTypeNotFoundException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new flower: " + ex.getMessage(), null));
        }
    }

    public void doUpdateFlower(ActionEvent event) {
        selectedFlowerEntityToUpdate = (Flower) event.getComponent().getAttributes().get("flowerEntityToUpdate");
        flowerTypeIdUpdate = selectedFlowerEntityToUpdate.getFlowerType().getFlowerTypeId();
        flowerColorUpdate = selectedFlowerEntityToUpdate.getFlowerColor();
    }

    public void updateFlower(ActionEvent event) {
        try {

            selectedFlowerEntityToUpdate.setFlowerColor(flowerColorUpdate);
            
            flowerSessionBeanLocal.updateFlower(selectedFlowerEntityToUpdate, flowerTypeIdUpdate);
            setFlowerEntities(flowerSessionBeanLocal.retrieveAllFlowers());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower updated successfully", null));
        } catch (FlowerNotFoundException | FlowerTypeNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating flower: " + ex.getMessage(), null));
        } catch (InputDataValidationException | UpdateFlowerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteFlower(ActionEvent event) {
        try {
            Flower flowerEntityToDelete = (Flower) event.getComponent().getAttributes().get("flowerEntityToDelete");
            System.out.println("================================"+flowerEntityToDelete.getName());
            flowerSessionBeanLocal.deleteFlower(flowerEntityToDelete.getFlowerId());

            getFlowerEntities().remove(flowerEntityToDelete);

            if (getFilteredFlowerEntities() != null) {
                getFilteredFlowerEntities().remove(flowerEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower deleted successfully", null));
        } catch (FlowerNotFoundException | DeleteFlowerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting flower: " + ex.getMessage(), null));
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
            getNewFlowerEntity().setImgAddress(uploadedFilePath);

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
            getSelectedFlowerEntityToUpdate().setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    /**
     * @return the newFlowerEntity
     */
    public Flower getNewFlowerEntity() {
        return newFlowerEntity;
    }

    /**
     * @param newFlowerEntity the newFlowerEntity to set
     */
    public void setNewFlowerEntity(Flower newFlowerEntity) {
        this.newFlowerEntity = newFlowerEntity;
    }

    /**
     * @return the flowerEntities
     */
    public List<Flower> getFlowerEntities() {
        return flowerEntities;
    }

    /**
     * @param flowerEntities the flowerEntities to set
     */
    public void setFlowerEntities(List<Flower> flowerEntities) {
        this.flowerEntities = flowerEntities;
    }

    /**
     * @return the filteredFlowerEntities
     */
    public List<Flower> getFilteredFlowerEntities() {
        return filteredFlowerEntities;
    }

    /**
     * @param filteredFlowerEntities the filteredFlowerEntities to set
     */
    public void setFilteredFlowerEntities(List<Flower> filteredFlowerEntities) {
        this.filteredFlowerEntities = filteredFlowerEntities;
    }

    /**
     * @return the selectedFlowerEntityToUpdate
     */
    public Flower getSelectedFlowerEntityToUpdate() {
        return selectedFlowerEntityToUpdate;
    }

    /**
     * @param selectedFlowerEntityToUpdate the selectedFlowerEntityToUpdate to
     * set
     */
    public void setSelectedFlowerEntityToUpdate(Flower selectedFlowerEntityToUpdate) {
        this.selectedFlowerEntityToUpdate = selectedFlowerEntityToUpdate;
    }

    /**
     * @return the uploadedFilePath
     */
    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    /**
     * @param uploadedFilePath the uploadedFilePath to set
     */
    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    /**
     * @return the showUploadedFile
     */
    public Boolean getShowUploadedFile() {
        return showUploadedFile;
    }

    /**
     * @param showUploadedFile the showUploadedFile to set
     */
    public void setShowUploadedFile(Boolean showUploadedFile) {
        this.showUploadedFile = showUploadedFile;
    }

    /**
     * @return the flowerTypeEntities
     */
    public List<FlowerType> getFlowerTypeEntities() {
        return flowerTypeEntities;
    }

    /**
     * @param flowerTypeEntities the flowerTypeEntities to set
     */
    public void setFlowerTypeEntities(List<FlowerType> flowerTypeEntities) {
        this.flowerTypeEntities = flowerTypeEntities;
    }

    /**
     * @return the flowerColorEnums
     */
    public List<FlowerColorEnum> getFlowerColorEnums() {
        return flowerColorEnums;
    }

    /**
     * @param flowerColorEnums the flowerColorEnums to set
     */
    public void setFlowerColorEnums(List<FlowerColorEnum> flowerColorEnums) {
        this.flowerColorEnums = flowerColorEnums;
    }

    /**
     * @return the newFlowerEntityType
     */
    public Long getNewFlowerEntityType() {
        return newFlowerEntityType;
    }

    /**
     * @param newFlowerEntityType the newFlowerEntityType to set
     */
    public void setNewFlowerEntityType(Long newFlowerEntityType) {
        this.newFlowerEntityType = newFlowerEntityType;
    }

    /**
     * @return the flowerTypeIdUpdate
     */
    public Long getFlowerTypeIdUpdate() {
        return flowerTypeIdUpdate;
    }

    /**
     * @param flowerTypeIdUpdate the flowerTypeIdUpdate to set
     */
    public void setFlowerTypeIdUpdate(Long flowerTypeIdUpdate) {
        this.flowerTypeIdUpdate = flowerTypeIdUpdate;
    }

}

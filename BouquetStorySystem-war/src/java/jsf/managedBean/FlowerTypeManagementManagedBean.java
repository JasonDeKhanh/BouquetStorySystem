/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.FlowerTypeSessionBeanLocal;
import entity.FlowerType;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.CreateNewFlowerTypeException;
import util.exception.DeleteFlowerTypeException;
import util.exception.FlowerTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFlowerTypeException;

/**
 *
 * @author xqy11
 */
@Named(value = "flowerTypeManagementManagedBean")
@ViewScoped
public class FlowerTypeManagementManagedBean implements Serializable{

    @EJB(name = "FlowerTypeSessionBeanLocal")
    private FlowerTypeSessionBeanLocal flowerTypeSessionBeanLocal;

    private FlowerType newFlowerTypeEntity;
    private List<FlowerType> flowerTypeEntities;
    private List<FlowerType> filteredFlowerTypeEntities;
    private FlowerType selectedFlowerTypeEntityToUpdate;
    
    public FlowerTypeManagementManagedBean() {
        newFlowerTypeEntity = new FlowerType();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setFlowerTypeEntities(flowerTypeSessionBeanLocal.retrieveAllCategories());
    }
    
    public void createNewFlowerType(ActionEvent event)
    {  
        try
        {
            FlowerType pe = flowerTypeSessionBeanLocal.createNewFlowerType(getNewFlowerTypeEntity());
            getFlowerTypeEntities().add(pe);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New flowerType created successfully (Flower Type ID: " + pe.getFlowerTypeId()+ ")", null));
        }
        catch(InputDataValidationException | CreateNewFlowerTypeException  ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new flower type: " + ex.getMessage(), null));
        }
    }

    public void doUpdateFlowerType(ActionEvent event)
    {
        selectedFlowerTypeEntityToUpdate = (FlowerType)event.getComponent().getAttributes().get("flowerTypeEntityToUpdate");

    }
    
    public void updateFlowerType(ActionEvent event)
    {        
        try
        {
            
            flowerTypeSessionBeanLocal.updateCategory(selectedFlowerTypeEntityToUpdate);
                        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower Type updated successfully", null));
        }
        catch(FlowerTypeNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating flower type: " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UpdateFlowerTypeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteFlowerType(ActionEvent event)
    {
        try
        {
            FlowerType flowerTypeEntityToDelete = (FlowerType)event.getComponent().getAttributes().get("flowerTypeEntityToDelete");
            flowerTypeSessionBeanLocal.deleteFlowerType(flowerTypeEntityToDelete.getFlowerTypeId());
            
            getFlowerTypeEntities().remove(flowerTypeEntityToDelete);
            
            if(getFilteredFlowerTypeEntities()!= null)
            {
                getFilteredFlowerTypeEntities().remove(flowerTypeEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower Type deleted successfully", null));
        }
        catch(FlowerTypeNotFoundException | DeleteFlowerTypeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting flower type: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    /**
     * @return the newFlowerTypeEntity
     */
    public FlowerType getNewFlowerTypeEntity() {
        return newFlowerTypeEntity;
    }

    /**
     * @param newFlowerTypeEntity the newFlowerTypeEntity to set
     */
    public void setNewFlowerTypeEntity(FlowerType newFlowerTypeEntity) {
        this.newFlowerTypeEntity = newFlowerTypeEntity;
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
     * @return the filteredFlowerTypeEntities
     */
    public List<FlowerType> getFilteredFlowerTypeEntities() {
        return filteredFlowerTypeEntities;
    }

    /**
     * @param filteredFlowerTypeEntities the filteredFlowerTypeEntities to set
     */
    public void setFilteredFlowerTypeEntities(List<FlowerType> filteredFlowerTypeEntities) {
        this.filteredFlowerTypeEntities = filteredFlowerTypeEntities;
    }

    /**
     * @return the selectedFlowerTypeEntityToUpdate
     */
    public FlowerType getSelectedFlowerTypeEntityToUpdate() {
        return selectedFlowerTypeEntityToUpdate;
    }

    /**
     * @param selectedFlowerTypeEntityToUpdate the selectedFlowerTypeEntityToUpdate to set
     */
    public void setSelectedFlowerTypeEntityToUpdate(FlowerType selectedFlowerTypeEntityToUpdate) {
        this.selectedFlowerTypeEntityToUpdate = selectedFlowerTypeEntityToUpdate;
    }
    
}

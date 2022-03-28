/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.DecorationSessionBeanLocal;
import entity.Decoration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
@RequestScoped
public class DecorationManagementManagedBean {

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;
    
    private List<Decoration> decorationEntities;
    private List<Decoration> filteredDecorationEntities;
    
    private Decoration newDecorationEntity;
    
    private Decoration selectedDecorationEntityToUpdate;
    
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

    
}

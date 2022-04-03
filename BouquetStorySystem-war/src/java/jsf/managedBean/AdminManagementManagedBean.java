/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.AdminSessionBeanLocal;
import entity.Admin;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdminException;

/**
 *
 * @author xqy11
 */
@Named(value = "adminManagementManagedBean")
@ViewScoped
public class AdminManagementManagedBean implements Serializable {

    @EJB(name = "AdminSessionBeanLocal")
    private AdminSessionBeanLocal adminSessionBeanLocal;

    private Admin newAdmin;
    private List<Admin> adminEntities;
    private List<Admin> filteredAdminEntities;
    private Admin selectedAdminEntityToUpdate;
    
    public AdminManagementManagedBean() {
        newAdmin = new Admin();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        adminEntities = adminSessionBeanLocal.retrieveAllAdmins();
    }
    
    
    public void createNewAdmin(ActionEvent event) {
        
        try {
            Admin admin = adminSessionBeanLocal.createNewAdmin(getNewAdmin());
            
            
            getAdminEntities().add(admin);
            
            if(filteredAdminEntities != null) {
                filteredAdminEntities.add(admin);
            }
            newAdmin = new Admin();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New admin created successfully (Admin ID: " + admin.getAdminId() + ")", null));
           

            
        } catch (AdminUsernameExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "An error has occurred while creating the new admin: The Admin Username already exist", null));
            
        } catch (UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Admin: " + ex.getMessage(), null));
            
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Admin (InputDataInvalidationError): " + ex.getMessage(), null));
            
        }
        
    }
    
    public void doUpdateAdmin(ActionEvent event) 
    {
        setSelectedAdminEntityToUpdate((Admin)event.getComponent().getAttributes().get("adminEntityToUpdate"));
        System.out.println("==============================================");
        System.out.println(getSelectedAdminEntityToUpdate().getUsername());
        System.out.println(getSelectedAdminEntityToUpdate().getFirstName());
        System.out.println("==============================================");
    }
    
    public void updateAdmin(ActionEvent event) {
        try {
            System.out.println("==================ffff============================");
            System.out.println(getSelectedAdminEntityToUpdate().getUsername());
            System.out.println(getSelectedAdminEntityToUpdate().getFirstName());
            System.out.println("=======================fffff=======================");

        
            adminSessionBeanLocal.updateAdmin(getSelectedAdminEntityToUpdate());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The Admin Username: "+getSelectedAdminEntityToUpdate().getUsername()+" has been updated successfully!", null));
            
        } catch (AdminNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting admin: " + ex.getMessage(), null));
        } catch (UpdateAdminException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the new Admin (UpdateAdminException): " + ex.getMessage(), null));
            
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the new Admin (InputDataInvalidationError): " + ex.getMessage(), null));
            
        }
    }
    
    public void deleteAdmin(ActionEvent event)
    {
        try
        {
            Admin adminEntityToDelete = (Admin)event.getComponent().getAttributes().get("adminEntityToDelete");
            adminSessionBeanLocal.deleteAdmin(adminEntityToDelete.getAdminId());
            
            adminEntities.remove(adminEntityToDelete);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin deleted successfully", null));
        }
        catch(AdminNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting admin: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the newAdmin
     */
    public Admin getNewAdmin() {
        return newAdmin;
    }

    /**
     * @param newAdmin the newAdmin to set
     */
    public void setNewAdmin(Admin newAdmin) {
        this.newAdmin = newAdmin;
    }

    /**
     * @return the adminEntities
     */
    public List<Admin> getAdminEntities() {
        return adminEntities;
    }

    /**
     * @param adminEntities the adminEntities to set
     */
    public void setAdminEntities(List<Admin> adminEntities) {
        this.adminEntities = adminEntities;
    }

    /**
     * @return the filteredAdminEntities
     */
    public List<Admin> getFilteredAdminEntities() {
        return filteredAdminEntities;
    }

    /**
     * @param filteredAdminEntities the filteredAdminEntities to set
     */
    public void setFilteredAdminEntities(List<Admin> filteredAdminEntities) {
        this.filteredAdminEntities = filteredAdminEntities;
    }

    /**
     * @return the selectedAdminEntityToUpdate
     */
    public Admin getSelectedAdminEntityToUpdate() {
        return selectedAdminEntityToUpdate;
    }

    /**
     * @param selectedAdminEntityToUpdate the selectedAdminEntityToUpdate to set
     */
    public void setSelectedAdminEntityToUpdate(Admin selectedAdminEntityToUpdate) {
        this.selectedAdminEntityToUpdate = selectedAdminEntityToUpdate;
    }

}

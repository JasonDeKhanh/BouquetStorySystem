/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;

/**
 *
 * @author xqy11
 */
@Named(value = "customerManagementManagedBean")
@ViewScoped
public class CustomerManagementManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    
    private List<Customer> customerEntities;
    private List<Customer> filteredCustumerEntities;
     

    public CustomerManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct()
    {
        setCustomerEntities(customerSessionBeanLocal.retrieveAllCustomers());
    }
    
    public void deleteCustomer(ActionEvent event)
    {
        Customer customerEntityToDelete = (Customer)event.getComponent().getAttributes().get("customerEntityToDelete");
        try {
            customerSessionBeanLocal.deleteCustomer(customerEntityToDelete.getCustomerId());
            
            customerEntities.remove(customerEntityToDelete);
            
            if(filteredCustumerEntities!=null){
                filteredCustumerEntities.remove(customerEntityToDelete);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer deleted successfully", null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found Exception: " + ex.getMessage(), null));
        } catch (DeleteCustomerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting customer:  " + ex.getMessage(), null));
        }
            
    }


    /**
     * @return the customerEntities
     */
    public List<Customer> getCustomerEntities() {
        return customerEntities;
    }

    /**
     * @param customerEntities the customerEntities to set
     */
    public void setCustomerEntities(List<Customer> customerEntities) {
        this.customerEntities = customerEntities;
    }



    /**
     * @return the filteredCustumerEntities
     */
    public List<Customer> getFilteredCustumerEntities() {
        return filteredCustumerEntities;
    }

    /**
     * @param filteredCustumerEntities the filteredCustumerEntities to set
     */
    public void setFilteredCustumerEntities(List<Customer> filteredCustumerEntities) {
        this.filteredCustumerEntities = filteredCustumerEntities;
    }
    
}

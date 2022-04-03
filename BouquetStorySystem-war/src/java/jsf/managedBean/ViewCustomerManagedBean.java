/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.AddressSessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import entity.Address;
import entity.Customer;
import entity.RegisteredGuest;
import entity.UnregisteredGuest;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;

/**
 *
 * @author xqy11
 */
@Named(value = "viewCustomerManagedBean")
@ViewScoped
public class ViewCustomerManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "AddressSessionBeanLocal")
    private AddressSessionBeanLocal addressSessionBeanLocal;

    private Customer customerEntityToView;
    private boolean isRegistered;
    private List<Address> addresses;
    private List<Customer> customerEntities;//FilteredCustomerEntities
    private List<Customer> filteredCustomerEntities;
            
    public ViewCustomerManagedBean() {
        if (customerEntityToView instanceof  RegisteredGuest) {
            customerEntityToView = new RegisteredGuest();
            isRegistered = true;
            
        } else {
            customerEntityToView = new UnregisteredGuest();
            isRegistered = false;
        }
//        System.out.println("=============================="+isRegistered);
    }

    
    public void updateIsRegister(ActionEvent event){
        customerEntityToView = (Customer)event.getComponent().getAttributes().get("customerEntityToView");
        
        if (customerEntityToView instanceof  RegisteredGuest) {
//            customerEntityToView = new RegisteredGuest();
            isRegistered = true;
            setAddresses(addressSessionBeanLocal.retrieveAllAddressesByCustomerId((RegisteredGuest) customerEntityToView));
            
        } else {
//            customerEntityToView = new UnregisteredGuest();
            isRegistered = false;
        }
    }
    
    public void deleteCustomer(ActionEvent event)
    {
        try
        {
            Customer customerEntityToDelete = (Customer)event.getComponent().getAttributes().get("customerEntityToDelete");
            customerSessionBeanLocal.deleteCustomer(customerEntityToDelete.getCustomerId());
            
            getCustomerEntities().remove(customerEntityToDelete);
            
            if(getFilteredCustomerEntities()!= null)
            {
                getFilteredCustomerEntities().remove(customerEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer deleted successfully", null));
        }
        catch(CustomerNotFoundException | DeleteCustomerException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting customer: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the customerEntityToView
     */
    public Customer getCustomerEntityToView() {
        return customerEntityToView;
    }

    /**
     * @param customerEntityToView the customerEntityToView to set
     */
    public void setCustomerEntityToView(Customer customerEntityToView) {
        this.customerEntityToView = customerEntityToView;
    }

    /**
     * @return the isRegistered
     */
    public boolean isIsRegistered() {
        return isRegistered;
    }

    /**
     * @param isRegistered the isRegistered to set
     */
    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    /**
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
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
     * @return the filteredCustomerEntities
     */
    public List<Customer> getFilteredCustomerEntities() {
        return filteredCustomerEntities;
    }

    /**
     * @param filteredCustomerEntities the filteredCustomerEntities to set
     */
    public void setFilteredCustomerEntities(List<Customer> filteredCustomerEntities) {
        this.filteredCustomerEntities = filteredCustomerEntities;
    }

}

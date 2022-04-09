/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author matt_
 */
@Local
public interface CustomerSessionBeanLocal {


    public List<Customer> retrieveAllCustomers();

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException;
    
}

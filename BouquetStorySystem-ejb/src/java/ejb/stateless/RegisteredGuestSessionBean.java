/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import entity.RegisteredGuest;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;

/**
 *
 * @author matt_
 */
@Stateless
public class RegisteredGuestSessionBean implements RegisteredGuestSessionBeanLocal {

    @EJB(name = "EmailSessionBeanLocal")
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    public RegisteredGuestSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    

    @Override
    public Long createNewRegisteredGuest(RegisteredGuest newRegisteredGuest) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<RegisteredGuest>> constraintViolations = validator.validate(newRegisteredGuest);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newRegisteredGuest);
                em.flush();

                Future<Boolean> asyncResult;
               
                try {
                    asyncResult = emailSessionBeanLocal.emailRegisterNotificationAsync(newRegisteredGuest, newRegisteredGuest.getEmail());
                    Thread thread = new Thread()
                    {
                        public void run()
                        {
                            try
                            {
                                if(asyncResult.get())
                                {
                                    System.out.println("[SERVER] Registration notification email actually sent successfully!\n");
                                }
                                else
                                {
                                    System.out.println("[SERVER] Registration notification email was NOT actually sent!\n");
                                }
                            }
                            catch(ExecutionException | InterruptedException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } catch (InterruptedException ex) {
                    Logger.getLogger(RegisteredGuestSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return newRegisteredGuest.getCustomerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerEmailExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }
    
    @Override
    public RegisteredGuest retrieveRegisteredGuestByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (RegisteredGuest) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer email " + email + " does not exist!");
        }

    }
    
    @Override
    public RegisteredGuest retrieveRegisteredGuestByCustomerId(Long customerId) throws CustomerNotFoundException {
        RegisteredGuest customer = (RegisteredGuest)em.find(Customer.class, customerId);

        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }
    
    @Override
    public RegisteredGuest registeredGuestLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            RegisteredGuest customer = retrieveRegisteredGuestByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customer.getSalt()));

            if (customer.getPassword().equals(passwordHash)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }

        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    // update
    @Override
    public RegisteredGuest updateRegisteredGuest(RegisteredGuest registeredGuest) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (registeredGuest != null && registeredGuest.getCustomerId() != null) {
            Set<ConstraintViolation<RegisteredGuest>> constraintViolations = validator.validate(registeredGuest);

            if (constraintViolations.isEmpty()) {
                RegisteredGuest registeredGuestToUpdate = retrieveRegisteredGuestByCustomerId(registeredGuest.getCustomerId());

                if (registeredGuestToUpdate.getEmail().equals(registeredGuest.getEmail())) {
                    //should I allow upate of email and password?
                    registeredGuestToUpdate.setFirstName(registeredGuest.getFirstName());
                    registeredGuestToUpdate.setLastName(registeredGuest.getLastName());
//                    System.out.println("=================================================");
//                    System.out.println(registeredGuest.getPassword());
//                    if(registeredGuest.getPassword()!= "") {
//                        registeredGuestToUpdate.setPassword(registeredGuest.getPassword());
//                    }
                    return registeredGuestToUpdate;
                } else {
                    throw new UpdateCustomerException("Email of customer record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }

    }
    
    @Override
    public RegisteredGuest updatePassword(RegisteredGuest registeredGuest) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (registeredGuest != null && registeredGuest.getCustomerId() != null) {
            Set<ConstraintViolation<RegisteredGuest>> constraintViolations = validator.validate(registeredGuest);

            if (constraintViolations.isEmpty()) {
                RegisteredGuest registeredGuestToUpdate = retrieveRegisteredGuestByCustomerId(registeredGuest.getCustomerId());

                
                System.out.println("========================================");
                System.out.println(registeredGuest.getPassword());
                if (registeredGuestToUpdate.getEmail().equals(registeredGuest.getEmail())) {
                    em.merge(registeredGuest);
//                        registeredGuestToUpdate.setPassword(registeredGuest.getPassword());
                    return registeredGuestToUpdate;
                } else {
                    throw new UpdateCustomerException("Email of customer record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }

    }

    /* delete
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException{
        Customer customerToRemove = retrieveCustomerByCustomerId(customerId);
        
        if(customerToRemove.getSaleTransactions().isEmpty())
        {
            if(customerToRemove instanceof RegisteredGuest){
                ((RegisteredGuest) customerToRemove).setAddresses(null);
            }
            em.remove(customerToRemove);
        }
        else
        {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteCustomerException("Customer ID " + customerId + " is associated with existing sale transaction(s) and cannot be deleted!");
        }

        
    }
*/
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RegisteredGuest>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    
}

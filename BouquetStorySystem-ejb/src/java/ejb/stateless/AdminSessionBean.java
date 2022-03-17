/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Admin;
import java.util.List;
import java.util.Set;
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
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdminException;
import util.security.CryptographicHelper;

/**
 *
 * @author msipc
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public AdminSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    // create
    @Override
    public Long createNewAdmin(Admin newAdmin) throws AdminUsernameExistException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Admin>>constraintViolations = validator.validate(newAdmin);
        
        if(constraintViolations.isEmpty())
        {
            try 
            {
                em.persist(newAdmin);
                em.flush();
                
                return newAdmin.getAdminId();
            } 
            catch (PersistenceException ex) 
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new AdminUsernameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } 
        else 
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        
    }
    

    // retrieve all
    @Override
    public List<Admin> retrieveAllAdmins()
    {
        Query query = em.createQuery("SELECT a FROM Admin a");
        
        return query.getResultList();
    }
    
    // retrieve by adminId
    @Override
    public Admin retrieveAdminByAdminId(Long adminId) throws AdminNotFoundException
    {
        Admin admin = em.find(Admin.class, adminId);
        
        if(admin != null)
        {
            return admin;
        }
        else
        {
            throw new AdminNotFoundException("Admin ID " + adminId + " does not exist!");
        }
    }
    
    
    // retrieve by username
    @Override
    public Admin retrieveAdminByUsername(String username) throws AdminNotFoundException
    {
        Query query = em.createQuery("SELECT a FROM Admin a WHERE a.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try 
        {
            return (Admin)query.getSingleResult();
        } 
        catch (NoResultException | NonUniqueResultException ex) 
        {
            throw new AdminNotFoundException("Admin username " + username + " does not exist!");
        }
        
    }
    
    
     // login
    @Override
    public Admin adminLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try 
        {
            Admin admin = retrieveAdminByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + admin.getSalt()));
        
            if(admin.getPassword().equals(passwordHash))
            {
                return admin;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        
        } 
        catch (AdminNotFoundException ex) 
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    // update
    @Override
    public void updateAdmin(Admin admin) throws AdminNotFoundException, UpdateAdminException, InputDataValidationException
    {
        if(admin != null && admin.getAdminId() != null)
        {
            Set<ConstraintViolation<Admin>>constraintViolations = validator.validate(admin);
        
            if(constraintViolations.isEmpty())
            {
                Admin adminToUpdate = retrieveAdminByAdminId(admin.getAdminId());
                
                if(adminToUpdate.getUsername().equals(admin.getUsername()))
                {
                    // update
                }
                else
                {
                    throw new UpdateAdminException("Username of admin record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new AdminNotFoundException("Admin ID not provided for admin to be updated");
        }
        
    }
    
    
    // delete
    @Override
    public void deleteAdmin(Long adminId) throws AdminNotFoundException
    {
        Admin adminToRemove = retrieveAdminByAdminId(adminId);
        
        em.remove(adminToRemove);
    }

    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Admin>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}

package ejb.session.singleton;

import ejb.stateless.AdminSessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import entity.Admin;
import entity.RegisteredGuest;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.PersistenceContext;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.CustomerEmailExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;



@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean
{
    @EJB(name = "AdminSessionBeanLocal")
    private AdminSessionBeanLocal adminSessionBeanLocal;
    
    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    
    
    
    public DataInitializationSessionBean()
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            adminSessionBeanLocal.retrieveAdminByUsername("manager");
        }
        catch(AdminNotFoundException ex)
        {
            initializeData();
        }
    }
    
    
    
    // Updated in v4.1
    // Updated in v4.2 with bean validation
    
    private void initializeData()
    {
        try
        {
            
            adminSessionBeanLocal.createNewAdmin(new Admin("Default", "Manager", "manager", "password"));

            customerSessionBeanLocal.createNewCustomer(new RegisteredGuest("Default", "Customer","customer@gmail.com","password"));
        }
        catch(AdminUsernameExistException | CustomerEmailExistException | UnknownPersistenceException | InputDataValidationException ex)
        {
            ex.printStackTrace();
        }
    }
}
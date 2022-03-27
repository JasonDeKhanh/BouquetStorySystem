package ejb.singleton;

import ejb.stateless.AdminSessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import entity.Admin;
import entity.Decoration;
import entity.RegisteredGuest;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.PersistenceContext;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.CreateNewDecorationException;
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
    
    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;
    
    
    
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
            
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration A","xxx.png","Some description...",200,300,new BigDecimal(12.90), true));
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration B","xxx.png","Some description...",100,200,new BigDecimal(9.90), false));
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration C","xxx.png","Some description...",200,500,new BigDecimal(20.90), true));
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration D","xxx.png","Some description...",250,400,new BigDecimal(4.90), false));
        }
        catch(AdminUsernameExistException | CustomerEmailExistException | CreateNewDecorationException | UnknownPersistenceException | InputDataValidationException ex)
        {
            ex.printStackTrace();
        }
    }
}
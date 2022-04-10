package ejb.singleton;

import ejb.stateless.AddressSessionBeanLocal;
import ejb.stateless.AdminSessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.RegisteredGuestSessionBeanLocal;
import entity.Address;
import entity.Admin;
import entity.Customer;
import entity.Decoration;
import entity.RegisteredGuest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @EJB(name = "RegisteredGuestSessionBeanLocal")
    private RegisteredGuestSessionBeanLocal registeredGuestSessionBeanLocal;

    @EJB(name = "AddressSessionBeanLocal")
    private AddressSessionBeanLocal addressSessionBeanLocal;

    
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

            RegisteredGuest newCustomer = new RegisteredGuest("Default3", "Customer3","customer3@gmail.com","password");
            List<Address> addresses = new ArrayList<>();
            Address newAddress = new Address("Address Line","888000");
            newAddress.setCustomer((RegisteredGuest)newCustomer);
            addresses.add(newAddress);
            ((RegisteredGuest)newCustomer).setAddresses(addresses);
            
            registeredGuestSessionBeanLocal.createNewRegisteredGuest(new RegisteredGuest("Default", "Customer","customer@gmail.com","password"));
            registeredGuestSessionBeanLocal.createNewRegisteredGuest(new RegisteredGuest("Default2", "Customer2","customer2@gmail.com","password"));
            registeredGuestSessionBeanLocal.createNewRegisteredGuest(newCustomer);
    
            addressSessionBeanLocal.createNewAddress(newAddress);
            
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
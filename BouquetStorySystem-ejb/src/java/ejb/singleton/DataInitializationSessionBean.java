package ejb.singleton;

import ejb.stateless.AddOnSessionBeanLocal;
import ejb.stateless.AddressSessionBeanLocal;
import ejb.stateless.AdminSessionBeanLocal;
import ejb.stateless.BundleSessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.GiftCardSessionBeanLocal;
import ejb.stateless.GiftCardTypeSessionBeanLocal;
import ejb.stateless.RegisteredGuestSessionBeanLocal;
import ejb.stateless.SaleTransactionSessionBeanLocal;
import entity.AddOn;
import entity.Address;
import entity.Admin;
import entity.Bundle;
import entity.Customer;
import entity.Decoration;
import entity.GiftCard;
import entity.GiftCardType;
import entity.RegisteredGuest;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.PersistenceContext;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.CreateNewAddOnException;
import util.exception.CreateNewDecorationException;
import util.exception.CreateNewGiftCardException;
import util.exception.CreateNewGiftCardTypeException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;



@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean
{

    @EJB(name = "GiftCardSessionBeanLocal")
    private GiftCardSessionBeanLocal giftCardSessionBeanLocal;

    @EJB(name = "GiftCardTypeSessionBeanLocal")
    private GiftCardTypeSessionBeanLocal giftCardTypeSessionBeanLocal;

    @EJB(name = "BundleSessionBeanLocal")
    private BundleSessionBeanLocal bundleSessionBeanLocal;

    @EJB(name = "AddOnSessionBeanLocal")
    private AddOnSessionBeanLocal addOnSessionBeanLocal;

    @EJB(name = "SaleTransactionSessionBeanLocal")
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;

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
            AddOn newAddOn = addOnSessionBeanLocal.createNewAddOn(new AddOn("AddOn 2", "/uploadedFiles/e.png", "Add On 2 description", 15, 10, new BigDecimal(12.00), true));
            SaleTransactionLineItem newSaleTransactionLineItem = new SaleTransactionLineItem(123, 2, new BigDecimal(10.00), newAddOn);
            GiftCardType newGiftCardType = giftCardTypeSessionBeanLocal.createNewGiftCardType(new GiftCardType("GiftCardType 1", "/uploadedFiles/e.png", "1X1", "descpription", 16, 10, new BigDecimal(15.00), true));
            GiftCard newGiftCard = giftCardSessionBeanLocal.createNewGiftCard(new GiftCard("hello world", "/uploadedFiles/e.png"));
            newGiftCard.setGiftCardType(newGiftCardType);
            SaleTransactionLineItem secondSaleTransactionLineItem = new SaleTransactionLineItem(1234, 3, new BigDecimal(15.00), newGiftCard);
            SaleTransaction newSaleTransaction = new SaleTransaction(1,2,new BigDecimal(20.00), new Date(), new Date(), true, "null", false, true, true);
            List<SaleTransactionLineItem> lineItems = new ArrayList<>();
            lineItems.add(newSaleTransactionLineItem);
            lineItems.add(secondSaleTransactionLineItem);
            newSaleTransaction.setSaleTransactionLineItems(lineItems);
            saleTransactionSessionBeanLocal.createNewSaleTransaction(newCustomer.getCustomerId(), newSaleTransaction);
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration A","xxx.png","Some description...",200,300,new BigDecimal(12.90), true));
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration B","xxx.png","Some description...",100,200,new BigDecimal(9.90), false));
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration C","xxx.png","Some description...",200,500,new BigDecimal(20.90), true));
            decorationSessionBeanLocal.createNewDecoration(new Decoration("Decoration D","xxx.png","Some description...",250,400,new BigDecimal(4.90), false));
        }
        catch(AdminUsernameExistException | CustomerEmailExistException | CreateNewDecorationException | UnknownPersistenceException | InputDataValidationException | CustomerNotFoundException | CreateNewSaleTransactionException | CreateNewAddOnException | CreateNewGiftCardTypeException | CreateNewGiftCardException ex)
        {
            ex.printStackTrace();
        }
    }
}
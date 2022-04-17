/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.ItemSessionBeanLocal;
import ejb.stateless.RegisteredGuestSessionBeanLocal;
import ejb.stateless.SaleTransactionSessionBeanLocal;
import ejb.stateless.UnregisteredGuestSessionBeanLocal;
import entity.Customer;
import entity.Item;
import entity.RegisteredGuest;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import entity.UnregisteredGuest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.datamodel.SalesTransactionReq;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * REST Web Service
 *
 * @author xqy11
 */
@Path("SaleTransaction")
public class SaleTransactionResource {

    UnregisteredGuestSessionBeanLocal unregisteredGuestSessionBean = lookupUnregisteredGuestSessionBeanLocal();

    ItemSessionBeanLocal itemSessionBean = lookupItemSessionBeanLocal();

    RegisteredGuestSessionBeanLocal registeredGuestSessionBeanLocal = lookupRegisteredGuestSessionBeanLocal();

    CustomerSessionBeanLocal customerSessionBean = lookupCustomerSessionBeanLocal();

    SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal = lookupSaleTransactionSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SaleTransactionResource
     */
    public SaleTransactionResource() {
    }
    
    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSaleTransaction(SalesTransactionReq newSalesTransactionReq)
    {
        if(newSalesTransactionReq != null)
        {
            try
            {
                Customer customer;
                
                try{
                    customer = customerSessionBean.retrieveCustomerByEmail(newSalesTransactionReq.getUsername());
                }catch(Exception ex) {
                    UnregisteredGuest newGuest = new UnregisteredGuest(newSalesTransactionReq.getFirstName(), newSalesTransactionReq.getLastName(), newSalesTransactionReq.getUsername());
                    Long customerId = unregisteredGuestSessionBean.createNewUnregisteredGuest(newGuest);
                    customer = customerSessionBean.retrieveCustomerByEmail(newSalesTransactionReq.getUsername());
                }
                
                SaleTransactionLineItem[] saleTransactionLineItems = newSalesTransactionReq.getSaleTransactionLineItems();
               
                List<Item> items = new ArrayList<>();
                
                
                for(int i :  newSalesTransactionReq.getItems()){
                    Long id = new Long(i);
                    items.add(itemSessionBean.retrieveItemByItemId(id));
                }
         
                
                SaleTransaction newSaleTransactionToAdd = newSalesTransactionReq.getSaleTransaction();
                System.out.println("====================> A sample new Date() can be persist in EJB: "+ new Date());
//                //collectionDateTimecollectionDatetimecollectionDatetime collectionDatetime
                System.out.println("====================> Retrive Collection Time from json: "+ newSaleTransactionToAdd.getCollectionDateTime());
//                System.out.println("==========================================>: "+ newSaleTransactionToAdd.getCollectionDateTime().toString());
//                System.out.println("===xx===="+ newSaleTransactionToAdd.getTransactionDateTime());
//                Date collecitonTime = new Date(newSaleTransactionToAdd.getCollectionDateTime().toString());
                
//                Date collectioDate = new Date(newSaleTransactionToAdd.getCollectionDateTime().getTime());
                
//                Timestamp ts=new Timestamp(newSaleTransactionToAdd.getCollectionDateTime().getTime());  

//                Date firstDate1 = new Date(2022, 12, 12);
                if(newSaleTransactionToAdd.getCollectionDateTime() instanceof Date) {
                    System.out.println("============this datteeeeeeee");
                }
                LocalDateTime newDate = convertToLocalDateTimeViaInstant(newSaleTransactionToAdd.getCollectionDateTime());
                
                Instant instant = newDate.atZone(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                newSaleTransactionToAdd.setCollectionDateTime(date);

//                newSaleTransactionToAdd.setCollectionDateTime(new Date());
                
//                
//                
//                public void updateDeliveryDate(Long saleTransactionId, LocalDateTime newDate) {
//        SaleTransaction saleTransaction = em.find(SaleTransaction.class, saleTransactionId);
//        Instant instant = newDate.atZone(ZoneId.systemDefault()).toInstant();
//        Date date = Date.from(instant);
//        saleTransaction.setCollectionDateTime(date);

//    }
//               
                newSaleTransactionToAdd.setTransactionDateTime(new Date());
                
                List<SaleTransactionLineItem> lineItems = new ArrayList<>();
                
//                for(SaleTransactionLineItem lineItem : saleTransactionLineItems ) {
                for(int i = 0; i < saleTransactionLineItems.length;i++) {
                    System.out.println("in saleTrans rws, lineItem.item: " + saleTransactionLineItems[i].getItemEntity());
                    
                    SaleTransactionLineItem newLineItem = new SaleTransactionLineItem(saleTransactionLineItems[i].getSerialNumber(),saleTransactionLineItems[i].getQuantity(),saleTransactionLineItems[i].getUnitPrice(),saleTransactionLineItems[i].getItemEntity());
                    newLineItem.setItemEntity(items.get(i));
                    
                    System.out.println("=====================>"+newLineItem.getItemEntity().getItemId());
                    lineItems.add(newLineItem);
                }
                newSaleTransactionToAdd.setSaleTransactionLineItems(lineItems);
                
                SaleTransaction salesTransaction = saleTransactionSessionBeanLocal.createNewSaleTransaction(customer.getCustomerId(),newSaleTransactionToAdd);
                
                return Response.status(Response.Status.OK).entity(salesTransaction.getSaleTransactionId()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new sales transaction request").build();
        }
    }
    
    @Path("retrieveAllTransactions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTransactions(@QueryParam("username") String username)
    {
        try
        {
            RegisteredGuest customerEntity = registeredGuestSessionBeanLocal.retrieveRegisteredGuestByEmail(username);
            
            List<SaleTransaction> saleTransactionEntities = saleTransactionSessionBeanLocal.retrieveAllSaleTransactionsByCustomerId(customerEntity.getCustomerId());
            
            for(SaleTransaction salesEntity:saleTransactionEntities)
            {
                salesEntity.setCustomer(null);
                
//                for(SaleTransactionLineItem item : salesEntity.getSaleTransactionLineItems()) {
//                    item.(null);
//                }
            }
            
            GenericEntity<List<SaleTransaction>> genericEntity = new GenericEntity<List<SaleTransaction>>(saleTransactionEntities) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private SaleTransactionSessionBeanLocal lookupSaleTransactionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SaleTransactionSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/SaleTransactionSessionBean!ejb.stateless.SaleTransactionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomerSessionBeanLocal lookupCustomerSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/CustomerSessionBean!ejb.stateless.CustomerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private RegisteredGuestSessionBeanLocal lookupRegisteredGuestSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RegisteredGuestSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/RegisteredGuestSessionBean!ejb.stateless.RegisteredGuestSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ItemSessionBeanLocal lookupItemSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ItemSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/ItemSessionBean!ejb.stateless.ItemSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private UnregisteredGuestSessionBeanLocal lookupUnregisteredGuestSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (UnregisteredGuestSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/UnregisteredGuestSessionBean!ejb.stateless.UnregisteredGuestSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}

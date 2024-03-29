/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.RegisteredGuestSessionBeanLocal;
import entity.Address;
import entity.Customer;
import entity.Flower;
import entity.RegisteredGuest;
import entity.SaleTransaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;

/**
 * REST Web Service
 *
 * @author xqy11
 */
@Path("Customer")
public class CustomerResource {

    RegisteredGuestSessionBeanLocal registeredGuestSessionBean = lookupRegisteredGuestSessionBeanLocal();

    CustomerSessionBeanLocal customerSessionBean = lookupCustomerSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
    }
    
    
    @Path("customerLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("username") String username, 
                                @QueryParam("password") String password)
    {
        try
        {
            RegisteredGuest customerEntity = registeredGuestSessionBean.registeredGuestLogin(username, password);
            System.out.println("********** StaffResource.staffLogin(): Customer " + customerEntity.getEmail()+ " login remotely via web service");

            customerEntity.setPassword(null);
            customerEntity.setSalt(null);
            customerEntity.getSaleTransactions().clear();
            
            for(Address address : customerEntity.getAddresses()){
                address.setCustomer(null);
            }
            
            return Response.status(Status.OK).entity(customerEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCustomer(RegisteredGuest newCustomer)
    {
        if(newCustomer != null)
        {
            try
            {
                    Long newRcordId = registeredGuestSessionBean.createNewRegisteredGuest(newCustomer);

                    return Response.status(Response.Status.OK).entity(newRcordId).build();
            }				
            catch(Exception ex)
            {
                    return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("updateCustomer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(RegisteredGuest updateCustomer)
    {
        if(updateCustomer != null)
        {
            try
            {                
//                System.out.println("===================================================");
//                System.out.println(updateCustomer.getPassword());
                RegisteredGuest newCustomer = registeredGuestSessionBean.updateRegisteredGuest(updateCustomer);
                newCustomer.setPassword(null);
                newCustomer.setSalt(null);
                newCustomer.getSaleTransactions().clear();
                return Response.status(Response.Status.OK).entity(newCustomer).build();
            }
            catch(UpdateCustomerException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }
    
    @Path("updatePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePassword(RegisteredGuest updateCustomer)
    {
        if(updateCustomer != null)
        {
            try
            {                
                System.out.println("===================================================");
                System.out.println(updateCustomer.getPassword());
                RegisteredGuest newCustomer = registeredGuestSessionBean.updatePassword(updateCustomer);
                newCustomer.setPassword(null);
                newCustomer.setSalt(null);
                newCustomer.getSaleTransactions().clear();
                return Response.status(Response.Status.OK).entity(newCustomer).build();
            }
            catch(UpdateCustomerException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
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
}

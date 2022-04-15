/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.AddressSessionBeanLocal;
import ejb.stateless.RegisteredGuestSessionBeanLocal;
import entity.Address;
import entity.RegisteredGuest;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InvalidLoginCredentialException;
import ws.datamodel.AddressReq;

/**
 * REST Web Service
 *
 * @author xqy11
 */
@Path("Address")
public class AddressResource {

    RegisteredGuestSessionBeanLocal registeredGuestSessionBeanLocal = lookupRegisteredGuestSessionBeanLocal();

    AddressSessionBeanLocal addressSessionBeanLocal = lookupAddressSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddressResource
     */
    public AddressResource() {
        
    }

    @Path("retrieveAllAddresses")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAddresses(@QueryParam("username") String username)
    {
        try
        {
            RegisteredGuest customerEntity = registeredGuestSessionBeanLocal.retrieveRegisteredGuestByEmail(username);
            
            List<Address> addressesEntities = addressSessionBeanLocal.retrieveAllAddressesByCustomerId(customerEntity);
            
            for(Address addressEntity:addressesEntities)
            {
                addressEntity.setCustomer(null);
            }
            
            GenericEntity<List<Address>> genericEntity = new GenericEntity<List<Address>>(addressesEntities) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAddress(AddressReq addressReq)
    {
        if(addressReq != null)
        {
            try
            {
                Address address = addressReq.getAddress();
                address.setCustomer(registeredGuestSessionBeanLocal.retrieveRegisteredGuestByEmail(addressReq.getUsername()));
                Address addressEntity = addressSessionBeanLocal.createNewAddress(address);
                
                return Response.status(Response.Status.OK).entity(addressEntity.getAddressId()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new address request").build();
        }
    }
    
    @Path("{addressId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("addressId") Long addressId)
    {
        try
        {
            addressSessionBeanLocal.deleteAddress(addressId);
            
            return Response.status(Response.Status.OK).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(Address updateAddress)
    {
        if(updateAddress != null)
        {
            try
            {                
                
                addressSessionBeanLocal.updateAdress(updateAddress);
                
                return Response.status(Response.Status.OK).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }
    
    
    private AddressSessionBeanLocal lookupAddressSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AddressSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/AddressSessionBean!ejb.stateless.AddressSessionBeanLocal");
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

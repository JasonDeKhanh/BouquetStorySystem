/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.CreditCardSessionBeanLocal;
import ejb.stateless.RegisteredGuestSessionBeanLocal;
import entity.Address;
import entity.CreditCard;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.datamodel.AddressReq;
import ws.datamodel.CreditCardReq;

/**
 * REST Web Service
 *
 * @author xqy11
 */
@Path("CreditCard")
public class CreditCardResource {

    RegisteredGuestSessionBeanLocal registeredGuestSessionBeanLocal = lookupRegisteredGuestSessionBeanLocal();

    CreditCardSessionBeanLocal creditCardSessionBeanLocal = lookupCreditCardSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CreditCardResource
     */
    public CreditCardResource() {
    }

    @Path("retrieveAllCreditCards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCreditCards(@QueryParam("username") String username)
    {
        try
        {
            System.out.println("================");
            System.out.println(username);
            RegisteredGuest customerEntity = registeredGuestSessionBeanLocal.retrieveRegisteredGuestByEmail(username);

            System.out.println("================");

            System.out.println(customerEntity.getCustomerId());
            List<CreditCard> creditCardEntities = creditCardSessionBeanLocal.retrieveRegisteredGuestCreditCards(customerEntity.getCustomerId());
            
            
            GenericEntity<List<CreditCard>> genericEntity = new GenericEntity<List<CreditCard>>(creditCardEntities) {
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
    public Response createCreditCard(CreditCardReq creditCardReq)
    {
        if(creditCardReq != null)
        {
            try
            {
                CreditCard cc = creditCardReq.getCreditCard();
                RegisteredGuest customer = registeredGuestSessionBeanLocal.retrieveRegisteredGuestByEmail(creditCardReq.getUsername());
//                
//                customer.getCreditCards().add(cc);
//                registeredGuestSessionBeanLocal.updateRegisteredGuest(customer);
                
                CreditCard ccEntity = creditCardSessionBeanLocal.createNewCreditCard(customer.getCustomerId(),cc);
                
                return Response.status(Response.Status.OK).entity(ccEntity.getCreditCardId()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new credit card request").build();
        }
    }
    
    @Path("{deleteCard}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCard(@QueryParam("creditCardId") Long creditCardId, @QueryParam("username") String username)
    {
        try
        {
            Long id = registeredGuestSessionBeanLocal.retrieveRegisteredGuestByEmail(username).getCustomerId();
            creditCardSessionBeanLocal.deleteCreditCard(creditCardId,id);
            
            return Response.status(Response.Status.OK).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private CreditCardSessionBeanLocal lookupCreditCardSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CreditCardSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/CreditCardSessionBean!ejb.stateless.CreditCardSessionBeanLocal");
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

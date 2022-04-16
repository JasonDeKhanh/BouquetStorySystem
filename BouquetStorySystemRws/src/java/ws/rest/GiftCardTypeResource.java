package ws.rest;

import ejb.stateless.GiftCardTypeSessionBeanLocal;
import entity.GiftCardType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author JORD-SSD
 */
@Path("GiftCardType")
public class GiftCardTypeResource {

    GiftCardTypeSessionBeanLocal giftCardTypeSessionBeanLocal = lookupGiftCardTypeSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GiftCardTypeResource
     */
    public GiftCardTypeResource() {
    }

    @Path("retrieveAllGiftCardTypes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllGiftCardTypes() {
        try
        {
            List<GiftCardType> giftCardTypeEntities = giftCardTypeSessionBeanLocal.retrieveAllGiftCardTypes();

            GenericEntity<List<GiftCardType>> genericEntity = new GenericEntity<List<GiftCardType>>(giftCardTypeEntities) {};

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveGiftCardType/{giftCardTypeId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGiftCardType(@PathParam("giftCardTypeId") Long giftCardTypeId) {
        try {
            GiftCardType giftCardTypeEntity = giftCardTypeSessionBeanLocal.retrieveGiftCardTypeByGiftCardTypeId(giftCardTypeId);
            
            return Response.status(Response.Status.OK).entity(giftCardTypeEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private GiftCardTypeSessionBeanLocal lookupGiftCardTypeSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (GiftCardTypeSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/GiftCardTypeSessionBean!ejb.stateless.GiftCardTypeSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

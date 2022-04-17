package ws.rest;

import ejb.stateless.ItemSessionBeanLocal;
import entity.Item;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author JORD-SSD
 */
@Path("Item")
public class ItemResource {

    ItemSessionBeanLocal itemSessionBeanLocal = lookupItemSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ProductResource
     */
    public ItemResource() {
    }
    
    @Path("retrieveAllAdminCreatedItems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAdminCreatedItems() {
        try
        {
            List<Item> itemEntities = itemSessionBeanLocal.retrieveAllAdminCreatedItems();

            GenericEntity<List<Item>> genericEntity = new GenericEntity<List<Item>>(itemEntities) {};

            return Response.status(Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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

    
}

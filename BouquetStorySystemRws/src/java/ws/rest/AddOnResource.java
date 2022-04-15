package ws.rest;

import ejb.stateless.AddOnSessionBeanLocal;
import entity.AddOn;
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
@Path("AddOn")
public class AddOnResource {

    AddOnSessionBeanLocal addOnSessionBeanLocal = lookupAddOnSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddOnResource
     */
    public AddOnResource() {
    }

    @Path("retrieveAllAddOns")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAddOns() {
        try
        {
            List<AddOn> addOnEntities = addOnSessionBeanLocal.retrieveAllAddOns();

            GenericEntity<List<AddOn>> genericEntity = new GenericEntity<List<AddOn>>(addOnEntities) {};

            return Response.status(Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private AddOnSessionBeanLocal lookupAddOnSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AddOnSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/AddOnSessionBean!ejb.stateless.AddOnSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

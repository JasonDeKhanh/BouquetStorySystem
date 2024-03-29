package ws.rest;

import ejb.stateless.PremadeBouquetSessionBeanLocal;
import entity.PremadeBouquet;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author JORD-SSD
 */
@Path("PremadeBouquet")
public class PremadeBouquetResource {

    PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal = lookupPremadeBouquetSessionBeanLocal();

    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of PremadeBouquetResource
     */
    public PremadeBouquetResource() {
    }
    
    @Path("retrieveAllPremadeBouquets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPremadeBouquets() {
        try
        {
            List<PremadeBouquet> premadeBouquetEntities = premadeBouquetSessionBeanLocal.retrieveAllPremadeBouquets();

            GenericEntity<List<PremadeBouquet>> genericEntity = new GenericEntity<List<PremadeBouquet>>(premadeBouquetEntities) {};

            return Response.status(Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private PremadeBouquetSessionBeanLocal lookupPremadeBouquetSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PremadeBouquetSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/PremadeBouquetSessionBean!ejb.stateless.PremadeBouquetSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.BouquetSessionBeanLocal;
import ejb.stateless.PremadeBouquetSessionBeanLocal;
import entity.Bouquet;
import entity.Flower;
import entity.PremadeBouquet;
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

/**
 * REST Web Service
 *
 * @author xqy11
 */
@Path("Bouquet")
public class BouquetResource {

    PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal = lookupPremadeBouquetSessionBeanLocal();

    BouquetSessionBeanLocal bouquetSessionBeanLocal = lookupBouquetSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BouquetResource
     */
    public BouquetResource() {
    }
    
    @Path("retrieveAllPremadeBouquets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFlowers()
    {
        try
        {
            List<PremadeBouquet> premadeBouquetEntities = premadeBouquetSessionBeanLocal.retrieveAllPremadeBouquets();
            
            for(PremadeBouquet premadeBouquetEntity:premadeBouquetEntities)
            {
                for(Flower flowerEntity : premadeBouquetEntity.getFlowers()) {
                    flowerEntity.getFlowerType().setFlowerEntities(null);
                }
                
            }
            
            GenericEntity<List<PremadeBouquet>> genericEntity = new GenericEntity<List<PremadeBouquet>>(premadeBouquetEntities) {};

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private BouquetSessionBeanLocal lookupBouquetSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (BouquetSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/BouquetSessionBean!ejb.stateless.BouquetSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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

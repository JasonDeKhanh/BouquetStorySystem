/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.FlowerSessionBeanLocal;
import entity.Flower;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author xqy11
 */
@Path("Flower")
public class FlowerResource {

    FlowerSessionBeanLocal flowerSessionBean = lookupFlowerSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FlowerResource
     */
    public FlowerResource() {
    }
    
    @Path("retrieveAllFlowers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFlowers()
    {
        try
        {
            List<Flower> flowerEntities = flowerSessionBean.retrieveAllFlowers();

            for(Flower flowerEntity:flowerEntities)
            {                
                flowerEntity.getFlowerType().setFlowerEntities(null);
            }
            
            GenericEntity<List<Flower>> genericEntity = new GenericEntity<List<Flower>>(flowerEntities) {};

            return Response.status(Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private FlowerSessionBeanLocal lookupFlowerSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FlowerSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/FlowerSessionBean!ejb.stateless.FlowerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
    
    
}

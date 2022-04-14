/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.FlowerTypeSessionBeanLocal;
import entity.FlowerType;
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
 * @author msipc
 */
@Path("FlowerType")
public class FlowerTypeResource {

    FlowerTypeSessionBeanLocal flowerTypeSessionBean = lookupFlowerTypeSessionBeanLocal();

    
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FlowerTypeResource
     */
    public FlowerTypeResource() {
    }

    @Path("retrieveAllFlowerTypes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFlowers()
    {
        try
        {
            List<FlowerType> flowerTypeEntities = flowerTypeSessionBean.retrieveAllCategories();

//            for(Flower flowerEntity:flowerEntities)
//            {                
//                flowerEntity.getFlowerType().setFlowerEntities(null);
//            }
            
            GenericEntity<List<FlowerType>> genericEntity = new GenericEntity<List<FlowerType>>(flowerTypeEntities) {};

            return Response.status(Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    

    private FlowerTypeSessionBeanLocal lookupFlowerTypeSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FlowerTypeSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/FlowerTypeSessionBean!ejb.stateless.FlowerTypeSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

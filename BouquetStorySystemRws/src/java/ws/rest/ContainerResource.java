/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.ContainerSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import entity.Container;
import entity.Decoration;
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

/**
 * REST Web Service
 *
 * @author msipc
 */
@Path("Container")
public class ContainerResource {

    ContainerSessionBeanLocal containerSessionBean = lookupContainerSessionBeanLocal();

    
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ContainerResource
     */
    public ContainerResource() {
    }
    
    @Path("retrieveAllContainers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFlowers()
    {
        try
        {
            List<Container> containers = containerSessionBean.retrieveAllContainers();

//            for(Flower flowerEntity:flowerEntities)
//            {                
//                flowerEntity.getFlowerType().setFlowerEntities(null);
//            }
            
            GenericEntity<List<Container>> genericEntity = new GenericEntity<List<Container>>(containers) {};

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }			
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }


    private ContainerSessionBeanLocal lookupContainerSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ContainerSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/ContainerSessionBean!ejb.stateless.ContainerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

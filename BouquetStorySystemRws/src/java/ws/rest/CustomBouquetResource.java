/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.ContainerSessionBeanLocal;
import ejb.stateless.CustomBouquetSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.FlowerSessionBeanLocal;
import entity.Container;
import entity.CustomBouquet;
import entity.Decoration;
import entity.Flower;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.datamodel.CustomBouquetReq;

/**
 * REST Web Service
 *
 * @author msipc
 */
@Path("CustomBouquet")
public class CustomBouquetResource {

    DecorationSessionBeanLocal decorationSessionBean = lookupDecorationSessionBeanLocal();

    ContainerSessionBeanLocal containerSessionBean = lookupContainerSessionBeanLocal();

    FlowerSessionBeanLocal flowerSessionBean = lookupFlowerSessionBeanLocal();

    CustomBouquetSessionBeanLocal customBouquetSessionBean = lookupCustomBouquetSessionBeanLocal();

    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CustomBouquetResource
     */
    public CustomBouquetResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomBouquet(CustomBouquetReq customBouquetReq)
    {
        if(customBouquetReq != null)
        {
            try
            {
                CustomBouquet customBouquet = customBouquetReq.getCustomBouquet();
                Container container = customBouquetReq.getContainer();
                Flower[] flowers = customBouquetReq.getFlowers();
                Integer[] flowerQuantities = customBouquetReq.getFlowerQuantities();
                Decoration[] decorations = customBouquetReq.getDecorations();
                Integer[] decorationQuantities = customBouquetReq.getDecorationQuantities();
                
                
                customBouquet.setContainer(containerSessionBean.retrieveContainerByContainerId(container.getContainerId()));
                System.out.println("after container before flower loop");
//                int i = 0;
//                for(Flower flower : flowers) {
//                    customBouquet.getFlowers().add(flower);
//                    customBouquet.getFlowerQuantities().put(flower, flowerQuantities[0]);
//                    i+= 1;
//                }
                List<Flower> newFlowers = new ArrayList<>();
                customBouquet.getFlowerQuantities().clear();
                for(int i = 0; i < flowers.length; i++) {
                    Flower curFlower = flowerSessionBean.retrieveFlowerByFlowerId(flowers[i].getFlowerId());
                    System.out.println("cur flower name: " + curFlower.getName());
                    System.out.println("cur flower already flower check: " + customBouquet.getFlowers().get(0));
//                    customBouquet.getFlowers().add(curFlower);
                    newFlowers.add(curFlower);
//                    System.out.println("after putting?? flower name: " + curFlower.getName());
                    customBouquet.getFlowerQuantities().put(curFlower, flowerQuantities[i]);
                }
                customBouquet.setFlowers(newFlowers);
                
//                int j = 0;
//                for(Decoration decoration : decorations) {
//                    customBouquet.getDecorations().add(decoration);
//                    customBouquet.getDecorationQuantities().put(decoration, decorationQuantities[j]);
//                    j+=1;
//                }
                List<Decoration> newDecorations = new ArrayList<>();
                customBouquet.getDecorationQuantities().clear();
                for(int i = 0; i < decorations.length; i++) {
                    Decoration curDecoration = decorationSessionBean.retrieveDecorationByDecorationId(decorations[i].getDecorationId());
//                    customBouquet.getDecorations().add(curDecoration);
                    newDecorations.add(curDecoration);
                    customBouquet.getDecorationQuantities().put(curDecoration, decorationQuantities[i]);
                }
                customBouquet.setDecorations(newDecorations);


                CustomBouquet newCustomBouquet = customBouquetSessionBean.createNewCustomBouquet(customBouquet);
                System.out.println("newCustomBouquet id: " + newCustomBouquet.getItemId());
                return Response.status(Response.Status.OK).entity(newCustomBouquet.getItemId()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new custom bouquet request").build();
        }
    }
    
    
    private CustomBouquetSessionBeanLocal lookupCustomBouquetSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomBouquetSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/CustomBouquetSessionBean!ejb.stateless.CustomBouquetSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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

    private ContainerSessionBeanLocal lookupContainerSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ContainerSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/ContainerSessionBean!ejb.stateless.ContainerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private DecorationSessionBeanLocal lookupDecorationSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DecorationSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/DecorationSessionBean!ejb.stateless.DecorationSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

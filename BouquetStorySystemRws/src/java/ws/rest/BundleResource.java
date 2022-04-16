/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.stateless.BundleSessionBeanLocal;
import entity.Bundle;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author JORD-SSD
 */
@Path("Bundle")
public class BundleResource {

    BundleSessionBeanLocal bundleSessionBeanLocal = lookupBundleSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BundleResource
     */
    public BundleResource() {
    }

    @Path("retrieveAllBundles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllBundles() {
        try {
            List<Bundle> bundleEntities = bundleSessionBeanLocal.retrieveAllBundles();
            
            GenericEntity<List<Bundle>> genericEntity = new GenericEntity<List<Bundle>>(bundleEntities) {};
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveBundle/{bundleId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBundle(@PathParam("bundleId") Long bundleId) {
        try {
            Bundle bundleEntity = bundleSessionBeanLocal.retrieveBundleByItemId(bundleId);
            
            return Response.status(Response.Status.OK).entity(bundleEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private BundleSessionBeanLocal lookupBundleSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (BundleSessionBeanLocal) c.lookup("java:global/BouquetStorySystem/BouquetStorySystem-ejb/BundleSessionBean!ejb.stateless.BundleSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

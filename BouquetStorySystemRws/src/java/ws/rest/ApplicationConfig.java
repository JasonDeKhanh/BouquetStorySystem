/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author xqy11
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.rest.AddOnResource.class);
        resources.add(ws.rest.AddressResource.class);
        resources.add(ws.rest.BouquetResource.class);
        resources.add(ws.rest.BundleResource.class);
        resources.add(ws.rest.ContainerResource.class);
        resources.add(ws.rest.CorsFilter.class);
        resources.add(ws.rest.CreditCardResource.class);
        resources.add(ws.rest.CustomBouquetResource.class);
        resources.add(ws.rest.CustomerResource.class);
        resources.add(ws.rest.DecorationResource.class);
        resources.add(ws.rest.FlowerResource.class);
        resources.add(ws.rest.FlowerTypeResource.class);
        resources.add(ws.rest.GiftCardTypeResource.class);
        resources.add(ws.rest.ItemResource.class);
        resources.add(ws.rest.PremadeBouquetResource.class);
        resources.add(ws.rest.SaleTransactionResource.class);
    }
    
}

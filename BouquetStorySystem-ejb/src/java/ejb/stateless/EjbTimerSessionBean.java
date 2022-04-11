/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import entity.RegisteredGuest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author xqy11
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @EJB(name = "EmailSessionBeanLocal")
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB(name = "SaleTransactionSessionBeanLocal")
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;
    
    
    @Schedule(hour = "0", minute = "0", second = "0", info = "collectionOrDeliveryReminderTimer")    
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
    public void collectionOrDeliveryReminderTimer()
    {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.productEntityReorderQuantityCheckTimer(): Timeout at " + timeStamp);
        
        Customer newCustomer = new RegisteredGuest("Jason", "PooPoo", "xqy1115@gmail.com","password");
        
        Future<Boolean> asyncResult;
        try {
            asyncResult = emailSessionBeanLocal.emailCollectionOrDeliverTimerNotificationAsync(newCustomer, "xqy1115@gmail.com");
            
            Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    if(asyncResult.get())
                    {
                        System.out.println("[SERVER] Checkout notification email actually sent successfully!\n");
                    }
                    else
                    {
                        System.out.println("[SERVER] Checkout notification email was NOT actually sent!\n");
                    }
                }
                catch(ExecutionException | InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        };

        thread.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(EjbTimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @Schedule(hour = "*", minute = "*", second = "*/5", info = "productEntityReorderQuantityCheckTimer")    
//    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
//    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
//    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
//    public void productEntityReorderQuantityCheckTimer()
//    {
//        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//        System.out.println("********** EjbTimerSessionBean.productEntityReorderQuantityCheckTimer(): Timeout at " + timeStamp);
//        
//        List<ProductEntity> productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
//        
//        for(ProductEntity productEntity:productEntities)
//        {
//            if(productEntity.getQuantityOnHand().compareTo(productEntity.getReorderQuantity()) <= 0)
//            {
//                System.out.println("********** Product " + productEntity.getSkuCode() + " requires reordering: QOH = " + productEntity.getQuantityOnHand() + "; RQ = " + productEntity.getReorderQuantity());
//            }
//        }
//    }

    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "9-17", dayOfMonth = "*", year = "*", minute = "*", second = "0")
    @Override
    public void myTimer() {
        System.out.println("Timer event: " + new Date());
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

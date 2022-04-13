/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.EmailSessionBeanLocal;
import entity.Customer;
import entity.RegisteredGuest;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author xqy11
 */
@Named(value = "emailManagedBean")
@ViewScoped
public class EmailManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "EmailSessionBeanLocal")
    private EmailSessionBeanLocal emailSessionBeanLocal;

    private Customer newCustomer;
    private String toEmailAddress;
    
    public EmailManagedBean() {
         newCustomer = new RegisteredGuest("Jason", "PoPo", "xqy1115@gmail.com","password");
    }
    
    public void sendEmail(ActionEvent event) {

        Future<Boolean> asyncResult;
        try {
            asyncResult = emailSessionBeanLocal.emailRegisterNotificationAsync(newCustomer, toEmailAddress);
            
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
            Logger.getLogger(EmailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
        
        
        
        
//        System.out.println(newCustomer.getFirstName());
//        Connection connection = null;
//        Session session = null;
//        try 
//        {
//            connection = queueCheckoutNotificationFactory.createConnection();
//            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            
//            MapMessage mapMessage = session.createMapMessage();
//            mapMessage.setString("toEmailAddress", toEmailAddress);            
//            mapMessage.setLong("customerId", 1);
//            MessageProducer messageProducer = session.createProducer(queueCheckoutNotification);
//            messageProducer.send(mapMessage);
//        } catch (JMSException ex){
//            System.out.println(ex);
//        }
//        finally
//        {
//            if (session != null) 
//            {
//                try 
//                {
//                    session.close();
//                } 
//                catch (JMSException ex) 
//                {
//                    ex.printStackTrace();
//                }
//            }
//            
//            if (connection != null) 
//            {
//                try {
//                    connection.close();
//                } catch (JMSException ex) {
//                    Logger.getLogger(EmailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }                            

        
    }

    /**
     * @return the newCustomer
     */
    public Customer getNewCustomer() {
        return newCustomer;
    }

    /**
     * @param newCustomer the newCustomer to set
     */
    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

    /**
     * @return the toEmailAddress
     */
    public String getToEmailAddress() {
        return toEmailAddress;
    }

    /**
     * @param toEmailAddress the toEmailAddress to set
     */
    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }
                                            
    
}

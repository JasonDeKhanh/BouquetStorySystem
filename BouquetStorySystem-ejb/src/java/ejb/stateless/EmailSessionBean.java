/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import util.email.EmailManager;

/**
 *
 * @author xqy11
 */
@Stateless
public class EmailSessionBean implements EmailSessionBeanLocal {

    private final String FROM_EMAIL_ADDRESS = "BouquetStoryIS3106 <BouquetStoryIS3106@gmail.com>";
    private final String GMAIL_USERNAME = "BouquetStoryIS3106@gmail.com";
    private final String GMAIL_PASSWORD = "password123#";        
    
    @Override
    public Boolean emailRegisterNotificationSync(Customer customerEntity, String toEmailAddress)
    {
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailRegisterNotification(customerEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
        
        return result;
    } 
    
    @Asynchronous
    @Override
    public Future<Boolean> emailRegisterNotificationAsync(Customer customerEntity, String toEmailAddress) throws InterruptedException
    {        
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailRegisterNotification(customerEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
        
        return new AsyncResult<>(result);
    }
    
//    /emailCollectionOrDeliverTimerNotification
    @Asynchronous
    @Override
    public Future<Boolean> emailCollectionOrDeliverTimerNotificationAsync(Customer customerEntity, String toEmailAddress) throws InterruptedException
    {        
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCollectionOrDeliverTimerNotification(customerEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
        
        return new AsyncResult<>(result);
    }
    
//    
//    @Override
//    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress)
//    {
//        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
//        Boolean result = emailManager.emailCheckoutNotification(saleTransactionEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
//        
//        return result;
//    } 
//    
//    
//    
//    @Asynchronous
//    @Override
//    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress) throws InterruptedException
//    {        
//        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
//        Boolean result = emailManager.emailCheckoutNotification(saleTransactionEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
//        
//        return new AsyncResult<>(result);
//    }
}

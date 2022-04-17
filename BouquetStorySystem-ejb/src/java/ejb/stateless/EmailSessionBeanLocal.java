/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import entity.SaleTransaction;
import java.util.concurrent.Future;
import javax.ejb.Local;

/**
 *
 * @author xqy11
 */
@Local
public interface EmailSessionBeanLocal {

    public Boolean emailRegisterNotificationSync(Customer customerEntity, String toEmailAddress);

    public Future<Boolean> emailRegisterNotificationAsync(Customer customerEntity, String toEmailAddress) throws InterruptedException;

    public Future<Boolean> emailCollectionOrDeliverTimerNotificationAsync(Customer customerEntity, String toEmailAddress) throws InterruptedException;

    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransaction saleTransactionEntity, String toEmailAddress) throws InterruptedException;
    
}

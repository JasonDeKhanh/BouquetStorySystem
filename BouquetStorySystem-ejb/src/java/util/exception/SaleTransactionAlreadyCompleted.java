/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author matt_
 */
public class SaleTransactionAlreadyCompleted extends Exception{

    /**
     * Creates a new instance of <code>SaleTransactionAlreadyCompleted</code>
     * without detail message.
     */
    public SaleTransactionAlreadyCompleted() {
    }

    /**
     * Constructs an instance of <code>SaleTransactionAlreadyCompleted</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SaleTransactionAlreadyCompleted(String msg) {
        super(msg);
    }
}

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
public class InsufficientQuantityException extends Exception{

    /**
     * Creates a new instance of <code>InsufficientQuantityException</code>
     * without detail message.
     */
    public InsufficientQuantityException() {
    }

    /**
     * Constructs an instance of <code>InsufficientQuantityException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientQuantityException(String msg) {
        super(msg);
    }
}

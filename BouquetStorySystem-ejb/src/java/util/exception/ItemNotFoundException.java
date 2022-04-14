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
public class ItemNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>ItemNotFoundException</code> without
     * detail message.
     */
    public ItemNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ItemNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ItemNotFoundException(String msg) {
        super(msg);
    }
}

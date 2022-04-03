/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author xqy11
 */
public class AddressNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>AdressNotFoundException</code> without
     * detail message.
     */
    public AddressNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AdressNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AddressNotFoundException(String msg) {
        super(msg);
    }
}

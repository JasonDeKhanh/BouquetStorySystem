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
public class DecorationInsufficientQuantityOnHandException extends Exception{

    /**
     * Creates a new instance of
     * <code>DecorationInsufficientQuantityOnHandException</code> without detail
     * message.
     */
    public DecorationInsufficientQuantityOnHandException() {
    }

    /**
     * Constructs an instance of
     * <code>DecorationInsufficientQuantityOnHandException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DecorationInsufficientQuantityOnHandException(String msg) {
        super(msg);
    }
}

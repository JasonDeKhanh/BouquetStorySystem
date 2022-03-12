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
public class FlowerInsufficientQuantityOnHandException extends Exception{

    /**
     * Creates a new instance of
     * <code>FlowerInsufficientQuantityOnHandException</code> without detail
     * message.
     */
    public FlowerInsufficientQuantityOnHandException() {
    }

    /**
     * Constructs an instance of
     * <code>FlowerInsufficientQuantityOnHandException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FlowerInsufficientQuantityOnHandException(String msg) {
        super(msg);
    }
}

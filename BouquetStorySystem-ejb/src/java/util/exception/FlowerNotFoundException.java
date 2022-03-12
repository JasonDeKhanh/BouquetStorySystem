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
public class FlowerNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>FlowerNotFound</code> without detail
     * message.
     */
    public FlowerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlowerNotFound</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FlowerNotFoundException(String msg) {
        super(msg);
    }
}

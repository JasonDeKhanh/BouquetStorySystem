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
public class UpdateFlowerException extends Exception{

    /**
     * Creates a new instance of <code>UpdateFlowerException</code> without
     * detail message.
     */
    public UpdateFlowerException() {
    }

    /**
     * Constructs an instance of <code>UpdateFlowerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFlowerException(String msg) {
        super(msg);
    }
}

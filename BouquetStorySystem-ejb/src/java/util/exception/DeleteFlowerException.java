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
public class DeleteFlowerException extends Exception{

    /**
     * Creates a new instance of <code>DeleteFlowerException</code> without
     * detail message.
     */
    public DeleteFlowerException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlowerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlowerException(String msg) {
        super(msg);
    }
}

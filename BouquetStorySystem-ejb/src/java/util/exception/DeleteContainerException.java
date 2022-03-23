/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author msipc
 */
public class DeleteContainerException extends Exception {

    /**
     * Creates a new instance of <code>DeleteContainerException</code> without
     * detail message.
     */
    public DeleteContainerException() {
    }

    /**
     * Constructs an instance of <code>DeleteContainerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteContainerException(String msg) {
        super(msg);
    }
}

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
public class DeleteContainerTypeException extends Exception {

    /**
     * Creates a new instance of <code>DeleteContainerTypeException</code>
     * without detail message.
     */
    public DeleteContainerTypeException() {
    }

    /**
     * Constructs an instance of <code>DeleteContainerTypeException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteContainerTypeException(String msg) {
        super(msg);
    }
}

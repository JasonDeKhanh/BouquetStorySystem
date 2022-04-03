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
public class UpdateContainerTypeException extends Exception {

    /**
     * Creates a new instance of <code>UpdateContainerTypeException</code>
     * without detail message.
     */
    public UpdateContainerTypeException() {
    }

    /**
     * Constructs an instance of <code>UpdateContainerTypeException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateContainerTypeException(String msg) {
        super(msg);
    }
}

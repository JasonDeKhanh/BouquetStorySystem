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
public class CreateNewContainerTypeException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewContainerTypeException</code>
     * without detail message.
     */
    public CreateNewContainerTypeException() {
    }

    /**
     * Constructs an instance of <code>CreateNewContainerTypeException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewContainerTypeException(String msg) {
        super(msg);
    }
}

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
public class CreateNewContainerException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewContainerException</code>
     * without detail message.
     */
    public CreateNewContainerException() {
    }

    /**
     * Constructs an instance of <code>CreateNewContainerException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewContainerException(String msg) {
        super(msg);
    }
}

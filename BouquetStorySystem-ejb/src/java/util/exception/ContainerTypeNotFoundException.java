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
public class ContainerTypeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ContainerTypeNotFoundException</code>
     * without detail message.
     */
    public ContainerTypeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ContainerTypeNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ContainerTypeNotFoundException(String msg) {
        super(msg);
    }
}

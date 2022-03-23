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
public class ContainerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ContainerNotFoundException</code> without
     * detail message.
     */
    public ContainerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ContainerNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ContainerNotFoundException(String msg) {
        super(msg);
    }
}

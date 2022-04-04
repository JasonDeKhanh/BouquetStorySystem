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
public class UpdateContainerException extends Exception{

    /**
     * Creates a new instance of <code>UpdateContainerException</code> without
     * detail message.
     */
    public UpdateContainerException() {
    }

    /**
     * Constructs an instance of <code>UpdateContainerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateContainerException(String msg) {
        super(msg);
    }
}

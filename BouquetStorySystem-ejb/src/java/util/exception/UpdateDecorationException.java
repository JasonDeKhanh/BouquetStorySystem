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
public class UpdateDecorationException extends Exception{

    /**
     * Creates a new instance of <code>UpdateDecorationException</code> without
     * detail message.
     */
    public UpdateDecorationException() {
    }

    /**
     * Constructs an instance of <code>UpdateDecorationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDecorationException(String msg) {
        super(msg);
    }
}

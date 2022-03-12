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
public class DeleteDecorationException extends Exception{

    /**
     * Creates a new instance of <code>DeleteDecorationException</code> without
     * detail message.
     */
    public DeleteDecorationException() {
    }

    /**
     * Constructs an instance of <code>DeleteDecorationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteDecorationException(String msg) {
        super(msg);
    }
}

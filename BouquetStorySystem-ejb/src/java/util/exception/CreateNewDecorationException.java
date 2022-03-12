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
public class CreateNewDecorationException extends Exception{

    /**
     * Creates a new instance of <code>CreateNewDecorationException</code>
     * without detail message.
     */
    public CreateNewDecorationException() {
    }

    /**
     * Constructs an instance of <code>CreateNewDecorationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewDecorationException(String msg) {
        super(msg);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author matt_
 */
public class MarkIsCompletedException extends Exception{

    /**
     * Creates a new instance of <code>MarkIsCompletedException</code> without
     * detail message.
     */
    public MarkIsCompletedException() {
    }

    /**
     * Constructs an instance of <code>MarkIsCompletedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MarkIsCompletedException(String msg) {
        super(msg);
    }
}

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
public class DeleteFlowerTypeException extends Exception{

    /**
     * Creates a new instance of <code>DeleteFlowerTypeException</code> without
     * detail message.
     */
    public DeleteFlowerTypeException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlowerTypeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlowerTypeException(String msg) {
        super(msg);
    }
}

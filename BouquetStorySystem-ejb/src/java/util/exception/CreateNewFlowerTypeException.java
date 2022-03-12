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
public class CreateNewFlowerTypeException extends Exception{

    /**
     * Creates a new instance of <code>CreateNewFlowerTypeException</code>
     * without detail message.
     */
    public CreateNewFlowerTypeException() {
    }

    /**
     * Constructs an instance of <code>CreateNewFlowerTypeException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewFlowerTypeException(String msg) {
        super(msg);
    }
}

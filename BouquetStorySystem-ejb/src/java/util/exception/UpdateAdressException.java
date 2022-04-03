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
public class UpdateAdressException extends Exception{

    /**
     * Creates a new instance of <code>UpdateAdressException</code> without
     * detail message.
     */
    public UpdateAdressException() {
    }

    /**
     * Constructs an instance of <code>UpdateAdressException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateAdressException(String msg) {
        super(msg);
    }
}

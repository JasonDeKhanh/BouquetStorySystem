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
public class SendEmailException extends Exception{

    /**
     * Creates a new instance of <code>SendEmailException</code> without detail
     * message.
     */
    public SendEmailException() {
    }

    /**
     * Constructs an instance of <code>SendEmailException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SendEmailException(String msg) {
        super(msg);
    }
}

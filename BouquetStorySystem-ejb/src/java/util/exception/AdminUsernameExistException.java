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
public class AdminUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>AdminUsernameExistException</code>
     * without detail message.
     */
    public AdminUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>AdminUsernameExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AdminUsernameExistException(String msg) {
        super(msg);
    }
}

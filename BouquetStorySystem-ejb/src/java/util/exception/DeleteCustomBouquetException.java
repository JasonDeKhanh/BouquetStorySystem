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
public class DeleteCustomBouquetException extends Exception {

    /**
     * Creates a new instance of <code>DeleteCustomBouquetException</code>
     * without detail message.
     */
    public DeleteCustomBouquetException() {
    }

    /**
     * Constructs an instance of <code>DeleteCustomBouquetException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteCustomBouquetException(String msg) {
        super(msg);
    }
}

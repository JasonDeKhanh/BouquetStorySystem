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
public class UpdatePremadeBouquetException extends Exception {

    /**
     * Creates a new instance of <code>UpdatePremadeBouquetException</code>
     * without detail message.
     */
    public UpdatePremadeBouquetException() {
    }

    /**
     * Constructs an instance of <code>UpdatePremadeBouquetException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdatePremadeBouquetException(String msg) {
        super(msg);
    }
}

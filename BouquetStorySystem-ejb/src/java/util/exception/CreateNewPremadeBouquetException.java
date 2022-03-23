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
public class CreateNewPremadeBouquetException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewPremadeBouquetException</code>
     * without detail message.
     */
    public CreateNewPremadeBouquetException() {
    }

    /**
     * Constructs an instance of <code>CreateNewPremadeBouquetException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewPremadeBouquetException(String msg) {
        super(msg);
    }
}

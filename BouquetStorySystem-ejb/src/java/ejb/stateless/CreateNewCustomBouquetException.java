/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

/**
 *
 * @author msipc
 */
public class CreateNewCustomBouquetException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCustomBouquetException</code>
     * without detail message.
     */
    public CreateNewCustomBouquetException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCustomBouquetException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCustomBouquetException(String msg) {
        super(msg);
    }
}

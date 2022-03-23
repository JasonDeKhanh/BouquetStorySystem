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
public class CustomBouquetNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CustomBouquetNotFoundException</code>
     * without detail message.
     */
    public CustomBouquetNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CustomBouquetNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomBouquetNotFoundException(String msg) {
        super(msg);
    }
}

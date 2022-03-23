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
public class PremadeBouquetNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PremadeBouquetNotFoundException</code>
     * without detail message.
     */
    public PremadeBouquetNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PremadeBouquetNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PremadeBouquetNotFoundException(String msg) {
        super(msg);
    }
}

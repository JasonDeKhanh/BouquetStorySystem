/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author JORD-SSD
 */
public class GiftCardTypeNotFoundException extends Exception {

    public GiftCardTypeNotFoundException() {
    }

    public GiftCardTypeNotFoundException(String msg) {
        super(msg);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import javax.ejb.Local;

/**
 *
 * @author xqy11
 */
@Local
public interface EjbTimerSessionBeanLocal {
    
    public void myTimer();
}

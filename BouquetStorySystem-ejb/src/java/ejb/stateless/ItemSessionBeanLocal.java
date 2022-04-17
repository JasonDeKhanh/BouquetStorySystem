/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Item;
import java.util.List;
import javax.ejb.Local;
import util.exception.ItemNotFoundException;

/**
 *
 * @author matt_
 */
@Local
public interface ItemSessionBeanLocal {

    public Item retrieveItemByItemId(Long itemId) throws ItemNotFoundException;
    public List<Item> retrieveAllAdminCreatedItems();
    
}

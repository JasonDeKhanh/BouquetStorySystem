/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Item;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ItemNotFoundException;

/**
 *
 * @author matt_
 */
@Stateless
public class ItemSessionBean implements ItemSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    @Override
    public Item retrieveItemByItemId(Long itemId) throws ItemNotFoundException
    {
        Item item = em.find(Item.class, itemId);
        
        if(item != null)
        {
           
            return item;
        }
        else
        {
            throw new ItemNotFoundException("Item ID " + itemId + " does not exist!");
        }               
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

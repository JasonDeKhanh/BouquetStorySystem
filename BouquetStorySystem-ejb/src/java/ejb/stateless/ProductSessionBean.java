/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Item;
import entity.Product;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ItemNotFoundException;

/**
 *
 * @author matt_
 */
@Stateless
public class ProductSessionBean implements ProductSessionBeanLocal {
    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    @Override
    public Product retrieveProductByItemId(Long itemId) throws ItemNotFoundException
    {
        Product item = (Product)em.find(Item.class, itemId);
        
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

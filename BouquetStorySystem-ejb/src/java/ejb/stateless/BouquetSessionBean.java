/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Bouquet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BouquetNotFoundException;
import util.exception.CustomBouquetNotFoundException;

/**
 *
 * @author msipc
 */
@Stateless
public class BouquetSessionBean implements BouquetSessionBeanLocal {

    @PersistenceContext(unitName = "BouquetStorySystem-ejbPU")
    private EntityManager em;

    public BouquetSessionBean() {
    }


    public List<Bouquet> retrieveAlBouquets()
    {
        Query query = em.createQuery("SELECT b FROM Bouquet b ORDER BY b.itemId ASC");
        List<Bouquet> bouquets = query.getResultList();
        
        return bouquets;
    }
    
    
    public Bouquet retrieveCustomBouquetByItemId(Long itemId) throws BouquetNotFoundException
    {
        Bouquet bouquet = em.find(Bouquet.class, itemId);
        
        if(bouquet != null)
        {
            return bouquet;
        }
        else
        {
            throw new BouquetNotFoundException("Bouquet ID " + itemId + " does not exist!");
        }
    }
    
}

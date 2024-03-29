/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Decoration;
import entity.Flower;
import entity.PremadeBouquet;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewPremadeBouquetException;
import util.exception.DeletePremadeBouquetException;
import util.exception.InputDataValidationException;
import util.exception.PremadeBouquetNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePremadeBouquetException;

/**
 *
 * @author msipc
 */
@Local
public interface PremadeBouquetSessionBeanLocal {

    public void deletePremadeBouquet(Long itemId) throws PremadeBouquetNotFoundException, DeletePremadeBouquetException;

    public PremadeBouquet retrievePremadeBouquetByItemId(Long itemId) throws PremadeBouquetNotFoundException;

    public List<PremadeBouquet> retrieveAllPremadeBouquets();

    public PremadeBouquet createNewPremadeBouquet(PremadeBouquet premadeBouquet, Long containerId, List<Decoration> decorations, List<Flower> flowers) throws CreateNewPremadeBouquetException, UnknownPersistenceException, InputDataValidationException;

    public void updatePremadeBouquet(PremadeBouquet premadeBouquet, Long containerId, List<Decoration> decorations, List<Flower> flowers) throws PremadeBouquetNotFoundException, InputDataValidationException, UpdatePremadeBouquetException;
    
}

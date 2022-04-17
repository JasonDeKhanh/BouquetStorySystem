/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomBouquet;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomBouquetNotFoundException;
import util.exception.DeleteCustomBouquetException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author msipc
 */
@Local
public interface CustomBouquetSessionBeanLocal {

    public CustomBouquet createNewCustomBouquet(CustomBouquet newCustomBouquet) throws CreateNewCustomBouquetException, UnknownPersistenceException, InputDataValidationException;

    public List<CustomBouquet> retrieveAllCustomBouquets();

    public CustomBouquet retrieveCustomBouquetByItemId(Long itemId) throws CustomBouquetNotFoundException;

    public void updateCustomBouquet(CustomBouquet customBouquet) throws CustomBouquetNotFoundException, InputDataValidationException;

    public void deleteCustomBouquet(Long itemId) throws CustomBouquetNotFoundException, DeleteCustomBouquetException;
    
}

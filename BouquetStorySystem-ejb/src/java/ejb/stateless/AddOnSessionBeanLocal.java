/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.AddOn;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddOnNotFoundException;
import util.exception.CreateNewAddOnException;
import util.exception.DeleteAddOnException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddOnException;

/**
 *
 * @author JORD-SSD
 */
@Local
public interface AddOnSessionBeanLocal {
    public AddOn createNewAddOn(AddOn newAddOnEntity) throws CreateNewAddOnException, UnknownPersistenceException, InputDataValidationException;
    public List<AddOn> retrieveAllAddOns();
    public AddOn retrieveAddOnByAddOnId(Long itemId) throws AddOnNotFoundException;
    public void updateAddOn(AddOn addOnEntity) throws AddOnNotFoundException, UpdateAddOnException, InputDataValidationException;
    public void deleteAddOn(Long itemId) throws AddOnNotFoundException, DeleteAddOnException;
}

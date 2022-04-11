/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Bundle;
import java.util.List;
import javax.ejb.Local;
import util.exception.BundleNotFoundException;
import util.exception.CreateNewBundleException;
import util.exception.DeleteBundleException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateBundleException;

/**
 *
 * @author JORD-SSD
 */
@Local
public interface BundleSessionBeanLocal {
    public Bundle createNewBundle(Bundle newBundleEntity) throws CreateNewBundleException, UnknownPersistenceException, InputDataValidationException;
    public List<Bundle> retrieveAllBundles();
    public Bundle retrieveBundleByItemId(Long itemId) throws BundleNotFoundException;
    public void updateBundle(Bundle bundleEntity) throws BundleNotFoundException, UpdateBundleException, InputDataValidationException;
    public void deleteBundle(Long itemId) throws BundleNotFoundException, DeleteBundleException;
}

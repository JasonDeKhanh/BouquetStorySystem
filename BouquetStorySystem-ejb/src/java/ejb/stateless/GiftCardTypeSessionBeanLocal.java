/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.GiftCardType;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewGiftCardTypeException;
import util.exception.DeleteGiftCardTypeException;
import util.exception.GiftCardTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateGiftCardTypeException;

/**
 *
 * @author JORD-SSD
 */
@Local
public interface GiftCardTypeSessionBeanLocal {
    public GiftCardType createNewGiftCardType(GiftCardType newGiftCardTypeEntity) throws CreateNewGiftCardTypeException, UnknownPersistenceException, InputDataValidationException;
    public List<GiftCardType> retrieveAllGiftCardTypes();
    public GiftCardType retrieveGiftCardTypeByGiftCardTypeId(Long giftCardTypeId) throws GiftCardTypeNotFoundException;
    public void updateGiftCardType(GiftCardType giftCardTypeEntity) throws GiftCardTypeNotFoundException, UpdateGiftCardTypeException, InputDataValidationException;
    public void deleteGiftCardTypeDecoration(Long giftCardTypeId) throws GiftCardTypeNotFoundException, DeleteGiftCardTypeException;
}

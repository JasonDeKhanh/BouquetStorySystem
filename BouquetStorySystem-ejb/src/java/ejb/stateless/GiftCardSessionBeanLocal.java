/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.GiftCard;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewGiftCardException;
import util.exception.DeleteGiftCardException;
import util.exception.GiftCardNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateGiftCardException;

/**
 *
 * @author JORD-SSD
 */
@Local
public interface GiftCardSessionBeanLocal {
    public GiftCard createNewGiftCard(GiftCard newGiftCardEntity) throws CreateNewGiftCardException, UnknownPersistenceException, InputDataValidationException;
    public List<GiftCard> retrieveAllGiftCards();
    public GiftCard retrieveGiftCardByGiftCardId(Long itemId) throws GiftCardNotFoundException;
    public void updateGiftCard(GiftCard giftCardEntity) throws GiftCardNotFoundException, UpdateGiftCardException, InputDataValidationException;
    public void deleteGiftCard(Long itemId) throws GiftCardNotFoundException, DeleteGiftCardException;
}

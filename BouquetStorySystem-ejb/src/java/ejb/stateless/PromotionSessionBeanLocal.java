/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Promotion;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewPromotionException;
import util.exception.DeletePromotionException;
import util.exception.InputDataValidationException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePromotionException;

/**
 *
 * @author JORD-SSD
 */
@Local
public interface PromotionSessionBeanLocal {
    public Promotion createNewPromotion(Promotion newPromotionEntity) throws CreateNewPromotionException, UnknownPersistenceException, InputDataValidationException;
    public List<Promotion> retrieveAllPromotions();
    public Promotion retrievePromotionByPromotionId(Long promotionId) throws PromotionNotFoundException;
    public void updatePromotion(Promotion promotionEntity) throws PromotionNotFoundException, UpdatePromotionException, InputDataValidationException;
    public void deletePromotionDecoration(Long promotionId) throws PromotionNotFoundException, DeletePromotionException;
}

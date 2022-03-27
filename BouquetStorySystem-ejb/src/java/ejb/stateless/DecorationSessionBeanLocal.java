/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Decoration;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewDecorationException;
import util.exception.DecorationInsufficientQuantityOnHandException;
import util.exception.DecorationNotFoundException;
import util.exception.DeleteDecorationException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDecorationException;

/**
 *
 * @author xqy11
 */
@Local
public interface DecorationSessionBeanLocal {

    public Decoration createNewDecoration(Decoration newDecorationEntity) throws CreateNewDecorationException, UnknownPersistenceException, InputDataValidationException;

    public List<Decoration> retrieveAllDecorations();

    public Decoration retrieveDecorationByDecorationId(Long decorationId) throws DecorationNotFoundException;

    public void updateDecoration(Decoration decorarionEntity) throws DecorationNotFoundException, UpdateDecorationException, InputDataValidationException;

    public void deleteDecoration(Long decorationId) throws DecorationNotFoundException, DeleteDecorationException;

    public void debitQuantityOnHand(Long decorationId, Integer quantityToDebit) throws DecorationNotFoundException, DecorationInsufficientQuantityOnHandException;

    public void creditQuantityOnHand(Long flowerId, Integer quantityToCredit) throws DecorationNotFoundException;
    
}

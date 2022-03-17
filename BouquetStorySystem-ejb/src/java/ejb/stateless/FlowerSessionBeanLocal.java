/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Flower;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.FlowerColorEnum;
import util.enumeration.OccasionEnum;
import util.exception.CreateNewFlowerException;
import util.exception.DeleteFlowerException;
import util.exception.FlowerInsufficientQuantityOnHandException;
import util.exception.FlowerNotFoundException;
import util.exception.FlowerTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlowerException;

/**
 *
 * @author xqy11
 */
@Local
public interface FlowerSessionBeanLocal {

    public Flower createNewFlower(Flower newFlowerEntity, Long flowerTypeId) throws CreateNewFlowerException, FlowerTypeNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<Flower> retrieveAllFlowers();

    public List<Flower> searchFlowersByName(String searchString);

    public List<Flower> filterFlowersByCategory(Long fowerTypeId) throws FlowerTypeNotFoundException;

    public Flower retrieveFlowerByFlowerId(Long flowerId) throws FlowerNotFoundException;

    public void updateFlower(Flower flowerEntity, Long flowerTypeId) throws FlowerNotFoundException, FlowerTypeNotFoundException, UpdateFlowerException, InputDataValidationException;

    public void deleteFlower(Long flowerId) throws FlowerNotFoundException, DeleteFlowerException, FlowerTypeNotFoundException;

    public void debitQuantityOnHand(Long flowerId, Integer quantityToDebit) throws FlowerNotFoundException, FlowerInsufficientQuantityOnHandException, FlowerTypeNotFoundException;

    public void creditQuantityOnHand(Long flowerId, Integer quantityToCredit) throws FlowerNotFoundException, FlowerTypeNotFoundException;

    
}

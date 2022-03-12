/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.FlowerType;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewFlowerTypeException;
import util.exception.DeleteFlowerTypeException;
import util.exception.FlowerTypeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFlowerTypeException;

/**
 *
 * @author xqy11
 */
@Local
public interface FlowerTypeSessionBeanLocal {

    public FlowerType createNewFlowerType(FlowerType newFlowerType) throws InputDataValidationException, CreateNewFlowerTypeException;

    public List<FlowerType> retrieveAllCategories();

    public FlowerType retrieveFlowerTypeByFlowerTypeId(Long flowerTypeId) throws FlowerTypeNotFoundException;

    public void updateCategory(FlowerType flowerType) throws InputDataValidationException, FlowerTypeNotFoundException, UpdateFlowerTypeException;

    public void deleteFlowerType(Long flowerTypeId) throws FlowerTypeNotFoundException, DeleteFlowerTypeException;
    
}

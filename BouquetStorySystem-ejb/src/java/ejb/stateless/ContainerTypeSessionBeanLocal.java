/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ContainerType;
import java.util.List;
import javax.ejb.Local;
import util.exception.ContainerTypeNotFoundException;
import util.exception.CreateNewContainerTypeException;
import util.exception.DeleteContainerTypeException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateContainerTypeException;

/**
 *
 * @author msipc
 */
@Local
public interface ContainerTypeSessionBeanLocal {

    public ContainerType createNewContainerType(ContainerType newContainerType) throws CreateNewContainerTypeException, UnknownPersistenceException, InputDataValidationException;

    public List<ContainerType> retrieveAllContainerTypes();

    public ContainerType retrieveContainerTypeByContainerTypeId(Long containerTypeId) throws ContainerTypeNotFoundException;

    public void updateContainerType(ContainerType containerTypeEntity) throws ContainerTypeNotFoundException, InputDataValidationException, UpdateContainerTypeException;

    public void deleteContainerType(Long containerTypeId) throws ContainerTypeNotFoundException, DeleteContainerTypeException;
    
}

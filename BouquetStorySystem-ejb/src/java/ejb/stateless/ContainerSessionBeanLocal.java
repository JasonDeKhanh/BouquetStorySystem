/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Container;
import java.util.List;
import javax.ejb.Local;
import util.exception.ContainerNotFoundException;
import util.exception.ContainerTypeNotFoundException;
import util.exception.CreateNewContainerException;
import util.exception.DeleteContainerException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateContainerException;

/**
 *
 * @author msipc
 */
@Local
public interface ContainerSessionBeanLocal {

    public Container createNewContainer(Container newContainerEntity, Long containerTypeId) throws CreateNewContainerException, UnknownPersistenceException, InputDataValidationException, ContainerTypeNotFoundException;

    public List<Container> retrieveAllContainers();

    public Container retrieveContainerByContainerId(Long containerId) throws ContainerNotFoundException;

    public void deleteContainer(Long containerId) throws ContainerNotFoundException, DeleteContainerException;

    public void updateContainer(Container containerEntity, Long containerTypeId) throws ContainerNotFoundException, InputDataValidationException, ContainerTypeNotFoundException, UpdateContainerException;
    
}

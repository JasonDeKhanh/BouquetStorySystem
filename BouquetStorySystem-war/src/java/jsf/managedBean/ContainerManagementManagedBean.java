/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.ContainerSessionBeanLocal;
import entity.Container;
import entity.ContainerType;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author msipc
 */
@Named(value = "containerManagementManagedBean")
@ViewScoped
public class ContainerManagementManagedBean implements Serializable {

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    private Container newContainerEntity;
    private List<Container> containerEntities;
    private List<Container> filteredContainerEntities;
    
    private Long newContainerEntityTypeId;
    private List<ContainerType> containerTypeEntities;
    
    private Container selectedContainerEntityToUpdate;
    private Long containerTypeIdToUpdate;
    

    public ContainerManagementManagedBean() {
        newContainerEntity = new Container();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
    }
}

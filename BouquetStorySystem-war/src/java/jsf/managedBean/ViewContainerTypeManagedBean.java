/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.ContainerType;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author msipc
 */
@Named(value = "viewContainerTypeManagedBean")
@ViewScoped
public class ViewContainerTypeManagedBean implements Serializable {

    private ContainerType containerTypeToView;

    public ViewContainerTypeManagedBean() 
    {
        containerTypeToView = new ContainerType();
    }

    @PostConstruct
    public void postConstruct()
    {
    }

    public ContainerType getContainerTypeToView() {
        return containerTypeToView;
    }

    public void setContainerTypeToView(ContainerType containerTypeToView) {
        this.containerTypeToView = containerTypeToView;
    }
    
}

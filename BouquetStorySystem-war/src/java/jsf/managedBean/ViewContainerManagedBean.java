/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.Container;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author msipc
 */
@Named(value = "viewContainerManagedBean")
@ViewScoped
public class ViewContainerManagedBean implements Serializable {

    private Container containerEntityToView;
    
    public ViewContainerManagedBean() {
    }

    public Container getContainerEntityToView() {
        return containerEntityToView;
    }

    public void setContainerEntityToView(Container containerEntityToView) {
        this.containerEntityToView = containerEntityToView;
    }
    
}

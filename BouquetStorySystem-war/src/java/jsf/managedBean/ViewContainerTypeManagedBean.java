/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author msipc
 */
@Named(value = "viewContainerTypeManagedBean")
@ViewScoped
public class ViewContainerTypeManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewContainerTypeManagedBean
     */
    public ViewContainerTypeManagedBean() {
    }
    
}

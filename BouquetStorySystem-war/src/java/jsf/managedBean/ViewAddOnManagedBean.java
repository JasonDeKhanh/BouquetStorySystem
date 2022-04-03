/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.AddOn;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "viewAddOnManagedBean")
@ViewScoped
public class ViewAddOnManagedBean implements Serializable {

    private AddOn addOnEntityToView;
    
    public ViewAddOnManagedBean() {
        addOnEntityToView = new AddOn();
    }
    
    @PostConstruct
    public void postConstruct()
    {
    }

    public AddOn getAddOnEntityToView() {
        return addOnEntityToView;
    }

    public void setAddOnEntityToView(AddOn addOnEntityToView) {
        this.addOnEntityToView = addOnEntityToView;
    }
    
}

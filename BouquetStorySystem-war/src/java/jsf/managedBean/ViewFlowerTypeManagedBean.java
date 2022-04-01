/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.FlowerType;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author xqy11
 */
@Named(value = "viewFlowerTypeManagedBean")
@ViewScoped
public class ViewFlowerTypeManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewFlowerTypeManagedBean
     */
    private FlowerType flowerTypeEntityToView;
    public ViewFlowerTypeManagedBean() {
        flowerTypeEntityToView = new FlowerType();
    }

    /**
     * @return the flowerTypeEntityToView
     */
    public FlowerType getFlowerTypeEntityToView() {
        return flowerTypeEntityToView;
    }

    /**
     * @param flowerTypeEntityToView the flowerTypeEntityToView to set
     */
    public void setFlowerTypeEntityToView(FlowerType flowerTypeEntityToView) {
        this.flowerTypeEntityToView = flowerTypeEntityToView;
    }
    
}

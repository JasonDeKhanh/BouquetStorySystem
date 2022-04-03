/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.Flower;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author xqy11
 */
@Named(value = "viewFlowerManagedBean")
@ViewScoped
public class ViewFlowerManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewFlowerManagedBean
     */
    private Flower flowerEntityToView;
    
    public ViewFlowerManagedBean() {
        flowerEntityToView = new Flower();
    }

    /**
     * @return the flowerEntityToView
     */
    public Flower getFlowerEntityToView() {
        return flowerEntityToView;
    }

    /**
     * @param flowerEntityToView the flowerEntityToView to set
     */
    public void setFlowerEntityToView(Flower flowerEntityToView) {
        this.flowerEntityToView = flowerEntityToView;
    }
    
}

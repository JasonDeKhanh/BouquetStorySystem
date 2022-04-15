/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.Bundle;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "viewBundleCCManagedBean")
@ViewScoped
public class ViewBundleCCManagedBean implements Serializable {

    private Bundle bundleEntityToView;
    
    public ViewBundleCCManagedBean() {
    }

    public Bundle getBundleEntityToView() {
        return bundleEntityToView;
    }

    public void setBundleEntityToView(Bundle bundleEntityToView) {
        this.bundleEntityToView = bundleEntityToView;
    }
    
    
}

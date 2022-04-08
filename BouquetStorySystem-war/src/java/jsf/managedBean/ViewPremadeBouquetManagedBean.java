/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.PremadeBouquet;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author msipc
 */
@Named(value = "viewPremadeBouquetManagedBean")
@ViewScoped
public class ViewPremadeBouquetManagedBean implements Serializable {

    private PremadeBouquet premadeBouquetEntityToView;
    
    public ViewPremadeBouquetManagedBean() {
        premadeBouquetEntityToView = new PremadeBouquet();
    }

    public PremadeBouquet getPremadeBouquetEntityToView() {
        return premadeBouquetEntityToView;
    }

    public void setPremadeBouquetEntityToView(PremadeBouquet premadeBouquetEntityToView) {
        this.premadeBouquetEntityToView = premadeBouquetEntityToView;
    }
    
}

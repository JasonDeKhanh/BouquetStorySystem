package jsf.managedBean;

import entity.AddOn;
import entity.Bundle;
import entity.PremadeBouquet;
import entity.Product;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "viewBundleManagedBean")
@ViewScoped
public class ViewBundleManagedBean implements Serializable {

    private Bundle bundleEntityToView;
    private Product productEntity;
    private AddOn currAddOn;
    private PremadeBouquet currPremadeBouquet;
    
    public ViewBundleManagedBean() {
        bundleEntityToView = new Bundle();
        currAddOn = new AddOn();
        currPremadeBouquet = new PremadeBouquet();
    }
    
    @PostConstruct
    public void postConstruct()
    {
    }
    
    public void convertProductToAddOn(ActionEvent event) {
        currAddOn = (AddOn) productEntity;
    }
    
    public void convertProductToPremadeBouquet(ActionEvent event) {
        currPremadeBouquet = (PremadeBouquet) productEntity;
    }

    public Bundle getBundleEntityToView() {
        return bundleEntityToView;
    }

    public void setBundleEntityToView(Bundle bundleEntityToView) {
        this.bundleEntityToView = bundleEntityToView;
    }

    public Product getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(Product productEntity) {
        this.productEntity = productEntity;
    }

    public AddOn getCurrAddOn() {
        return currAddOn;
    }

    public void setCurrAddOn(AddOn currAddOn) {
        this.currAddOn = currAddOn;
    }

    public PremadeBouquet getCurrPremadeBouquet() {
        return currPremadeBouquet;
    }

    public void setCurrPremadeBouquet(PremadeBouquet currPremadeBouquet) {
        this.currPremadeBouquet = currPremadeBouquet;
    }
}

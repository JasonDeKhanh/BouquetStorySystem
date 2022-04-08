package jsf.managedBean;

import entity.Bundle;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "viewBundleManagedBean")
@ViewScoped
public class ViewBundleManagedBean implements Serializable {

    private Bundle bundleEntityToView;
    
    public ViewBundleManagedBean() {
        bundleEntityToView = new Bundle();
    }
    
    @PostConstruct
    public void postConstruct()
    {
    }

    public Bundle getBundleEntityToView() {
        return bundleEntityToView;
    }

    public void setBundleEntityToView(Bundle bundleEntityToView) {
        this.bundleEntityToView = bundleEntityToView;
    }
}

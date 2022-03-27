package jsf.managedBean;

import entity.Decoration;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;



@Named(value = "viewDecorationtManagedBean")
@ViewScoped

public class ViewDecorationtManagedBean implements Serializable
{
    private Decoration decorationEntityToView;
    
    
    
    public ViewDecorationtManagedBean()
    {
        decorationEntityToView = new Decoration();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {        
    }


    /**
     * @return the decorationEntityToView
     */
    public Decoration getDecorationEntityToView() {
        return decorationEntityToView;
    }

    /**
     * @param decorationEntityToView the decorationEntityToView to set
     */
    public void setDecorationEntityToView(Decoration decorationEntityToView) {
        this.decorationEntityToView = decorationEntityToView;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.CustomBouquetSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.FlowerSessionBeanLocal;
import entity.CustomBouquet;
import entity.Decoration;
import entity.Flower;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.DecorationNotFoundException;
import util.exception.FlowerNotFoundException;

/**
 *
 * @author matt_
 */
@Named(value = "viewCustomBouquetManagedBean")
@ViewScoped
public class ViewCustomBouquetManagedBean implements Serializable {

    @EJB(name = "CustomBouquetSessionBeanLocal")
    private CustomBouquetSessionBeanLocal customBouquetSessionBeanLocal;
    
    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;
        
    private Decoration curDecoration;
    // used to help View Details Flower
    private Flower curFlower;
    private CustomBouquet customBouquetToView;
    /**
     * Creates a new instance of ViewCustomBouquetManagedBean
     */
    public ViewCustomBouquetManagedBean() {
        customBouquetToView = new CustomBouquet();
    }
    
    public Decoration getUpdatedDecoration()
    {
        try 
        {
            return decorationSessionBeanLocal.retrieveDecorationByDecorationId(curDecoration.getDecorationId());
        } 
        catch (DecorationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Decoration not found", null));
            return null;
        }
    }
    
    public Flower getUpdatedFlower()
    {
        try 
        {
            return flowerSessionBeanLocal.retrieveFlowerByFlowerId(curFlower.getFlowerId());
        } 
        catch (FlowerNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Flower not found", null));
            return null;
        }
    }

    /**
     * @return the curDecoration
     */
    public Decoration getCurDecoration() {
        return curDecoration;
    }

    /**
     * @param curDecoration the curDecoration to set
     */
    public void setCurDecoration(Decoration curDecoration) {
        this.curDecoration = curDecoration;
    }

    /**
     * @return the curFlower
     */
    public Flower getCurFlower() {
        return curFlower;
    }

    /**
     * @param curFlower the curFlower to set
     */
    public void setCurFlower(Flower curFlower) {
        this.curFlower = curFlower;
    }

    /**
     * @return the customBouquetToView
     */
    public CustomBouquet getCustomBouquetToView() {
        return customBouquetToView;
    }

    /**
     * @param customBouquetToView the customBouquetToView to set
     */
    public void setCustomBouquetToView(CustomBouquet customBouquetToView) {
        this.customBouquetToView = customBouquetToView;
    }
    
}

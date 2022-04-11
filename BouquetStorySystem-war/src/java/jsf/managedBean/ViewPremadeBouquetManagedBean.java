/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.FlowerSessionBeanLocal;
import ejb.stateless.PremadeBouquetSessionBeanLocal;
import entity.Decoration;
import entity.Flower;
import entity.PremadeBouquet;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.DecorationNotFoundException;
import util.exception.FlowerNotFoundException;
import util.exception.PremadeBouquetNotFoundException;

/**
 *
 * @author msipc
 */
@Named(value = "viewPremadeBouquetManagedBean")
@ViewScoped
public class ViewPremadeBouquetManagedBean implements Serializable {

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;

    @EJB(name = "PremadeBouquetSessionBeanLocal")
    private PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal;

    // used to help View Details Decoration
    private Decoration curDecoration;
    // used to help View Details Flower
    private Flower curFlower;
    
    private Long premadeBouquetId;
    private PremadeBouquet premadeBouquet;
    
    public ViewPremadeBouquetManagedBean() {
        premadeBouquet = new PremadeBouquet();
    }

    @PostConstruct
    public void postConstruct()
    {
        try 
        {
            System.out.println("Inside try block post construct");
            premadeBouquetId = Long.valueOf(
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("premadeBouquetId"));
            premadeBouquet = premadeBouquetSessionBeanLocal.retrievePremadeBouquetByItemId(premadeBouquetId);
        } 
        catch (PremadeBouquetNotFoundException ex) 
        {
            System.out.println("inside catch error block postconstruct");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Premade Bouquet ID not provided", null));
        }
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
    
    public PremadeBouquet getPremadeBouquet() {
        return premadeBouquet;
    }

    public void setPremadeBouquet(PremadeBouquet premadeBouquet) {
        this.premadeBouquet = premadeBouquet;
    }

    public Long getPremadeBouquetId() {
        return premadeBouquetId;
    }

    public void setPremadeBouquetId(Long premadeBouquetId) {
        this.premadeBouquetId = premadeBouquetId;
    }

    public Decoration getCurDecoration() {
        return curDecoration;
    }

    public void setCurDecoration(Decoration curDecoration) {
        this.curDecoration = curDecoration;
    }

    public Flower getCurFlower() {
        return curFlower;
    }

    public void setCurFlower(Flower curFlower) {
        this.curFlower = curFlower;
    }
    
}

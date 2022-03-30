package jsf.managedBean;

import entity.GiftCardType;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author JORD-SSD
 */
@Named(value = "viewGiftCardTypeManagedBean")
@ViewScoped
public class ViewGiftCardTypeManagedBean implements Serializable {

    private GiftCardType giftCardTypeEntityToView;
    
    public ViewGiftCardTypeManagedBean() {
        giftCardTypeEntityToView = new GiftCardType();
    }
    
    @PostConstruct
    public void postConstruct()
    {
    }

    public GiftCardType getGiftCardTypeEntityToView() {
        return giftCardTypeEntityToView;
    }

    public void setGiftCardTypeEntityToView(GiftCardType giftCardTypeEntityToView) {
        this.giftCardTypeEntityToView = giftCardTypeEntityToView;
    }
    
}

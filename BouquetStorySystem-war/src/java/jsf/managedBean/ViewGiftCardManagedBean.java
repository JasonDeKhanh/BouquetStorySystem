/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import entity.GiftCard;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author matt_
 */
@Named(value = "viewGiftCardManagedBean")
@ViewScoped
public class ViewGiftCardManagedBean implements Serializable {

    private GiftCard giftCardToView;
    private StreamedContent file;

    public ViewGiftCardManagedBean() {
        giftCardToView = new GiftCard();
    }

    /**
     * @return the giftCardToView
     */
    public GiftCard getGiftCardToView() {
        return giftCardToView;
    }

    /**
     * @param giftCardToView the giftCardToView to set
     */
    public void setGiftCardToView(GiftCard giftCardToView) {
        this.giftCardToView = giftCardToView;
    }

    /**
     * @return the file
     */
    public StreamedContent getFile() {
        String photoImgAddress = giftCardToView.getPhotoImgAddress();
        String photoImgFormat = photoImgAddress.substring(photoImgAddress.length() - 3, photoImgAddress.length());

        if (photoImgFormat.equals("jpg")) {
            file = DefaultStreamedContent.builder()
                    .name("downloaded_giftCardImage.jpg")
                    .contentType("image/jpg")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(photoImgAddress))
                    .build();
        } else {
            System.out.println("in else ");
            file = DefaultStreamedContent.builder()
                    .name("downloaded_giftCardImage.png")
                    .contentType("image/png")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(photoImgAddress))
                    .build();
        }
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(StreamedContent file) {
        this.file = file;
    }

}

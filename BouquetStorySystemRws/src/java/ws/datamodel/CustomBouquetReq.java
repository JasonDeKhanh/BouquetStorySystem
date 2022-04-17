/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Container;
import entity.CustomBouquet;
import entity.Decoration;
import entity.Flower;

/**
 *
 * @author msipc
 */
public class CustomBouquetReq {

    
    private CustomBouquet customBouquet;
    private Container container;
    private Flower[] flowers;
    private Integer[] flowerQuantities;
    private Decoration[] decorations;
    private Integer[] decorationQuantities;
    
    public CustomBouquet getCustomBouquet() {
        return customBouquet;
    }

    public void setCustomBouquet(CustomBouquet customBouquet) {
        this.customBouquet = customBouquet;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Flower[] getFlowers() {
        return flowers;
    }

    public void setFlowers(Flower[] flowers) {
        this.flowers = flowers;
    }

    public Integer[] getFlowerQuantities() {
        return flowerQuantities;
    }

    public void setFlowerQuantities(Integer[] flowerQuantities) {
        this.flowerQuantities = flowerQuantities;
    }

    public Decoration[] getDecorations() {
        return decorations;
    }

    public void setDecorations(Decoration[] decorations) {
        this.decorations = decorations;
    }

    public Integer[] getDecorationQuantities() {
        return decorationQuantities;
    }

    public void setDecorationQuantities(Integer[] decorationQuantities) {
        this.decorationQuantities = decorationQuantities;
    }
    
}

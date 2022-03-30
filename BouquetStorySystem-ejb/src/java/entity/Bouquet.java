/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author msipc
 */
@Entity
//@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Bouquet extends Product implements Serializable {

    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected Long bouquetId;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String creatorName;
    
    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn
    private Container container;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Decoration> decorations;
    
    @ManyToMany
    private List<Flower> flowers;

    public Bouquet() {
        super();
        this.decorations = new ArrayList<>();
        this.flowers = new ArrayList<>();
    }

    public Bouquet(String creatorName) {
        
        super();
        
        this.creatorName = creatorName;
    }
    
    
    

//    public Long getBouquetId() {
//        return bouquetId;
//    }
//
//    public void setBouquetId(Long bouquetId) {
//        this.bouquetId = bouquetId;
//    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public List<Decoration> getDecorations() {
        return decorations;
    }

    public void setDecorations(List<Decoration> decorations) {
        this.decorations = decorations;
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public void setFlowers(List<Flower> flowers) {
        this.flowers = flowers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bouquet)) {
            return false;
        }
        Bouquet other = (Bouquet) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bouquet[ id=" + itemId + " ]";
    }
    
}

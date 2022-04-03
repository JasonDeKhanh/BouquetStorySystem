/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author msipc
 */
@Entity
public class FlowerType implements Serializable {
    // this entity class is like a Flower Category, a leaf category (no parents) :D ~ Jason March 4 2022 help me I want to sleep

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flowerTypeId;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(max = 32)
    private String name;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String description;
    
    @OneToMany(mappedBy = "flowerType", fetch = FetchType.EAGER)
    private List<Flower> flowerEntities;

    public FlowerType() {
    }

    public FlowerType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public Long getFlowerTypeId() {
        return flowerTypeId;
    }

    public void setFlowerTypeId(Long flowerTypeId) {
        this.flowerTypeId = flowerTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flowerTypeId != null ? flowerTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlowerType)) {
            return false;
        }
        FlowerType other = (FlowerType) object;
        if ((this.flowerTypeId == null && other.flowerTypeId != null) || (this.flowerTypeId != null && !this.flowerTypeId.equals(other.flowerTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlowerType[ id=" + flowerTypeId + " ]";
    }

    /**
     * @return the flowerEntities
     */
    public List<Flower> getFlowerEntities() {
        return flowerEntities;
    }

    /**
     * @param flowerEntities the flowerEntities to set
     */
    public void setFlowerEntities(List<Flower> flowerEntities) {
        this.flowerEntities = flowerEntities;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author msipc
 */
@Entity
public class ContainerType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long containerTypeId;
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String dimensions;

    public ContainerType() {
    }

    public ContainerType(String name, String dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }
    
    

    public Long getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(Long containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (containerTypeId != null ? containerTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContainerType)) {
            return false;
        }
        ContainerType other = (ContainerType) object;
        if ((this.containerTypeId == null && other.containerTypeId != null) || (this.containerTypeId != null && !this.containerTypeId.equals(other.containerTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ContainerType[ id=" + containerTypeId + " ]";
    }
    
}

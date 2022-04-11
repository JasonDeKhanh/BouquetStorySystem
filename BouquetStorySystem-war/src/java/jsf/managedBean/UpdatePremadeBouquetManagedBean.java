/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.ContainerSessionBeanLocal;
import ejb.stateless.DecorationSessionBeanLocal;
import ejb.stateless.FlowerSessionBeanLocal;
import ejb.stateless.PremadeBouquetSessionBeanLocal;
import entity.Container;
import entity.Decoration;
import entity.Flower;
import entity.PremadeBouquet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.OccasionEnum;
import util.exception.ContainerNotFoundException;
import util.exception.DecorationNotFoundException;
import util.exception.FlowerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PremadeBouquetNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePremadeBouquetException;

/**
 *
 * @author msipc
 */
@Named(value = "updatePremadeBouquetManagedBean")
@ViewScoped
public class UpdatePremadeBouquetManagedBean implements Serializable {

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "PremadeBouquetSessionBeanLocal")
    private PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal;

    private Long premadeBouquetToUpdateId;
    private PremadeBouquet updatePremadeBouquet;
    
    private List<OccasionEnum> updateOccasionEnums;
    private List<OccasionEnum> occasionEnums;
    
    private Long updateContainerId;
    private List<Container> containers;
    
    private Long selectedDecorationId;
    private Integer selectedDecorationQuantity;
    private Map<Decoration, Integer> updateDecorations;
    private List<Decoration> decorations;
    
    private Decoration decorationToUpdateQuantity;
    private Integer decorationQuantityToUpdate;
    
    private Long selectedFlowerId;
    private Integer selectedFlowerQuantity;
    private Map<Flower, Integer> updateFlowers;
    private List<Flower> flowers;
    
    private Flower flowerToUpdateQuantity;
    private Integer flowerQuantityToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;

    public UpdatePremadeBouquetManagedBean() {
        updateOccasionEnums = new ArrayList<>();
        updateDecorations = new HashMap<>();
        updateFlowers = new HashMap<>();
        
        updatePremadeBouquet = new PremadeBouquet();
        
        occasionEnums = Arrays.asList(OccasionEnum.values());
    }
    
    @PostConstruct
    public void postConstruct()
    {
        containers = containerSessionBeanLocal.retrieveAllContainers();
        decorations = decorationSessionBeanLocal.retrieveAllDecorations();
        flowers = flowerSessionBeanLocal.retrieveAllFlowers();
        
        try 
        {
            containers = containerSessionBeanLocal.retrieveAllContainers();
            decorations = decorationSessionBeanLocal.retrieveAllDecorations();
            flowers = flowerSessionBeanLocal.retrieveAllFlowers();
            
            premadeBouquetToUpdateId = (Long.valueOf(
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("premadeBouquetToUpdateId")));
            updatePremadeBouquet = premadeBouquetSessionBeanLocal.retrievePremadeBouquetByItemId(premadeBouquetToUpdateId);
            
            updateOccasionEnums.addAll(updatePremadeBouquet.getOccasions());
            updateContainerId = updatePremadeBouquet.getContainer().getContainerId();
            updateDecorations.putAll(updatePremadeBouquet.getDecorationQuantities());
            updateFlowers.putAll(updatePremadeBouquet.getFlowerQuantities());
            
        } 
        catch (PremadeBouquetNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error has occurred while retrieving the premade bouquet record: " + ex.getMessage(), null));
        } 
        catch (NumberFormatException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Premade Bouquet ID not provided", null));
        }
    }

    public Container getContainerById()
    {
        for(Container container: containers) {
            if(container.getContainerId().equals(updateContainerId)){
                return container;
            }
        }
        return null;
    }
    
    public Decoration getDecorationById()
    {
        for(Decoration decoration: decorations) {
            if(decoration.getDecorationId().equals(selectedDecorationId)){
                return decoration;
            }
        }
        return null;
    }
    
    public Flower getFlowerById()
    {
        for(Flower flower: flowers) {
            if(flower.getFlowerId().equals(selectedFlowerId)){
                return flower;
            }
        }
        return null;
    }
    
    public void addDecorationToList(ActionEvent event)
    {
        try 
        {
            if(selectedDecorationQuantity == null)
            {
                selectedDecorationQuantity = 1;
            }
            
            Decoration decorationToAdd = decorationSessionBeanLocal.retrieveDecorationByDecorationId(selectedDecorationId);

            updateDecorations.put(decorationToAdd, selectedDecorationQuantity);
            System.out.println("hashmap currently: " + updateDecorations.toString());
        
            selectedDecorationId = null;
            selectedDecorationQuantity = null;
        } 
        catch (DecorationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error occured: " + ex.getMessage(), null));
        }
    }
    
    public void deleteDecorationFromList(ActionEvent event)
    {
        System.out.println("inside delete Decoration From List");
        Decoration decorationToDelete = (Decoration)event.getComponent().getAttributes().get("decorationToDelete");
        System.out.println("Decoration to delete: " + decorationToDelete.getName());
        updateDecorations.remove(decorationToDelete);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Decoration " + decorationToDelete.getName() + " deleted from list to add successfully", "Decoration " + decorationToDelete.getName() + " deleted from lsit to add successfully"));
    }
    
    public void doUpdateDecorationQuantityInList(ActionEvent event)
    {
        decorationToUpdateQuantity = (Decoration)event.getComponent().getAttributes().get("decorationToUpdateQuantity");
        decorationQuantityToUpdate = updateDecorations.get(decorationToUpdateQuantity);
    }
    
    public void updateDecorationQuantityInList(ActionEvent event)
    {
        updateDecorations.put(decorationToUpdateQuantity, decorationQuantityToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Decoration quantity updated successfully", "Decoration quantity updated successfully"));
    
    }
    
    public void addFlowerToList(ActionEvent event)
    {
        try 
        {
            if(selectedFlowerQuantity == null)
            {
                selectedFlowerQuantity = 1;
            }
            Flower flowerToAdd = flowerSessionBeanLocal.retrieveFlowerByFlowerId(selectedFlowerId);
            System.out.println("Flower: " + flowerToAdd.getName() + ", Quantity: " + selectedFlowerQuantity);
            updateFlowers.put(flowerToAdd, selectedFlowerQuantity);
            System.out.println("hashmap currently: " + updateFlowers.toString());
        } 
        catch (FlowerNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error occured: " + ex.getMessage(), null));
        }
    }
    
    public void deleteFlowerFromList(ActionEvent event)
    {
        System.out.println("inside Delete Flower From List");
        Flower flowerToDelete = (Flower)event.getComponent().getAttributes().get("flowerToDelete");
    
        updateFlowers.remove(flowerToDelete);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower " + flowerToDelete.getName() + " deleted from list to add successfully", "Flower " + flowerToDelete.getName() + " deleted from lsit to add successfully"));
    }
    
    public void doUpdateFlowerQuantityInList(ActionEvent event)
    {
        flowerToUpdateQuantity = (Flower)event.getComponent().getAttributes().get("flowerToUpdateQuantity");
        flowerQuantityToUpdate = updateFlowers.get(flowerToUpdateQuantity);
    }
    
    public void updateFlowerQuantityInList(ActionEvent event)
    {
        updateFlowers.put(flowerToUpdateQuantity, flowerQuantityToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower quantity updated successfully", "Flower quantity updated successfully"));
    
    }
    
    ///
    public void updatePremadeBouquet(ActionEvent event)
    {
        try 
        {
            // link container
            Container containerToAdd = containerSessionBeanLocal.retrieveContainerByContainerId(updateContainerId);
//            newPremadeBouquet.setContainer(containToAdd);
            
            List<Decoration> updateDecorationsList = new ArrayList<>();
            // link deocrations
            for(Map.Entry<Decoration,Integer> decorationMap : updateDecorations.entrySet())
            {
                // associate
//                newPremadeBouquet.getDecorations().add(decorationMap.getKey());
                // nvm must do in session bean
                updateDecorationsList.add(decorationMap.getKey());
                // update quantities
                updatePremadeBouquet.getDecorationQuantities().put(decorationMap.getKey(), decorationMap.getValue());
            }
            
            List<Flower> updateFlowersList = new ArrayList<>();
            // link flowers
            for(Map.Entry<Flower,Integer> flowerMap : updateFlowers.entrySet())
            {
                // associate
//                newPremadeBouquet.getFlowers().add(flowerMap.getKey());
                // nvm must do inside session bean
                updateFlowersList.add(flowerMap.getKey());
                // update quantities
                System.out.println("Type of flowerMap key: " + flowerMap.getKey().getClass());
                System.out.println("Type of flowerMap value: " + flowerMap.getValue().getClass().getSimpleName());
                updatePremadeBouquet.getFlowerQuantities().put(flowerMap.getKey(), flowerMap.getValue());
            }
            
            // link occasions
            System.out.println("updateOccasionEnums: " + updateOccasionEnums.toString());
            updatePremadeBouquet.getOccasions().clear();
            updatePremadeBouquet.getOccasions().addAll(updateOccasionEnums);
            
            // call session bean to create
//            PremadeBouquet pb = premadeBouquetSessionBeanLocal.updatePremadeBouquet(updatePremadeBouquet, containerToAdd.getContainerId(), updateDecorationsList, updateFlowersList);
            premadeBouquetSessionBeanLocal.updatePremadeBouquet(updatePremadeBouquet, containerToAdd.getContainerId(), updateDecorationsList, updateFlowersList);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Premade bouquet updated successfully", null));
        } 
        catch (InputDataValidationException | PremadeBouquetNotFoundException | UpdatePremadeBouquetException | ContainerNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new premade bouquet: " + ex.getMessage(), null));
        }
        
    }
    
    
    public void handleImageUploadUpdate(FileUploadEvent event) {
        // from Prof's Lecture 6 Demo 3
        try {
//            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_2") + System.getProperty("file.separator") + event.getFile().getFileName();
            String test = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_2");
            String test2 = System.getProperty("file.separator");
            String test3 = event.getFile().getFileName();
            System.out.println("test: " + test);
            System.out.println("test2: " + test2);
            System.out.println("test3: " + test3);
            String newFilePath = test + test2 + test3;

            System.err.println("********** Bouquet Story System: File name: " + event.getFile().getFileName());
            System.err.println("********** Bouquet Story System: newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            setShowUploadedFile((Boolean) true);

            // Would it be correct to put setImgAddress(uploadedFilePath) here??
            getUpdatePremadeBouquet().setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public Long getPremadeBouquetToUpdateId() {
        return premadeBouquetToUpdateId;
    }

    public void setPremadeBouquetToUpdateId(Long premadeBouquetToUpdateId) {
        this.premadeBouquetToUpdateId = premadeBouquetToUpdateId;
    }
    
    
    /////////
    public PremadeBouquet getUpdatePremadeBouquet() {
        return updatePremadeBouquet;
    }

    public void setUpdatePremadeBouquet(PremadeBouquet updatePremadeBouquet) {
        this.updatePremadeBouquet = updatePremadeBouquet;
    }

    public List<OccasionEnum> getUpdateOccasionEnums() {
        return updateOccasionEnums;
    }

    public void setUpdateOccasionEnums(List<OccasionEnum> updateOccasionEnums) {
        this.updateOccasionEnums = updateOccasionEnums;
    }

    public List<OccasionEnum> getOccasionEnums() {
        return occasionEnums;
    }

    public void setOccasionEnums(List<OccasionEnum> occasionEnums) {
        this.occasionEnums = occasionEnums;
    }

    public Long getUpdateContainerId() {
        return updateContainerId;
    }

    public void setUpdateContainerId(Long updateContainerId) {
        this.updateContainerId = updateContainerId;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public Long getSelectedDecorationId() {
        return selectedDecorationId;
    }

    public void setSelectedDecorationId(Long selectedDecorationId) {
        this.selectedDecorationId = selectedDecorationId;
    }

    public Integer getSelectedDecorationQuantity() {
        return selectedDecorationQuantity;
    }

    public void setSelectedDecorationQuantity(Integer selectedDecorationQuantity) {
        this.selectedDecorationQuantity = selectedDecorationQuantity;
    }

    public Map<Decoration, Integer> getUpdateDecorations() {
        return updateDecorations;
    }

    public void setUpdateDecorations(Map<Decoration, Integer> updateDecorations) {
        this.updateDecorations = updateDecorations;
    }

    public List<Decoration> getDecorations() {
        return decorations;
    }

    public void setDecorations(List<Decoration> decorations) {
        this.decorations = decorations;
    }

    public Decoration getDecorationToUpdateQuantity() {
        return decorationToUpdateQuantity;
    }

    public void setDecorationToUpdateQuantity(Decoration decorationToUpdateQuantity) {
        this.decorationToUpdateQuantity = decorationToUpdateQuantity;
    }

    public Integer getDecorationQuantityToUpdate() {
        return decorationQuantityToUpdate;
    }

    public void setDecorationQuantityToUpdate(Integer decorationQuantityToUpdate) {
        this.decorationQuantityToUpdate = decorationQuantityToUpdate;
    }

    public Long getSelectedFlowerId() {
        return selectedFlowerId;
    }

    public void setSelectedFlowerId(Long selectedFlowerId) {
        this.selectedFlowerId = selectedFlowerId;
    }

    public Integer getSelectedFlowerQuantity() {
        return selectedFlowerQuantity;
    }

    public void setSelectedFlowerQuantity(Integer selectedFlowerQuantity) {
        this.selectedFlowerQuantity = selectedFlowerQuantity;
    }

    public Map<Flower, Integer> getUpdateFlowers() {
        return updateFlowers;
    }

    public void setUpdateFlowers(Map<Flower, Integer> updateFlowers) {
        this.updateFlowers = updateFlowers;
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public void setFlowers(List<Flower> flowers) {
        this.flowers = flowers;
    }

    public Flower getFlowerToUpdateQuantity() {
        return flowerToUpdateQuantity;
    }

    public void setFlowerToUpdateQuantity(Flower flowerToUpdateQuantity) {
        this.flowerToUpdateQuantity = flowerToUpdateQuantity;
    }

    public Integer getFlowerQuantityToUpdate() {
        return flowerQuantityToUpdate;
    }

    public void setFlowerQuantityToUpdate(Integer flowerQuantityToUpdate) {
        this.flowerQuantityToUpdate = flowerQuantityToUpdate;
    }

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public Boolean getShowUploadedFile() {
        return showUploadedFile;
    }

    public void setShowUploadedFile(Boolean showUploadedFile) {
        this.showUploadedFile = showUploadedFile;
    }
    
}

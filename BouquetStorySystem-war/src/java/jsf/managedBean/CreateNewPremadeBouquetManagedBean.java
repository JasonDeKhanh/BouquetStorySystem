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
import entity.Admin;
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
import util.exception.CreateNewPremadeBouquetException;
import util.exception.DecorationNotFoundException;
import util.exception.FlowerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author msipc
 */
@Named(value = "createNewPremadeBouquetManagedBean")
@ViewScoped
public class CreateNewPremadeBouquetManagedBean implements Serializable {

    @EJB(name = "FlowerSessionBeanLocal")
    private FlowerSessionBeanLocal flowerSessionBeanLocal;

    @EJB(name = "ContainerSessionBeanLocal")
    private ContainerSessionBeanLocal containerSessionBeanLocal;

    @EJB(name = "DecorationSessionBeanLocal")
    private DecorationSessionBeanLocal decorationSessionBeanLocal;

    @EJB(name = "PremadeBouquetSessionBeanLocal")
    private PremadeBouquetSessionBeanLocal premadeBouquetSessionBeanLocal;

    
    
    private PremadeBouquet newPremadeBouquet;
    
    private List<OccasionEnum> newOccasionEnums;
    private List<OccasionEnum> occasionEnums;
    
    private Long newContainerId;
    private List<Container> containers;
    
    private Long selectedDecorationId;
    private Integer selectedDecorationQuantity;
    private Map<Decoration, Integer> newDecorations;
    private List<Decoration> decorations;
    
    private Decoration decorationToUpdateQuantity;
    private Integer decorationQuantityToUpdate;
    
    private Long selectedFlowerId;
    private Integer selectedFlowerQuantity;
    private Map<Flower, Integer> newFlowers;
    private List<Flower> flowers;
    
    private Flower flowerToUpdateQuantity;
    private Integer flowerQuantityToUpdate;
    
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    


    public CreateNewPremadeBouquetManagedBean() {
        newDecorations = new HashMap<>();
        newFlowers = new HashMap<>();
        
        newPremadeBouquet = new PremadeBouquet();
        Admin currentAdmin = (Admin)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentAdmin");
        newPremadeBouquet.setCreatorName(currentAdmin.getFirstName() + " " + currentAdmin.getLastName() + " (BouquetStory)");
    
        occasionEnums = Arrays.asList(OccasionEnum.values());
    }
    
    @PostConstruct
    public void postConstruct()
    {
        containers = containerSessionBeanLocal.retrieveAllContainers();
        decorations = decorationSessionBeanLocal.retrieveAllDecorations();
        flowers = flowerSessionBeanLocal.retrieveAllFlowers();
    }
    
    
    
    public Container getContainerById()
    {
        for(Container container: containers) {
            if(container.getContainerId().equals(newContainerId)){
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
        System.out.println("inside addDecoration beginning!");
        try 
        {
            if(selectedDecorationQuantity == null)
            {
                selectedDecorationQuantity = 1;
            }
            
            System.out.println("inside addDecoration try block!");
            Decoration decorationToAdd = decorationSessionBeanLocal.retrieveDecorationByDecorationId(selectedDecorationId);
            System.out.println("after calling session bean, before put");
            System.out.println("Decoration: " + decorationToAdd.getName() + ", Quantity: " + selectedDecorationQuantity);
            newDecorations.put(decorationToAdd, selectedDecorationQuantity);
            System.out.println("hashmap currently: " + newDecorations.toString());
        
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
        newDecorations.remove(decorationToDelete);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Decoration " + decorationToDelete.getName() + " deleted from list to add successfully", "Decoration " + decorationToDelete.getName() + " deleted from lsit to add successfully"));
    }
    
    public void doUpdateDecorationQuantityInList(ActionEvent event)
    {
        decorationToUpdateQuantity = (Decoration)event.getComponent().getAttributes().get("decorationToUpdateQuantity");
        decorationQuantityToUpdate = newDecorations.get(decorationToUpdateQuantity);
    }
    
    public void updateDecorationQuantityInList(ActionEvent event)
    {
        newDecorations.put(decorationToUpdateQuantity, decorationQuantityToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Decoration quantity updated successfully", "Decoration quantity updated successfully"));
    
    }
    
    public void addFlowerToList(ActionEvent event)
    {
        System.out.println("inside addFlowerToList beginning!");
        try 
        {
            if(selectedFlowerQuantity == null)
            {
                selectedFlowerQuantity = 1;
            }
            System.out.println("inside addFlowerToList try block!");
            Flower flowerToAdd = flowerSessionBeanLocal.retrieveFlowerByFlowerId(selectedFlowerId);
            System.out.println("Flower: " + flowerToAdd.getName() + ", Quantity: " + selectedFlowerQuantity);
            newFlowers.put(flowerToAdd, selectedFlowerQuantity);
            System.out.println("hashmap currently: " + newFlowers.toString());
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
    
        newFlowers.remove(flowerToDelete);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower " + flowerToDelete.getName() + " deleted from list to add successfully", "Flower " + flowerToDelete.getName() + " deleted from lsit to add successfully"));
    }
    
    public void doUpdateFlowerQuantityInList(ActionEvent event)
    {
        flowerToUpdateQuantity = (Flower)event.getComponent().getAttributes().get("flowerToUpdateQuantity");
        flowerQuantityToUpdate = newFlowers.get(flowerToUpdateQuantity);
    }
    
    public void updateFlowerQuantityInList(ActionEvent event)
    {
        newFlowers.put(flowerToUpdateQuantity, flowerQuantityToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flower quantity updated successfully", "Flower quantity updated successfully"));
    
    }
    
    ///
    public void createNewPremadeBouquet(ActionEvent event)
    {
        try 
        {
            // link container
            Container containerToAdd = containerSessionBeanLocal.retrieveContainerByContainerId(newContainerId);
//            newPremadeBouquet.setContainer(containToAdd);
            
            List<Decoration> newDecorationsList = new ArrayList<>();
            // link deocrations
            for(Map.Entry<Decoration,Integer> decorationMap : newDecorations.entrySet())
            {
                // associate
//                newPremadeBouquet.getDecorations().add(decorationMap.getKey());
                // nvm must do in session bean
                newDecorationsList.add(decorationMap.getKey());
                // update quantities
                newPremadeBouquet.getDecorationQuantities().put(decorationMap.getKey(), decorationMap.getValue());
            }
            
            List<Flower> newFlowersList = new ArrayList<>();
            // link flowers
            for(Map.Entry<Flower,Integer> flowerMap : newFlowers.entrySet())
            {
                // associate
//                newPremadeBouquet.getFlowers().add(flowerMap.getKey());
                // nvm must do inside session bean
                newFlowersList.add(flowerMap.getKey());
                // update quantities
                System.out.println("Type of flowerMap key: " + flowerMap.getKey().getClass());
                System.out.println("Type of flowerMap value: " + flowerMap.getValue().getClass().getSimpleName());
                newPremadeBouquet.getFlowerQuantities().put(flowerMap.getKey(), flowerMap.getValue());
            }
            
            // link occasions
            newPremadeBouquet.setOccasions(newOccasionEnums);
            
            // call session bean to create
            PremadeBouquet pb = premadeBouquetSessionBeanLocal.createNewPremadeBouquet(newPremadeBouquet, containerToAdd.getContainerId(), newDecorationsList, newFlowersList);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New premade bouquet created successfully (PremadeBouquet ID: " + pb.getItemId()+ ")", null));
        } 
        catch (InputDataValidationException | CreateNewPremadeBouquetException | ContainerNotFoundException | UnknownPersistenceException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new premade bouquet: " + ex.getMessage(), null));
        }
        
    }
    
    ///
    public void handleImageUploadNew(FileUploadEvent event) {
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
            getNewPremadeBouquet().setImgAddress(uploadedFilePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public PremadeBouquet getNewPremadeBouquet() {
        return newPremadeBouquet;
    }

    public void setNewPremadeBouquet(PremadeBouquet newPremadeBouquet) {
        this.newPremadeBouquet = newPremadeBouquet;
    }

    public List<OccasionEnum> getNewOccasionEnums() {
        return newOccasionEnums;
    }

    public void setNewOccasionEnums(List<OccasionEnum> newOccasionEnums) {
        this.newOccasionEnums = newOccasionEnums;
    }

    public List<OccasionEnum> getOccasionEnums() {
        return occasionEnums;
    }

    public void setOccasionEnums(List<OccasionEnum> occasionEnums) {
        this.occasionEnums = occasionEnums;
    }

    public Long getNewContainerId() {
        return newContainerId;
    }

    public void setNewContainerId(Long newContainerId) {
        this.newContainerId = newContainerId;
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

    public Map<Decoration, Integer> getNewDecorations() {
        return newDecorations;
    }

    public void setNewDecorations(Map<Decoration, Integer> newDecorations) {
        this.newDecorations = newDecorations;
    }

    public List<Decoration> getDecorations() {
        return decorations;
    }

    public void setDecorations(List<Decoration> decorations) {
        this.decorations = decorations;
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

    public Map<Flower, Integer> getNewFlowers() {
        return newFlowers;
    }

    public void setNewFlowers(Map<Flower, Integer> newFlowers) {
        this.newFlowers = newFlowers;
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public void setFlowers(List<Flower> flowers) {
        this.flowers = flowers;
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
    
}

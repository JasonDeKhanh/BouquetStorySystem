/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedBean;

import ejb.stateless.SaleTransactionSessionBeanLocal;
import entity.SaleTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author matt_
 */
@Named(value = "scheduleManagedBean")
@ViewScoped
public class scheduleManagedBean implements Serializable {

    @EJB(name = "SaleTransactionSessionBeanLocal")
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;
    private ScheduleModel scheduleModel;
    private ScheduleEvent scheduleEvent;

    /**
     * Creates a new instance of scheduleManagedBean
     */
    public scheduleManagedBean() {
        scheduleModel = new DefaultScheduleModel();
        scheduleEvent = new DefaultScheduleEvent();
    }

    @PostConstruct
    public void postConstruct() {
        List<SaleTransaction> saleTransactions = saleTransactionSessionBeanLocal.retrieveAllSaleTransactions();

        for (SaleTransaction saleTransaction : saleTransactions) {
            scheduleModel.addEvent(DefaultScheduleEvent.builder()
                    .title("Order for Customer" + saleTransaction.getCustomer().getFirstName() + "\n" + "Self PickUp: " + saleTransaction.getIsSelfPickup() + "\n" + "Delivery Address: " + saleTransaction.getDeliveryAddress())
                    .startDate(convertToLocalDateTimeViaInstant(saleTransaction.getCollectionDateTime()))
                    .endDate(convertToLocalDateTimeViaInstant(saleTransaction.getCollectionDateTime()))
                    .description("" + saleTransaction.getSaleTransactionId())
                    .build());

        }
    }

    public String getFormatDate() {
        if (scheduleEvent.getStartDate() == null) {
            return "";
        } else {
            LocalDateTime date = scheduleEvent.getStartDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            String formatDateTime = date.format(formatter);
            return formatDateTime;
        }
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public ScheduleModel getScheduleModel() {
        return scheduleModel;
    }

    public void setScheduleModel(ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
    }

    public ScheduleEvent getScheduleEvent() {
        return scheduleEvent;
    }

    public void setScheduleEvent(ScheduleEvent scheduleEvent) {
        this.scheduleEvent = scheduleEvent;
    }

    public void addEvent(ActionEvent actionEvent) {
        if (scheduleEvent.getId() == null) {
            scheduleModel.addEvent(scheduleEvent);
        } else {
            scheduleModel.updateEvent(scheduleEvent);
        }

        scheduleEvent = new DefaultScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        scheduleEvent = (ScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {

        scheduleEvent = DefaultScheduleEvent.builder()
                .title("")
                .startDate((LocalDateTime) selectEvent.getObject())
                .endDate((LocalDateTime) selectEvent.getObject())
                .build();
    }

    public void onEventMove(ScheduleEntryMoveEvent scheduleEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + scheduleEvent.getDayDelta() + ", Minute delta:" + scheduleEvent.getMinuteDelta());

        addMessage(message);
        saleTransactionSessionBeanLocal.updateDeliveryDate(Long.valueOf(scheduleEvent.getScheduleEvent().getDescription()), scheduleEvent.getScheduleEvent().getStartDate());
    }

    public void onEventResize(ScheduleEntryResizeEvent scheduleEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + scheduleEvent.getDayDeltaStart() + " to " + scheduleEvent.getDayDeltaEnd() + ", Minute delta:" + scheduleEvent.getMinuteDeltaStart() + " to " + scheduleEvent.getMinuteDeltaEnd());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar;
    }

    private LocalDateTime previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime today6Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime fourDaysLater3pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
        t.set(Calendar.HOUR, 3);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

}

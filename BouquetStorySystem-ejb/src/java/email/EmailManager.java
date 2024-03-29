package util.email;

//import entity.SaleTransactionEntity;
//import entity.SaleTransactionLineItemEntity;
import entity.Customer;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class EmailManager 
{
    private final String emailServerName = "smtp.gmail.com";
    private final String mailer = "JavaMailer";
    private String smtpAuthUser;
    private String smtpAuthPassword;
    
    
    
    public EmailManager()
    {
    }

    
    
    public EmailManager(String smtpAuthUser, String smtpAuthPassword)
    {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }
    
    public Boolean emailRegisterNotification(Customer customerEntity, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "Hello! "+customerEntity.getFirstName()+" "+customerEntity.getLastName() 
                + ". You have successfully registered a new account with Bouquet Story! \n\n";
    
        try 
        {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", emailServerName);
            props.put("mail.smtp.debug", "true");    
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);            
            
            Message msg = new MimeMessage(session);                                    
            msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
            msg.setSubject("[Bouquet Story] Registration Successfully!");
            msg.setText(emailBody);
            msg.setHeader("X-Mailer", mailer);

            Date timeStamp = new Date();
            msg.setSentDate(timeStamp);

            Transport.send(msg);

            return true;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            
            return false;
        }
    }
    

    public Boolean emailCollectionOrDeliverTimerNotification(Customer customerEntity, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "Hello! Dear "+customerEntity.getFirstName()+" "+customerEntity.getLastName() 
                + ". Your shipment with us will be delivered today! \n\n";
    
        try 
        {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", emailServerName);
            props.put("mail.smtp.debug", "true");    
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);            
            
            Message msg = new MimeMessage(session);                                    
            msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
            msg.setSubject("[Bouquet Story] You Have A Delivery Today!");
            msg.setText(emailBody);
            msg.setHeader("X-Mailer", mailer);

            Date timeStamp = new Date();
            msg.setSentDate(timeStamp);

            Transport.send(msg);

            return true;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    
    
    public Boolean emailCheckoutNotification(SaleTransaction saleTransactionEntity, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "You have completed the checkout successfully for Sale Transaction ID: " + saleTransactionEntity.getSaleTransactionId() +  "\n\n";
        emailBody += "S/N       Product Name     Quantity     Unit Price     Sub-Total\n\n";
            
        for(SaleTransactionLineItem saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItems())
        {
            emailBody += saleTransactionLineItemEntity.getSerialNumber()
                + "     " + saleTransactionLineItemEntity.getItemEntity().getName()
                + "     " + saleTransactionLineItemEntity.getQuantity()
                + "     " + NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getUnitPrice())
                + "     " + NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getUnitPrice().multiply(BigDecimal.valueOf(saleTransactionLineItemEntity.getQuantity()))) + "\n";
        }
            
        emailBody += "\nTotal Line Item: " + saleTransactionEntity.getTotalLineItem() + ", Total Quantity: " + saleTransactionEntity.getTotalQuantity() + ", Total Amount: " + NumberFormat.getCurrencyInstance().format(saleTransactionEntity.getTotalAmount()) + "\n";
        
        
        
        try 
        {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", emailServerName);
            props.put("mail.smtp.debug", "true");       
            
            Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);            
            
            Message msg = new MimeMessage(session);                                    
            msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
            msg.setSubject("Checkout Completed Successfully!");
            msg.setText(emailBody);
            msg.setHeader("X-Mailer", mailer);

            Date timeStamp = new Date();
            msg.setSentDate(timeStamp);

            Transport.send(msg);

            return true;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            
            return false;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.awt.Component;
import java.util.Properties;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.swing.JOptionPane;

/**
 *
 * @author Yornel
 */
public class SendMail {
    
    public SendMail() {;
    }
    
    private static String username = "tgh.ymarval@gmail.com";
    private static String password = "lasombra";
    private static String Mensage = "tes mensaje";
    private static String To = "yornelmarval@gmail.com";
    private static String Subject = "";
    
    public void SendMail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
 
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
 
        try {
 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(To));
            message.setSubject(Subject);
            message.setText(Mensage);
 
            Transport.send(message);
            //JOptionPane.showMessageDialog(parentComponent, "Su mensaje ha sido enviado");
            System.out.println("Su mensaje ha sido enviado");
 
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

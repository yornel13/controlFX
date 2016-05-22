/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Yornel
 */
public class CorreoUtil {

    public static void mandarCorreo(String empresa, String destinatario, String asunto, 
            String mensaje, String file, String filename) {
        
        // El correo gmail de envío
        String correoEnvia = "tgh.ymarval@gmail.com";
        String claveCorreo = "lasombra";

        // La configuración para enviar correo
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);

        // Obtener la sesion
        Session session = Session.getInstance(properties, null);

        try {
            // Crear el cuerpo del mensaje
            MimeMessage mimeMessage = new MimeMessage(session);

            // Agregar quien envía el correo
            mimeMessage.setFrom(new InternetAddress(correoEnvia, empresa));

            // Los destinatarios
            InternetAddress[] internetAddresses = {
                new InternetAddress(destinatario)};

            // Agregar los destinatarios al mensaje
            mimeMessage.setRecipients(Message.RecipientType.TO,
                internetAddresses);

            // Agregar el asunto al correo
            mimeMessage.setSubject(asunto);

            // Create the message part text attechment
            BodyPart attechmentBodyPart = new MimeBodyPart();
            
            // Create the message part text
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(mensaje);
            
            if (filename != null) {
                DataSource source = new FileDataSource(file);
                attechmentBodyPart.setDataHandler(new DataHandler(source));
                attechmentBodyPart.setFileName(filename);
            }
            //mimeBodyPart.setText(mensaje);
            // Crear el multipart para agregar la parte del mensaje anterior
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attechmentBodyPart);

            // Agregar el multipart al cuerpo del mensaje
            mimeMessage.setContent(multipart);

            // Enviar el mensaje
            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia, claveCorreo);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Correo enviado");
    }

    public static void main(String[] args) {
        
        String empresa = "J PRI Soft CA";
        String destinatario = "yornelmghfarval@gmail.com";
        String asunto = "Rol de pago individual";
        String mensaje = "Pago mensual gfdgfdg dfg fdgdf fd gfdgfd fdgfdgfd fggdfgdfg gfdgfd fdg.";
        String file = "C:\\Users\\Yornel\\Desktop\\rol_individual_20.pdf";
        String filename = "rol_individual_20.pdf";
        
        mandarCorreo(empresa, destinatario, asunto, mensaje, file, filename);

    }
}

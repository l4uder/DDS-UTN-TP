package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ClienteCorreoRealJavaMail implements ClienteCorreo {

  private final String correoRemitente;
  private final String password;

  public ClienteCorreoRealJavaMail(String correoRemitente, String password) {
    this.correoRemitente = correoRemitente;
    this.password = password;
  }

  @Override
  public void enviarCorreo(String correoDestino, String mensajeCuerpo) {
    //Configuración de propiedades para conectarse al servidor SMTP de Gmail
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");

    //Creación de la sesión autenticada
    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(correoRemitente, password);
      }
    });

    //Armado y envío del mensaje
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(correoRemitente));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
      
      message.setSubject("Novedades de tu donación - DonaTrack");
      message.setText(mensajeCuerpo);

      Transport.send(message);
      System.out.println("Éxito: Correo enviado a " + correoDestino);

    } catch (MessagingException e) {
      throw new RuntimeException("Error crítico al intentar enviar el correo a: " + correoDestino, e);
    }
  }
}
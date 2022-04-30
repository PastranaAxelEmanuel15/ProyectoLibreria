/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.servicios;

import java.io.IOException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author AXEL
 */
@Service
public class MailService {

    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${spring.mail.username}")
    private String mailFrom;

    public void enviarEmailSimple(String destinario, String asunto, String contenido) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(destinario);
        simpleMailMessage.setSubject(asunto);
        simpleMailMessage.setText(contenido);
        javaMailSender.send(simpleMailMessage);
    }

    public void genericMail(String mail, String userName, String subject, String artistName, String nombreArchivoHTML) throws MessagingException, IOException {

        Properties prop = new Properties();
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Context context = new Context();
        context.setVariable("artistName", artistName);
            
        String html = templateEngine.process("/emails/" + nombreArchivoHTML + ".html", context);
        new Thread(() -> {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();

                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");
                mimeMessageHelper.setFrom(mailFrom, "Gira por las casas");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(html, true);

                MimeBodyPart mensaje = new MimeBodyPart();
                mensaje.setContent(html, "text/html");

                MimeMultipart multipart = new MimeMultipart();
                multipart.addBodyPart(mensaje);
                message.setContent(multipart);
                
                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}

package com.itechart.contacts.mail;

import com.itechart.contacts.model.People;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class Mail {
    private final static Logger logger = LogManager.getLogger(Mail.class);

    public static void sendMail(List<People> peoples, String subject, String template) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "baturaoleg@gmail.com", "bat130395");
                    }
                });

        try {
            for (int i = 0; i < peoples.size(); ++i) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("baturaoleg@gmail.com"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(peoples.get(i).getEmail()));
                message.setSubject(subject);
                message.setText(MailTemplates.loadFromFile(template, peoples.get(i).getFirstName()));

                // send message
                Transport.send(message);

//                String s1 = peoples.get(i).getEmail();
//                String s2 = MailTemplates.loadFromFile(template, peoples.get(i).getFirstName());
//                System.out.println(s1 + '\n' + s2);
            }
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            logger.error(Level.INFO, e);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            logger.error(Level.INFO, e);
        }
    }

    public static void sendMailText(List<People> peoples, String subject, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "baturaoleg@gmail.com", "bat130395");
                    }
                });

        try {
            for (int i = 0; i < peoples.size(); ++i) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("baturaoleg@gmail.com"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(peoples.get(i).getEmail()));
                message.setSubject(subject);
                message.setText(text);

                // send message
                Transport.send(message);

//                String s1 = peoples.get(i).getEmail();
//                String s2 = text;
//                System.out.println(s1 + '\n' + s2);
            }
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            logger.error(Level.INFO, e);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            logger.error(Level.INFO, e);
        }
    }
}

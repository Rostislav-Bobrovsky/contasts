package com.itechart.contacts.mail;

import com.itechart.contacts.model.People;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Mail {
    private final static Logger logger = LogManager.getLogger(Mail.class);
    public static final String EMAIL = "rostislav.bobrovsky@gmail.com";
    public static final String PASSWORD = "****";

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                EMAIL, PASSWORD);
                    }
                });
    }

    public static void sendMail(List<People> peoples, String subject, String template) {
        logger.info("{}:{}; parameters: {}, {}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), subject, template);

        Session session = getSession();

        try {
            for (People people : peoples) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("EMAIL"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(people.getEmail()));
                message.setSubject(subject);
                message.setText(MailTemplates.loadFromFile(template, people.getFirstName()));

                Transport.send(message);
            }
        } catch (MessagingException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    public static void sendMailText(List<People> peoples, String subject, String text) {
        logger.info("{}:{}; parameters: {}, {}",  Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), subject, text);

        Session session = getSession();

        try {
            for (People people : peoples) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("EMAIL"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(people.getEmail()));
                message.setSubject(subject);
                message.setText(text);

                Transport.send(message);
            }
        } catch (MessagingException e) {
            logger.error("{}:{}; exception {}; {} \n{}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }
}

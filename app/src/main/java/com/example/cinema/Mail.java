package com.example.cinema;


import android.content.Context;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class Mail extends Thread{

    Session session;
    String main;
    String room;
    String price;
    String date;
    String movieName;

    public Mail(String main, String room, String price, String date, String movieName) {

        this.main = main;
        this.date = date;
        this.price = price;
        this.room = room;
        this.movieName = movieName;
    }

    public void run() {

        String Newligne = System.getProperty("line.separator");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        System.out.println("Ici ca marche");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("benjamin.hini@gmail.com", "black0648");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("UGCECE@gmail.com"));
            message.addRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(main));
            message.setSubject("Your Reservation");
            message.setText("Hi " + main + ", "
                    + Newligne + "Thank you for being a fidele client ! You made a reservation for "+movieName+", you paid "+price+" € for this session."
                    + Newligne + "For watching your movie, go in the room "+room+", the " +date
                    + Newligne + "UGC ECE wishes you an excellent movie. "
                    + Newligne + "You will be able to pay directly at the movie theatre."
                    + Newligne + Newligne + Newligne + "Your Movie Theater ");
            // Etape 3 : Envoyer le message
            Transport.send(message);
            System.out.println("Message_envoye");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
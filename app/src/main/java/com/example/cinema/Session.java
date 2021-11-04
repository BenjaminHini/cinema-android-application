package com.example.cinema;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

public class Session {
    private final int m_id;
    private final int m_idMovie;
    private final int m_idRoom;
    private final Date m_date;
    private final float m_price;

    //Une seance de cinéma contient un ID, un ID de film, un ID de room, une date, et un prix
    public Session(int id, int idMovie, int idRoom, Date date, float price) {
        m_id = id;
        m_idMovie = idMovie;
        m_idRoom = idRoom;
        m_date = date;
        m_price = price;
    }

    public Session(int id, int idMovie, int idRoom, String dt, float price) throws ParseException {
        m_id = id;
        m_idMovie = idMovie;
        m_idRoom = idRoom;
        m_date = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(dt);
        m_price = price;
    }

    public int getID() {
        return m_id;
    }

    public int getIDMovie() {
        return m_idMovie;
    }

    public int getRoom() {
        return m_idRoom;
    }

    public Date getDate() {
        return m_date;
    }

    public float getPrice() {
        return m_price;
    }
}


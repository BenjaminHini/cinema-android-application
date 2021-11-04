package com.example.cinema;

import java.sql.Time;

public class Movie {

    private final int m_id;
    private final String m_name;
    private final String m_director;
    private final String m_time;
    private final String url;
    //private int favorite;

    //Un movie possède un titre, un director, un ou plusieurs genre, un running time
    public Movie(int id, String name, String director, String time, String url) {
        m_id = id;
        m_name = name;
        m_director = director;
        m_time = time;
        this.url = url;
        //this.favorite = favorite;
    }

    public String getUrl() {
        return url;
    }

    //public int getFavorite(){return favorite;}

    public int getID() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public String getDirector() {
        return m_director;
    }

    public String getTime() {
        return m_time;
    }

    //public void setFavorite(int favorite){this.favorite = favorite;}

    public void display() {
        System.out.println(m_id + "\n" + m_name + "\n" + m_director + "\n" + m_time.toString());
    }

    @Override
    public String toString() {
        return getName();
    }
}

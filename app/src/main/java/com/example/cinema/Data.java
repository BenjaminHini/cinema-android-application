package com.example.cinema;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;

import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Data extends SQLiteOpenHelper {

    private ArrayList<Session> sessionList = new ArrayList<>();

    private static final String DATABASE_NAME = "MyTheatre";

    private static final String TABLE_CUSTOMERS = "Customers";
    private static final String TABLE_MOVIE = "Movie";
    private static final String TABLE_SESSION = "Session";
    private static final String TABLE_COMMENTS = "Comments";

    private static final String TABLE_DROP = "drop table if exists " + TABLE_CUSTOMERS + " ; ";
    private static final String TABLE_DROP2 = "drop table if exists " + TABLE_MOVIE +";";
    private static final String TABLE_DROP3 = "drop table if exists " + TABLE_SESSION + " ; ";
    private static final String TABLE_DROP4 = "drop table if exists " + TABLE_COMMENTS + ";";

    private static final String ID = "id", BIRTHDAY = "birthday", FirstNAME = "firstName", LastNAME = "lastName", PASSWORD = "password", EMAIL = "email";
    private static final String DIRECTOR = "director", TIME = "time", NAME = "name", URL = "url";
    private static final String ID_MOVIE = "id_movie", ID_ROOM = "id_room",DATE = "date",PRICE = "price";
    private static final String COMMENT = "comment";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CUSTOMERS + "( id TEXT ," +
                    " birthday TEXT, firstName TEXT, lastName TEXT, password TEXT, email  TEXT PRIMARY KEY);";
    private static final String TABLE_CREATE2 =
            "CREATE TABLE " + TABLE_MOVIE + "( id TEXT ," +
                    " director TEXT, name TEXT PRIMARY KEY, time REAL, url TEXT);";
    private static final String TABLE_CREATE3 =
            "CREATE TABLE " + TABLE_SESSION + "( id TEXT ," +
                    " id_movie  TEXT, id_room TEXT, date TEXT, price REAL);";
    private static final String TABLE_CREATE4 =
            "CREATE TABLE " + TABLE_COMMENTS + "(name TEXT,email TEXT,comment TEXT);";

    public Data(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE2);
        db.execSQL(TABLE_CREATE3);
        db.execSQL(TABLE_CREATE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        db.execSQL(TABLE_DROP2);
        db.execSQL(TABLE_DROP3);
        db.execSQL(TABLE_DROP4);
        onCreate(db);
    }



    //Méthode qui permet d'ajouter un membre
    public void addMemberCustomer(String ID, String password, String email, String firstname, String lastname, Date birthday)throws  SQLException{

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues val = new ContentValues();
            val.put(this.ID, ID);
            val.put(this.FirstNAME, firstname);
            val.put(this.LastNAME, lastname);
            val.put(this.PASSWORD, password);
            val.put(this.EMAIL, email);
            val.put(this.BIRTHDAY, birthday.toString());
            if (db.insert(TABLE_CUSTOMERS, null, val) == -1) {
                throw new SQLException("an error has occured");
            }
            Log.i("succes", "succesfully inserted to database");
    }

    public boolean checkCustomer(String mail, String pass){
        Cursor res;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("select * from " + TABLE_CUSTOMERS + " where email = \"" + mail + "\"", null);
            if(res.getCount() == 0){return false;}
            while (res.moveToNext()){
                System.out.println(res.getString(4)+"=="+pass);
                if(res.getString(4).equals(pass))
                    return true;
            }
            return false;

        } catch (Exception e) {
            Log.e("Exception in getData", e.toString());
            return false;
        }
    }

    public void fillSessionLst(){
        Calendar date= Calendar.getInstance();
        int tomorrow = (date.get(Calendar.DAY_OF_WEEK)+22);
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), tomorrow, 14,00);
        Date date1 = date.getTime();
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), tomorrow, 17,30);
        Date date2 = date.getTime();
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), tomorrow, 21,00);
        Date date3 = date.getTime();

        //ArrayList des sessions du film
        //sessionList = new ArrayList<>();

        sessionList.add(new Session(1, 1, 1,date1, 10));
        sessionList.add(new Session(2, 1, 1,date2, 10));
        sessionList.add(new Session(3, 1, 1,date3, 10));
        sessionList.add(new Session(4, 2, 2,date1, 10));
        sessionList.add(new Session(5, 2, 2,date2, 10));
        sessionList.add(new Session(6, 2, 2,date3, 10));
        sessionList.add(new Session(7, 3, 3,date1, 10));
        sessionList.add(new Session(8, 3, 3,date2, 10));
        sessionList.add(new Session(9, 3, 3,date3, 10));
        sessionList.add(new Session(10, 4, 4,date1, 10));
        sessionList.add(new Session(11, 4, 4,date2, 10));
        sessionList.add(new Session(12, 4, 4,date3, 10));

    }

    public void setSessions(){
        fillSessionLst();
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            for (Session s:
                    sessionList) {
                ContentValues cv = new ContentValues();
                cv.put(ID, s.getID());
                cv.put(ID_MOVIE, s.getIDMovie());
                cv.put(ID_ROOM, s.getRoom());
                cv.put(DATE, s.getDate().toString());
                cv.put(PRICE, s.getPrice());
                db.insert(TABLE_SESSION, null, cv);
            }
        }
        catch (SQLException e){

        }

    }

    public ArrayList<Session> getSessions(int id) {

        fillSessionLst();
        ArrayList<Session> list = new ArrayList<>();

        for(int i =0; i<sessionList.size(); ++i)
            if(sessionList.get(i).getIDMovie() == id)
                list.add(sessionList.get(i));

        return list;
    }

    public  ArrayList<Movie> loadMovies(){
        ArrayList<Movie> movies = new ArrayList<>();

        movies.add(new Movie(1, "OSS117 : Cairo, Nest of Spies ", "Michel Hazanavicius", "1h39m", "dw0tlOdn5Mc"));
        movies.add(new Movie(2, "Pokemon : Detective Pikachu ", "Rob Letterman", "1h45m", "1roy4o4tqQM"));
        movies.add(new Movie(3, "Sonic : The Hedgehog ", "Jeff Fowler", "1h40m", "4mW9FE5ILJs"));
        movies.add(new Movie(4, "Forrest Gump ", "Robert Zemeckis", "2h22m", "bLvqoHBptjg"));



        SQLiteDatabase db = getWritableDatabase();
        for(int i = 0; i<movies.size(); ++i) {
            try {
                ContentValues cv = new ContentValues();
                cv.put(ID, movies.get(i).getID());
                cv.put(NAME, movies.get(i).getName());
                cv.put(DIRECTOR, movies.get(i).getDirector());
                cv.put(TIME, movies.get(i).getTime());
                cv.put(URL, movies.get(i).getUrl());
                db.insert(TABLE_MOVIE, null, cv);
            }
            catch (SQLException e){}
        }
        return  movies;
    }

    public  void AddCol(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(" alter table "+ TABLE_MOVIE +" add "+URL+" TEXT");
            Log.i("SUCCESS", "table added");
        }
        catch (SQLException e){
            Log.e("ERROR", e.toString());
        }
    }

    public int getCustomerAge(String email){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor curs = db.rawQuery(" select * from "+TABLE_CUSTOMERS +" where email = "+ email, null);
            String birthday = curs.getString(1);
            Date date = new  SimpleDateFormat("dd/MM/yyyy").parse(birthday);
            return Calendar.getInstance().get(Calendar.YEAR) - date.getYear();
        }
        catch (Exception e){
            return  -1;
        }
    }
    //" select * from "+TABLE_SESSION+" where id_movie = "+movie_id+" and date = '"+date+"';"
    public String getRooomnb(int movie_id, String date){
        try {
            date = date.substring(0, date.length()-3);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor curs = db.rawQuery( " select * from "+TABLE_SESSION+" where id_movie = '"+movie_id+"';", null);
            while (curs.moveToNext()) {
                if(curs.getString(3).contains(date))
                    return curs.getString(2);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return movie_id+"";
    }

    public ArrayList<String> getAllComments(String movie){
        ArrayList<String> coms = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor curs = db.rawQuery("select email, comment from "+ TABLE_COMMENTS +" where name = '" +
                    movie+"';", null);
            while (curs.moveToNext()){
                coms.add( curs.getString(0)+" :\t"+curs.getString(1));
            }
        }
        catch (SQLException e ){}

        return  coms;
    }

    public  void addComment(String movie, String email, String com)throws  SQLException{
        SQLiteDatabase db = getWritableDatabase();
        String username = email;
        try {
            Cursor curs = db.rawQuery("select id from "+TABLE_CUSTOMERS+" where email = '"+email+"';" , null);
            while (curs.moveToNext())
                username = curs.getString(0);
        }
        catch (IllegalStateException e){

        }

        ContentValues cv = new ContentValues();
        cv.put(this.NAME, movie);
        cv.put(this.EMAIL, username);
        cv.put(this.COMMENT , com);
        if(db.insert(TABLE_COMMENTS, null, cv) == -1)
            throw new SQLException("couldnt add comment");

    }
}

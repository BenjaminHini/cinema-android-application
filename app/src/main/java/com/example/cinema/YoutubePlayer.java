package com.example.cinema;


import android.content.Intent;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class YoutubePlayer extends YouTubeBaseActivity {

    private YouTubePlayerView youtube_player;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private static final String API_KEY = "AIzaSyDeGZyfpUZM0pRtvjFgv2wGnoAPR9HXl0k";
    private Button button;
    private EditText comment;
    private ListView commentSection;
    private  String email, movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        final Intent intent = getIntent();
        String URL = intent.getStringExtra("url");
        email = intent.getStringExtra("name");
        movie = intent.getStringExtra("movie");

        button = (Button)findViewById(R.id.sendComment_btn);
        comment = (EditText) findViewById(R.id.writeComment_txt);
        //commentSection = (TextView)findViewById(R.id.comment_view);

        getComments();

        youtube_player = (YouTubePlayerView)findViewById(R.id.youtube_play);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(URL);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youtube_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtube_player.initialize(API_KEY, onInitializedListener);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Data mysql = new Data(getApplicationContext());
                    mysql.addComment(movie, email, comment.getText().toString());
                    mysql.close();
                    comment.setText("");
                    getComments();
                }
                catch (SQLException ex){
                }

            }
        });
    }

    public void getComments(){
        Data mysql = new Data(getApplicationContext());
        ArrayList<String> comms = mysql.getAllComments(movie);
        showComments(comms);
        mysql.close();
    }

    public void showComments(ArrayList<String> comms){

        if(comms.isEmpty())
            comms.add("no comment");

        String[] columns = new String[] {"_id", "col"};
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        for (int i =0; i<comms.size(); ++i)
            matrixCursor.addRow(new  Object[]{i, comms.get(i)});

        String [] from = new  String[]{"col"};
        int [] to = new  int[]{R.id.col};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.table, matrixCursor, from, to, 0);
        commentSection = (ListView) findViewById(R.id.lv);
        commentSection.setAdapter(adapter);
    }
}
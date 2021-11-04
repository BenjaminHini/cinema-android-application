package com.example.cinema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraint;
    TextView view;
    Button login, register;
    ImageView imag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data(getApplicationContext());
        data.setSessions();

        constraint = (ConstraintLayout)findViewById(R.id.constraintLayout);
        view = (TextView)findViewById(R.id.textView);
        login = (Button)findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        imag = (ImageView)findViewById(R.id.imageView2);

        register.setOnClickListener(v -> {
            constraint.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);
            register.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            imag.setVisibility(View.INVISIBLE);
            RegisterFragment frag = new RegisterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        });
        login.setOnClickListener(v -> {
            constraint.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);
            register.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            imag.setVisibility(View.INVISIBLE);
            LoginFragment frag = new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
}
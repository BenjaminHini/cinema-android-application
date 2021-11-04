package com.example.cinema;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.GetChars;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String email;
    private String mParam2;
    private static Intent intent;

    ArrayList<Movie> list;
    TextView name_author;
    TextView time_movie;
    ImageView image;
    Spinner mySpinner;
    Button watch;
    int curr = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String email) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(getActivity(), YoutubePlayer.class);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v =inflater.inflate(R.layout.fragment_home, container, false);

         Data mysql = new Data(getActivity());
         list = mysql.loadMovies();
        Button next = (Button)v.findViewById(R.id.button2);
        name_author = (TextView)v.findViewById(R.id.name_author);
        image = (ImageView)v.findViewById(R.id.imageView);
        time_movie = (TextView)v.findViewById(R.id.textView22);
        mySpinner = (Spinner) v.findViewById(R.id.myspinner);
        watch = (Button)v.findViewById(R.id.watch);
        TextView nb = (TextView)v.findViewById(R.id.editTextDate);
        Button pay = (Button)v.findViewById(R.id.payment_btn);
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("url", list.get(curr).getUrl());
                intent.putExtra("name", email);
                intent.putExtra("movie", list.get(curr).getName());
                startActivity(intent);
            }
        });
        Button back = (Button)v.findViewById(R.id.button3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr++;
                if(curr>list.size()-1)
                    curr =0;
                nextMovie();
                updateSpinner();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr--;
                if(curr<0)
                    curr = list.size() -1;
                nextMovie();
                updateSpinner();
            }
        });

        nb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    int number = Integer.parseInt(s.toString());
                    pay.setEnabled(true);
                }catch (NumberFormatException e){
                    pay.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double price = getPrice(Integer.parseInt(nb.getText().toString()));
                String room = mysql.getRooomnb(list.get(curr).getID(), mySpinner.getSelectedItem().toString());
                Payment frag = Payment.newInstance(price, email, room ,mySpinner.getSelectedItem().toString(), list.get(curr).getName());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
            }
        });
        nextMovie();
        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateSpinner();

    }

    public void nextMovie(){
        name_author.setText("Director : \n"+list.get(curr).getDirector());
        time_movie.setText("Time : \n"+list.get(curr).getTime().toString());

        String imagess = "R.drawable.a"+list.get(curr).getID()+".jpg";
        switch (list.get(curr).getID()){
            case 1:
                image.setImageResource(R.drawable.oss117);
                break;
            case 2:
                image.setImageResource(R.drawable.a2);
                break;
            case 3:
                image.setImageResource(R.drawable.a3);
                break;
            case 4:
                image.setImageResource(R.drawable.a4);
                break;
            default:
                break;
        }
    }

    public  void updateSpinner(){
        Data data = new Data(getActivity());
        ArrayList<Session> sess = data.getSessions(list.get(curr).getID());
        String []items = new  String[sess.size()];
        for(int i =0; i<items.length; ++i)
            items[i] = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(sess.get(i).getDate());
        mySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items));
    }


    public double getPrice(int nb) {
        Data data = new Data(getActivity());
        ArrayList<Session> sess = data.getSessions(list.get(curr).getID());
        try {
            double price = sess.get(0).getPrice();
            price *= nb;
            int age = data.getCustomerAge(email);
            if (age > 0 && age <= 12)
                return price *= 0.7;
            else if (age <= 25)
                return price *= 0.85;
            else
                return price;
        } catch (Exception e) {
            e.printStackTrace();
            return 12.5;
        }
    }


}
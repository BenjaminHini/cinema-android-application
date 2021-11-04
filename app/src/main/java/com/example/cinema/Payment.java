package com.example.cinema;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Payment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    private Button finish;
    private TextView name;
    private TextView number_card;
    private TextView expire;
    private TextView secret;

    // TODO: Rename and change types of parameters
    private double mParam1;
    private String mParam2;
    private String room;
    private String date;
    private  String movie_name;

    public Payment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1
     * @return A new instance of fragment Payment.
     */
    // TODO: Rename and change types and number of parameters
    public static Payment newInstance(double param1, String param2, String room, String date, String name) {
        Payment fragment = new Payment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, room);
        args.putString(ARG_PARAM4, date);
        args.putString(ARG_PARAM5, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            room = getArguments().getString(ARG_PARAM3);
            date = getArguments().getString(ARG_PARAM4);
            movie_name = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_payment, container, false);
        finish = (Button)v.findViewById(R.id.finish);
        name = (TextView)v.findViewById(R.id.editTextTextPersonName);
        number_card = (TextView)v.findViewById(R.id.editTextNumber);
        TextView price = (TextView)v.findViewById(R.id.price_txt);
        price.setText("Price : "+mParam1+" €");
        expire = (TextView)v.findViewById(R.id.editTextDate2);
        secret = (TextView)v.findViewById(R.id.editTextNumber2);
        number_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 16){
                    number_card.setEnabled(false);
                    finish.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mail mail = new Mail(mParam2, room, mParam1+"", date, movie_name );
                Thread t = new Thread(mail);
                t.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Payment done, check your emails for details").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeFragment frag = HomeFragment.newInstance(mParam2);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                    }
                });
                builder.create().show();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
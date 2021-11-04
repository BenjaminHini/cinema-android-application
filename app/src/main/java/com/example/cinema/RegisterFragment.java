package com.example.cinema;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        Data mysql = new Data(getActivity());
        TextView first_name = (TextView)v.findViewById(R.id.first_name);
        TextView last_name = (TextView)v.findViewById(R.id.last_name);
        TextView mail = (TextView)v.findViewById(R.id.mail);
        TextView id = (TextView)v.findViewById(R.id.id);
        TextView date = (TextView)v.findViewById(R.id.date);
         date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datee = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datee.show();
            }
        });
        TextView password = (TextView)v.findViewById(R.id.password);
        Button register = (Button)v.findViewById(R.id.button);
        register.setOnClickListener(v1 -> {
            int code;
            if (password !=null && mail != null && first_name != null && last_name != null && date.getText() != null) {
                try {
                    System.out.println(date.getText().toString());
                    Date date2 = (Date) new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                    mysql.addMemberCustomer(id.getText().toString(), password.getText().toString(), mail.getText().toString(), first_name.getText().toString(), last_name.getText().toString(), date2);
                    HomeFragment frag = HomeFragment.newInstance(mail.getText().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You are already registered").setPositiveButton("OK", null).create().show();
                }
            }
        });
        return v;
    }
}
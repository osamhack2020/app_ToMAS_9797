package com.team9797.ToMAS.ui.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;


public class fixprofile extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fixprofile, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

        // Inflate the layout for this fragment
        EditText fix_name=root.findViewById(R.id.fix_name);
        EditText fix_birth=root.findViewById(R.id.fix_birth);
        EditText fix_anumber=root.findViewById(R.id.fix_anumber);
        EditText fix_belong=root.findViewById(R.id.fix_belong);
        EditText fix_email=root.findViewById(R.id.fix_email);
        EditText fix_pw=root.findViewById(R.id.fix_pw);
        EditText fix_ph=root.findViewById(R.id.fix_ph);
        EditText fix_class=root.findViewById(R.id.fix_class);
        Button button_fix=root.findViewById(R.id.button_fix);


        fix_name.setText(//안드로이드 머시기에서 가져오기);
        fix_birth.setText();
        fix_anumber.setText();
        fix_belong.setText();
        fix_email.setText();
        fix_pw.setText();
        fix_ph.setText();
        fix_class.setText();

        button_fix.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(fix_name)&&isEmpty(fix_birth)&&isEmpty(fix_anumber)&&isEmpty(fix_belong)&&isEmpty(fix_email)&&isEmpty(fix_pw)&&isEmpty(fix_ph)&&isEmpty(fix_class))
            }
        });







        return inflater.inflate(R.layout.fragment_fixprofile, container, false);
    }

    public boolean isvaildepw(){}
    public boolean isEmpty(){}




}
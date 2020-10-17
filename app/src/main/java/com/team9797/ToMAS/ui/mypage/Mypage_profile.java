package com.team9797.ToMAS.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.loginactivity;

public class Mypage_profile extends Fragment {

    MainActivity mainActivity;
    public FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        mainActivity = (MainActivity)getActivity();
        EditText mp_name = (EditText)root.findViewById(R.id.mp_name);
        EditText mp_email = (EditText)root.findViewById(R.id.mp_email);
        EditText mp_ph = (EditText)root.findViewById(R.id.mp_pn);
        EditText mp_anm = (EditText)root.findViewById(R.id.mp_anm);
        EditText mp_point = (EditText)root.findViewById(R.id.mp_point);
        EditText mp_level = (EditText)root.findViewById(R.id.mp_level);
        final EditText et_birth = (EditText)root.findViewById(R.id.et_birth);
        EditText et_belong = (EditText)root.findViewById(R.id.et_belong);
        EditText et_pw = (EditText)root.findViewById(R.id.et_pw);
        EditText et_class = (EditText)root.findViewById(R.id.et_class);







        //db에서 불러와서 텍스트지정 혹은 저장한곳에서 불러와서 텍스트지정


        final Button bt_birth=(Button)root.findViewById(R.id.bt_birth);
        bt_birth.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bt_context=bt_birth.getText().toString();

                    et_birth.isEnabled();

                // TODO : click event
            }
        });
        Button bt_pw=(Button)root.findViewById(R.id.bt_pw);
        bt_pw.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
            }
        });
        Button bt_belong=(Button)root.findViewById(R.id.bt_belong);
        bt_belong.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
            }
        });
        Button bt_class=(Button)root.findViewById(R.id.bt_birth);
        bt_class.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }

}
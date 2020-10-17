package com.team9797.ToMAS.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.loginactivity;

public class MypageFragment extends Fragment {

    MainActivity mainActivity;
    LinearLayout profile_container;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        profile_container = root.findViewById(R.id.profile_container);
        Button btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.sp_editor.clear();
                mainActivity.sp_editor.commit();

                FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent=new Intent(mainActivity,loginactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        profile_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment change_fragment = new fixprofile();
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
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
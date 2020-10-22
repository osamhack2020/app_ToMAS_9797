package com.team9797.ToMAS.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

public class SocialFragment extends Fragment {

    MainActivity mainActivity;
    TextView survey_textView;
    TextView board_big_textView;
    TextView board_small_textView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        
        //get Views
        survey_textView = root.findViewById(R.id.social_survey);
        board_big_textView = root.findViewById(R.id.social_notice_board_big);
        board_small_textView = root.findViewById(R.id.social_notice_board_small);

        survey_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new social_survey();
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        board_big_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new social_board("big");
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        board_small_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new social_board("small");
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
package com.team9797.ToMAS.ui.social;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.postBoard.register_board_content;
import com.team9797.ToMAS.ui.home.group.recruit_register_fragment;

public class social_survey extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.social_survey, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        fab = root.findViewById(R.id.social_survey_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, register_social_survey.class);
                startActivity(intent);
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
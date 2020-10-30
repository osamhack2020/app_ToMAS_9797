package com.team9797.ToMAS.ui.home.group;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

public class groupFragment extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_group, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        btn1 = (Button) root.findViewById(R.id.group_btn1);
        btn2 = (Button) root.findViewById(R.id.group_btn2);
        btn3 = (Button) root.findViewById(R.id.group_btn3);
        btn4 = (Button) root.findViewById(R.id.group_btn4);
        btn1.setOnClickListener(btn_group);
        btn2.setOnClickListener(btn_group);
        btn3.setOnClickListener(btn_group);
        btn4.setOnClickListener(btn_group);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.push_title("인원모집");
        mainActivity.set_title();
    }

    View.OnClickListener btn_group = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment change_fragment = null;
            fragmentTransaction = fragmentManager.beginTransaction();
            String title = null;
            Bundle args = null;
            switch (view.getId()) {
                case R.id.group_btn1:
                    change_fragment = new GroupExercise();
                    args = new Bundle();
                    title = "운동";
                    break;
                case R.id.group_btn2:
                    change_fragment = new GroupExercise();
                    args = new Bundle();
                    title = "동아리";
                    break;
                case R.id.group_btn3:
                    change_fragment = new GroupExercise();
                    args = new Bundle();
                    title = "대회";
                    break;
                case R.id.group_btn4:
                    change_fragment = new GroupExercise();
                    args = new Bundle();
                    title = "기타";
                    break;

            }
            args.putString("title", title);
            change_fragment.setArguments(args);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commitAllowingStateLoss();
        }
    };
}
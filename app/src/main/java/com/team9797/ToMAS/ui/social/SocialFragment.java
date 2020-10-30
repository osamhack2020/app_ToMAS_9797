package com.team9797.ToMAS.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.social.enroll_social_manager.enroll_social_manager;

import com.team9797.ToMAS.ui.social.survey.SocialSurvey;
import com.team9797.ToMAS.ui.social.social_board.SocialBoard;

public class SocialFragment extends Fragment {

    MainActivity mainActivity;
    TextView survey_textView;
    TextView board_big_textView;
    TextView board_small_textView;
    TextView enroll_social_manager;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

        path = mainActivity.preferences.getString("소속", "armyunit/5군단/5군단");
        String[] tmp = path.split("/");
        path = path.substring(0, path.length() - tmp[tmp.length - 1].length());
        mainActivity.db.collection(path + "설문조사")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            survey_textView.setText("현재 진행 중인 설문조사 : " +Integer.toString(task.getResult().size())+"건");
                        } else {
                        }
                    }
                });

        
        //get Views
        survey_textView = root.findViewById(R.id.social_survey);
        board_big_textView = root.findViewById(R.id.social_notice_board_big);
        board_small_textView = root.findViewById(R.id.social_notice_board_small);
        enroll_social_manager = root.findViewById(R.id.social_enroll_manager);

        survey_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new SocialSurvey();
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        board_big_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path.split("/").length == 2)
                {
                    Toast.makeText(mainActivity, "상위 부대가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    Fragment change_fragment = new SocialBoard("big");
                    fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
                }
            }
        });

        board_small_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new SocialBoard("small");
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        if (mainActivity.preferences.getString("권한", "").equals("부대관리자") || mainActivity.preferences.getString("권한", "").equals("관리자"))
        {
            enroll_social_manager.setVisibility(View.VISIBLE);
            enroll_social_manager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    Fragment change_fragment = new enroll_social_manager();
                    fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
                }
            });
        }

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mainActivity.push_title("소속부대");
        mainActivity.set_title();
    }
}
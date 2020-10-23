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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.social.survey.social_survey;

public class SocialFragment extends Fragment {

    MainActivity mainActivity;
    TextView survey_textView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

        path = "armyunit/" + mainActivity.preferences.getString("소속", "5군단/5군단");
        String[] tmp = path.split("/");
        path = path.substring(0, path.length() - tmp[tmp.length - 1].length());
        path += "설문조사";
        mainActivity.db.collection(path)
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

        survey_textView = root.findViewById(R.id.social_survey);
        survey_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new social_survey();
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
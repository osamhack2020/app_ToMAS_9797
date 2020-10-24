package com.team9797.ToMAS.ui.social.survey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.social.survey.register_social_survey;
import com.team9797.ToMAS.ui.social.survey.survey_content;
import com.team9797.ToMAS.ui.social.survey.survey_content_result;
import com.team9797.ToMAS.ui.social.survey.survey_list_adapter;

public class social_survey extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    ListView survey_listView;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton fab;
    String path;

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

        survey_listView = root.findViewById(R.id.social_survey_listView);

        final survey_list_adapter list_adapter = new survey_list_adapter();
        survey_listView.setAdapter(list_adapter);

        // firestore의 소속 path 내 설문조사 찾기
        path = mainActivity.preferences.getString("소속", "5군단/5군단/105정보통신단/105정보통신단");
        String[] tmp = path.split("/");
        path = path.substring(0, path.length() - tmp[tmp.length - 1].length());
        path += "설문조사";
        mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list_adapter.addItem(document.get("title").toString(), document.get("numpeople", Integer.class), document.get("due_date").toString(), document.get("writer").toString(), document.getId());
                    }
                    list_adapter.notifyDataSetChanged();

                    survey_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.addToBackStack(null);
                            Bundle args = new Bundle();
                            args.putString("post_id", list_adapter.listViewItemList.get(position).getId());
                            args.putString("path", path);
                            Fragment change_fragment;
                            if (mainActivity.preferences.getString("권한", "").equals("사용자"))
                            {
                                change_fragment = new survey_content();
                            }
                            else
                            {
                                change_fragment = new survey_content_result();
                            }
                            change_fragment.setArguments(args);
                            fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
                        }
                    });
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
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
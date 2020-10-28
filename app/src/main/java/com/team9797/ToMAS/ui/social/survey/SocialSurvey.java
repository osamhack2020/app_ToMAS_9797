package com.team9797.ToMAS.ui.social.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class SocialSurvey extends Fragment {

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
                Intent intent = new Intent(mainActivity, RegisterSocialSurvey.class);
                startActivityForResult(intent, 11114);
            }
        });

        survey_listView = root.findViewById(R.id.social_survey_listView);

        final SurveyListAdapter list_adapter = new SurveyListAdapter();
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
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<String> participants_list = new ArrayList<>();
                        mainActivity.db.collection(path).document(document.getId()).collection("participants").orderBy("timestamp", Query.Direction.DESCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if (task.isSuccessful()) {
                                   for (QueryDocumentSnapshot tmp_document : task.getResult()) {
                                        participants_list.add(tmp_document.getId());
                                   }
                               }
                               list_adapter.addItem(document.get("title").toString(), document.get("due_date").toString(), document.get("writer").toString(), participants_list.indexOf(mainActivity.getUid())>-1, document.getId());
                               list_adapter.notifyDataSetChanged();
                           }
                       });
                    }

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
                                change_fragment = new SurveyContent();
                            }
                            else
                            {
                                change_fragment = new SurveyContentResult();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 11114) && (resultCode == mainActivity.RESULT_OK)) {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }
}
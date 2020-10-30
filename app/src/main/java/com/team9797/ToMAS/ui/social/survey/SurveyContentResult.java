package com.team9797.ToMAS.ui.social.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

public class SurveyContentResult extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    String post_id;
    String path;
    Button all_result_btn;
    RecyclerView recyclerView;
    DocumentReference mPostReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.survey_content_result, container, false);

        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");

        // get View
        all_result_btn = root.findViewById(R.id.survey_all_result_btn);
        recyclerView = root.findViewById(R.id.survey_content_result_list);

        SurveyContentResultListAdapter adapter = new SurveyContentResultListAdapter(path, post_id, fragmentManager);

        // recycler view 사용하기
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);


        // 선택한 게시물 document reference
        mPostReference = mainActivity.db.collection(path).document(post_id);
        // id를 바탕으로

        mPostReference.collection("submissions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        adapter.add_item(document.getId());
                    }
                    adapter.notifyDataSetChanged();
                }
                else {

                }
            }
        });

        all_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment change_fragment = new SurveyContentResultTotal();
                fragmentTransaction.addToBackStack(null);
                Bundle args = new Bundle();
                args.putString("path", path);
                args.putString("post_id", post_id);
                change_fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        return root;
}


}
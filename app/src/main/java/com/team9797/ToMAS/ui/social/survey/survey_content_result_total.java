package com.team9797.ToMAS.ui.social.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class survey_content_result_total extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    String post_id;
    String path;
    TextView title_textView;
    TextView writer_textView;
    TextView due_date_textView;
    LinearLayout container_linearLayout;
    DocumentReference mPostReference;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.survey_content_result_individual, container, false);

        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");

        // get View
        title_textView = root.findViewById(R.id.survey_content_title);
        writer_textView = root.findViewById(R.id.survey_content_writer);
        due_date_textView = root.findViewById(R.id.survey_content_due_date);
        container_linearLayout = root.findViewById(R.id.survey_content_container);

        // 선택한 게시물 document reference
        mPostReference = mainActivity.db.collection(path).document(post_id);
        // id를 바탕으로
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title_textView.setText(document.get("title", String.class));
                        due_date_textView.setText(document.get("due_date", String.class));
                        writer_textView.setText(document.get("writer", String.class));
                        ArrayList<ArrayList<String>> submissions_result = new ArrayList<>();
                        mPostReference.collection("submissions").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean check = true;
                                        for (QueryDocumentSnapshot document_last : task.getResult()) {
                                            ArrayList<String> tmp_arrayList = (ArrayList<String>) document_last.get("answers");
                                            for (int i = 0; i< tmp_arrayList.size(); i++)
                                            {
                                                if (check)
                                                {
                                                    submissions_result.add(new ArrayList<>());
                                                }
                                                submissions_result.get(i).add(tmp_arrayList.get(i));
                                            }
                                            check = false;
                                        }

                                        mPostReference.collection("questions").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int count = 1;
                                                    for (QueryDocumentSnapshot tmp_document : task.getResult()) {
                                                        int tmp_type = tmp_document.get("type", Integer.class);
                                                        String item_question = tmp_document.get("question", String.class);
                                                        survey_content_total_result_customView tmp_customView;
                                                        if (tmp_type == 1)
                                                        {
                                                            ArrayList<String> choices_list = (ArrayList<String>) tmp_document.get("multi_choice_questions");
                                                            int[] num_choice = new int[choices_list.size()];
                                                            for (int i = 0; i < choices_list.size(); i++)
                                                                num_choice[i] = 0;
                                                            for (int i = 0; i < submissions_result.get(count-1).size(); i++)
                                                            {
                                                                num_choice[Integer.parseInt(submissions_result.get(count-1).get(i))]++;
                                                            }
                                                            tmp_customView = new survey_content_total_result_customView(mainActivity, null, tmp_type, count, item_question, choices_list, num_choice);
                                                        }
                                                        else
                                                        {
                                                            ArrayList<String> type2_answer_list = submissions_result.get(count-1);
                                                            tmp_customView = new survey_content_total_result_customView(mainActivity, null, tmp_type, count, item_question, null, type2_answer_list);
                                                        }
                                                        container_linearLayout.addView(tmp_customView, count + 1);
                                                        count++;
                                                    }
                                                }
                                                else {

                                                }
                                            }
                                        });


                                    } else {

                                    }
                                }
                            });


                    } else {

                    }
                } else {

                }
            }
        });
        return root;
}


}
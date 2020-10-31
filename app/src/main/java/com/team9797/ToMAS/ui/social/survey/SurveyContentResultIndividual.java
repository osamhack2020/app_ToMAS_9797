package com.team9797.ToMAS.ui.social.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class SurveyContentResultIndividual extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    String post_id;
    String path;
    String participant_id;
    TextView title_textView;
    TextView writer_textView;
    TextView due_date_textView;
    Button enroll_button;
    LinearLayout container_linearLayout;
    DocumentReference mPostReference;
    ArrayList<String> answers_list;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.survey_content_result_individual, container, false);

        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        participant_id = getArguments().getString("participant_id");
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");

        // get View
        title_textView = root.findViewById(R.id.survey_content_title);
        writer_textView = root.findViewById(R.id.survey_content_writer);
        due_date_textView = root.findViewById(R.id.survey_content_due_date);
        enroll_button = root.findViewById(R.id.survey_content_enroll_button);
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
                        title_textView.setText("제목 : " + document.get("title", String.class));
                        due_date_textView.setText("마감일 : " + document.get("due_date", String.class));
                        writer_textView.setText("작성자 : " + document.get("writer", String.class));
                        mPostReference.collection("submissions").document(participant_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot adocument = task.getResult();
                                    if (adocument.exists()) {

                                        answers_list = (ArrayList<String>) adocument.get("answers");

                                        mPostReference.collection("questions").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int count = 1;
                                                    for (QueryDocumentSnapshot tmp_document : task.getResult()) {
                                                        int tmp_type = tmp_document.get("type", Integer.class);
                                                        String item_question = tmp_document.get("question", String.class);
                                                        SurveyContentResultCustomView tmp_customView;
                                                        if (tmp_type == 1) {
                                                            tmp_customView = new SurveyContentResultCustomView(mainActivity, null, tmp_type, count, item_question, (ArrayList<String>) tmp_document.get("multi_choice_questions"), answers_list.get(count-1));
                                                        } else {
                                                            tmp_customView = new SurveyContentResultCustomView(mainActivity, null, tmp_type, count, item_question, null, answers_list.get(count-1));
                                                        }
                                                        container_linearLayout.addView(tmp_customView, count + 2);
                                                        count++;
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
                        return root;
                    }


                }
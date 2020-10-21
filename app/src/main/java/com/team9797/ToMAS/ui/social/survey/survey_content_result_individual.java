package com.team9797.ToMAS.ui.social.survey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.home.market.tender_participant_list_adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class survey_content_result_individual extends Fragment {

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
        Log.d("path : ", path);
        Log.d("post_id : ", post_id);
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("document : ", document.getId());
                    if (document.exists()) {
                        title_textView.setText(document.get("title", String.class));
                        due_date_textView.setText(document.get("due_date", String.class));
                        writer_textView.setText(document.get("writer", String.class));
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
                                                        survey_content_result_customView tmp_customView;
                                                        if (tmp_type == 1) {
                                                            tmp_customView = new survey_content_result_customView(mainActivity, null, tmp_type, count, item_question, (ArrayList<String>) tmp_document.get("multi_choice_questions"), answers_list.get(count-1));
                                                        } else {
                                                            tmp_customView = new survey_content_result_customView(mainActivity, null, tmp_type, count, item_question, null, answers_list.get(count-1));
                                                        }
                                                        container_linearLayout.addView(tmp_customView, count + 1);
                                                        count++;
                                                    }
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

                    } else {

                    }
                } else {

                }
            }
        });
                        return root;
                    }


                }
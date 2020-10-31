package com.team9797.ToMAS.ui.social.survey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.team9797.ToMAS.ui.home.group.RegisterGroupContent;

import java.util.ArrayList;

public class SurveyContentResultTotal extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    String post_id;
    String path;
    TextView title_textView;
    TextView writer_textView;
    TextView due_date_textView;
    Button show_unread;
    LinearLayout container_linearLayout;
    DocumentReference mPostReference;

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
        show_unread = root.findViewById(R.id.social_survey_show_unread);

        show_unread.setVisibility(View.VISIBLE);
        mPostReference = mainActivity.db.collection(path).document(post_id);
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title_textView.setText("제목 : " + document.get("title", String.class));
                        due_date_textView.setText("마감일 : " + document.get("due_date", String.class));
                        writer_textView.setText("작성자 : " + document.get("writer", String.class));
                        ArrayList<ArrayList<String>> submissions_result = new ArrayList<>();
                        mPostReference.collection("submissions").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean check = true;
                                        ArrayList<String> participate_list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document_last : task.getResult()) {
                                            participate_list.add(document_last.getId());
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
                                                        SurveyContentTotalResultCustomView tmp_customView;
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
                                                            tmp_customView = new SurveyContentTotalResultCustomView(mainActivity, null, tmp_type, count, item_question, choices_list, num_choice);
                                                        }
                                                        else
                                                        {
                                                            ArrayList<String> type2_answer_list = submissions_result.get(count-1);
                                                            tmp_customView = new SurveyContentTotalResultCustomView(mainActivity, null, tmp_type, count, item_question, null, type2_answer_list);
                                                        }
                                                        container_linearLayout.addView(tmp_customView, count + 2);
                                                        count++;
                                                    }
                                                }
                                            }
                                        });
                                        show_unread.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String people_path = path.substring(0, path.length() - 4) + "부대원";
                                                ArrayList<String> unread_list = new ArrayList<>();
                                                mainActivity.db.collection(people_path).get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        if (participate_list.indexOf(document.getId()) == -1) {
                                                                            unread_list.add(document.get("name").toString());
                                                                        }
                                                                    }
                                                                    final CharSequence[] items =  unread_list.toArray(new String[unread_list.size()]);
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                                                                    builder.setTitle("미제출자 명단");
                                                                    builder.setPositiveButton("확인",
                                                                            new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            });
                                                                        builder.setItems(items, new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int pos) {
                                                                            String selectedText = items[pos].toString();
                                                                            Toast.makeText(mainActivity, selectedText, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                    builder.show();
                                                                } else {
                                                                    //
                                                                }
                                                            }
                                                        });
                                            }
                                        });

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
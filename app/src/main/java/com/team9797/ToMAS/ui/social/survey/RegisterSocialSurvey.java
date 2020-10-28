package com.team9797.ToMAS.ui.social.survey;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterSocialSurvey extends AppCompatActivity {

    ArrayList<SurveyQuestionCustomView> question_list;
    FloatingActionButton fab;
    Button btn;
    LinearLayout container;
    EditText edit_date;
    EditText edit_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_social_survey);

        question_list = new ArrayList<>();
        fab = findViewById(R.id.social_survey_question_fab);
        btn = findViewById(R.id.social_survey_commit);
        container = findViewById(R.id.register_survey_customView_container);
        edit_date = findViewById(R.id.register_survey_edit_date);

        edit_date.setShowSoftInputOnFocus(false);
        edit_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterSocialSurvey.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month++;
                            edit_date.setText(year + "-" + month + "-"+day);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }

                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SurveyQuestionCustomView tmp_customView = new SurveyQuestionCustomView(RegisterSocialSurvey.this);
                tmp_customView.delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        question_list.remove(tmp_customView.index);
                        container.removeView(tmp_customView);
                        recount();
                    }
                });
                container.addView(tmp_customView);
                question_list.add(tmp_customView);
                recount();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버로 제출

                // firestore의 소속 부대 내 설문조사 경로 찾기
                SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                String path = preferences.getString("소속", "5군단/5군단/105정보통신단/105정보통신단");
                String[] tmp = path.split("/");
                path = path.substring(0, path.length() - tmp[tmp.length - 1].length());
                path += "설문조사";

                // get Views
                edit_title = findViewById(R.id.register_survey_title);
                String title = edit_title.getText().toString();


                Map<String, Object> post = new HashMap<>();
                post.put("title", title);
                post.put("timestamp", FieldValue.serverTimestamp());
                post.put("due_date", edit_date.getText().toString());
                post.put("writer", preferences.getString("이름", ""));
                post.put("numpeople", 0);

                DocumentReference documentReference = FirebaseFirestore.getInstance().collection(path).document();
                documentReference.set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d("AAA", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w("AAA", "Error writing document", e);
                            }
                        });
                for (int i = 0; i < question_list.size(); i++)
                {
                    Map<String, Object> tmp_question = new HashMap<>();
                    tmp_question.put("index", i+1);

                    if (question_list.get(i).radioButton1.isChecked())
                    {//객관식
                        tmp_question.put("type", 1);
                        ArrayList<String> tmp_multi_choice_item_list = new ArrayList<>();
                        Log.d("question number : " , Integer.toString(question_list.get(i).multi_chice_selection_list.size()));
                        for (int j = 0; j < question_list.get(i).multi_chice_selection_list.size(); j++)
                        {
                            tmp_multi_choice_item_list.add(question_list.get(i).multi_chice_selection_list.get(j).selection_editText.getText().toString());
                        }
                        tmp_question.put("multi_choice_questions", tmp_multi_choice_item_list);
                    }
                    else
                    {//주관식
                        tmp_question.put("type", 2);
                    }
                    tmp_question.put("question", question_list.get(i).question_editText.getText().toString());

                    documentReference.collection("questions").document().set(tmp_question)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Log.d("AAA", "DocumentSnapshot successfully written!");
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Log.w("AAA", "Error writing document", e);
                                }
                            });
                }

            }
        });


    }
    public void recount()
    {
        for (int i = 0 ; i< question_list.size(); i++)
        {
            question_list.get(i).index = i;
            question_list.get(i).num_textView.setText(Integer.toString(i + 1) + ".");
        }
    }
}
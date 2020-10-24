package com.team9797.ToMAS.ui.home.group;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recruit_register_fragment extends Fragment {

    String path;
    Spinner spinner_category;
    EditText edit_numpeople;
    MainActivity mainActivity;
    EditText edit_title;
    EditText edit_date;
    EditText edit_start_time;
    EditText edit_end_time;
    EditText edit_place;
    EditText edit_position;
    FragmentManager fragmentManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recruit_register, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        // view 받아오기
        edit_title = root.findViewById(R.id.recruit_register_title);
        edit_date = root.findViewById(R.id.recruit_register_date);
        edit_start_time = root.findViewById(R.id.recruit_register_start_time);
        edit_end_time = root.findViewById(R.id.recruit_register_end_time);
        edit_place = root.findViewById(R.id.recruit_register_place);
        edit_position = root.findViewById(R.id.recruit_register_position);

        // args 받아오기
        path = getArguments().getString("path");

        // edit_date 클릭 시 dialog나오게하고, 키보드는 숨기기.
        edit_date.setFocusable(false);
        edit_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {
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

        edit_start_time.setFocusable(false);
        edit_start_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    final Calendar calendar = Calendar.getInstance();
                    int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            edit_start_time.setText(String.format("%02d:%02d", hour, minute));
                        }
                    }, mHour, mMinute, true);
                    timePickerDialog.show();
                }

                return false;
            }
        });

        edit_end_time.setFocusable(false);
        edit_end_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    final Calendar calendar = Calendar.getInstance();
                    int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            edit_end_time.setText(String.format("%02d:%02d", hour, minute));
                        }
                    }, mHour, mMinute, true);
                    timePickerDialog.show();
                }

                return false;
            }
        });


        //카테고리 spinner 설정
        spinner_category = (Spinner) root.findViewById(R.id.recruit_register_category);
        ArrayAdapter category_adapter = ArrayAdapter.createFromResource(mainActivity, R.array.list_exercise, android.R.layout.simple_spinner_item);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_category.setAdapter(category_adapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //need to fix
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edit_numpeople = root.findViewById(R.id.recruit_register_num_people);

        Button btn_recruit_register = root.findViewById(R.id.btn_recruit_register);
        btn_recruit_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // 참가자 정보 넣은 map : 인원모집 신청자도 자동으로 등록.
                Map<String, Map<String, String>> participants = new HashMap<>();
                Map<String, String> tmp_participant = new HashMap<>();
                tmp_participant.put("name", mainActivity.preferences.getString("이름", "홍길동"));
                tmp_participant.put("phonenumber", mainActivity.preferences.getString("phonenumber", "01012341234"));
                tmp_participant.put("position", edit_position.getText().toString());
                participants.put(mainActivity.getUid(), tmp_participant);
                Map<String, Object> post = new HashMap<>();
                //example : need to fix
                post.put("title", edit_title.getText().toString());
                post.put("timestamp", FieldValue.serverTimestamp());
                post.put("date", edit_date.getText().toString());
                post.put("place", edit_place.getText().toString());
                post.put("category", spinner_category.getSelectedItem().toString());
                post.put("num_people", Integer.parseInt(edit_numpeople.getText().toString()));
                post.put("now_people", 1);
                post.put("participants", participants);
                post.put("time", edit_start_time.getText().toString() + " ~ " + edit_end_time.getText().toString()); //need to fix : 이것도 integer로 받아서 마감임박같은거 처리하면 좋을 듯


                db.collection(path).document()
                        .set(post)
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
                fragmentManager.beginTransaction().remove(recruit_register_fragment.this).commit();
                fragmentManager.popBackStack();
            }
        });

        return root;
    }

}
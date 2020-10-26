package com.team9797.ToMAS.ui.calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnRangeSelectedListener{


    MaterialCalendarView calendarView;
    MainActivity mainActivity;
    FloatingActionButton btn_calendar;
    TextView type_textView;
    TextView title_textView;
    TextView content_textView;
    RangeDayDecorator decorator;
    Button event_update;
    Button event_delete;
    List<CalendarDay> selected_day_list = new ArrayList<>();
    ArrayList<List<CalendarDay>> events = new ArrayList<>();
    HashMap<CalendarDay, event> day_to_event_map = new HashMap<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        mainActivity = (MainActivity)getActivity();
        calendarView = root.findViewById(R.id.calendarView);
        btn_calendar = root.findViewById(R.id.btn_calendar);
        event_update = root.findViewById(R.id.event_update);
        event_delete = root.findViewById(R.id.event_delete);
        decorator = new RangeDayDecorator(mainActivity);
        calendarView.setOnRangeSelectedListener(this);
        calendarView.addDecorator(decorator);
        //calendarview 크기 줄이기

        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("events")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // calendarDay를 firebase내에 저장 할 수 있는지 확인 후 적용
                        event tmp_event = new event(document.get("type").toString(), document.get("title").toString(), document.get("content").toString(), document.get("color", Integer.class), document.getId());
                        ArrayList<CalendarDay> tmp_list= (ArrayList<CalendarDay>)document.get("days");
                        for (int i = 0; i< tmp_list.size(); i++)
                        {
                            day_to_event_map.put(tmp_list.get(i), tmp_event);
                        }
                        events.add(selected_day_list);
                        //show_events(color);
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_event_dialog dialog = new add_event_dialog(mainActivity);
                dialog.show(mainActivity.getSupportFragmentManager(), "일정등록");
                dialog.setDialogResult(new add_event_dialog.add_event_dialog_result() {
                    @Override
                    public void get_result(String type, String title, String content, int color) {
                        // 여기서 서버로 보내기
                        Map<String, Object> post = new HashMap<>();
                        post.put("type", type);
                        post.put("title", title);
                        post.put("content", content);
                        post.put("color", color);
                        post.put("days", selected_day_list); // 이거 안되면 for문 돌려서 calendarDay 변환해서 firebase 넣기

                        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("events").document()
                                .set(post)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("AAA", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("AAA", "Error writing document", e);
                                    }
                                });
                    }
                });
            }
        });

        return root;
    }

    public void show_events()
    {
        for (int i = 0; i < events.size(); i++)
        {
            // 여기서 글자 추가하면 될 듯
            calendarView.addDecorator(new EventDecorator(day_to_event_map.get(events.get(i).get(0)).color, events.get(i)));
        }
        calendarView.invalidateDecorators();
    }

    @Override
    public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
        selected_day_list.clear();
        for (int i = 0; i < dates.size(); i++)
            selected_day_list.add(dates.get(i));
    }


    // range모드에서 하나만 클릭했을 때 리스너 통해서 미리보기 구현
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d("AA", "selected!!!");
        event selected_event = day_to_event_map.get(date);
        type_textView.setText(selected_event.type);
        title_textView.setText(selected_event.title);
        content_textView.setText(selected_event.content);

        // linearlayout hide 풀기 map에 event있는지 if 문 통해서 접근

        event_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_event_dialog dialog = new add_event_dialog(mainActivity, selected_event.type, selected_event.title, selected_event.content);
                dialog.show(mainActivity.getSupportFragmentManager(), "일정등록");
                dialog.setDialogResult(new add_event_dialog.add_event_dialog_result() {
                    @Override
                    public void get_result(String type, String title, String content, int color) {
                        // 여기서 서버로 보내기
                        Map<String, Object> post = new HashMap<>();
                        post.put("type", type);
                        post.put("title", title);
                        post.put("content", content);
                        post.put("color", color);
                        post.put("days", selected_day_list); // 이거 안되면 for문 돌려서 calendarDay 변환해서 firebase 넣기

                        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("events").document()
                                .set(post)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("AAA", "DocumentSnapshot successfully written!");
                                        refresh();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("AAA", "Error writing document", e);
                                    }
                                });
                    }
                });
            }
        });
        event_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.db.collection("user").document(mainActivity.getUid()).collection("events").document(selected_event.id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                refresh();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w("AAA", "Error writing document", e);
                            }
                        });
            }
        });

    }
    public void refresh()
    {
        mainActivity.getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}

class event{
    String type;
    String title;
    String content;
    int color;
    String id;

    event()
    {

    }
    event(String tmp_type, String tmp_title, String tmp_content, int tmp_color, String tmp_id)
    {
        type = tmp_type;
        title = tmp_title;
        content = tmp_content;
        color = tmp_color;
        id = tmp_id;
    }
}
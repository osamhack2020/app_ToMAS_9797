package com.team9797.ToMAS.ui.home.group;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class group_content extends Fragment {

    MainActivity mainActivity;
    TextView title_textView;
    TextView category_textView;
    TextView place_textView;
    TextView date_textView;
    TextView time_textView;
    TextView numpeople_textView;
    TextView enroll_date_textView;
    TextView writer_textView;
    EditText position_edit;
    Button btn_enroll ;
    GridView participant_gridView;
    SlidingUpPanelLayout slidingUpPanelLayout;
    Map<String, Map<String, String>> tmp_participants;
    DocumentReference mPostReference;

    String path;
    String post_id;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.group_content, container, false);

        // Init
        mainActivity = (MainActivity)getActivity();

        // get argument
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");

        // get View
        title_textView = root.findViewById(R.id.group_content_title);
        category_textView = root.findViewById(R.id.group_content_category);
        place_textView = root.findViewById(R.id.group_content_place);
        date_textView = root.findViewById(R.id.group_content_date);
        enroll_date_textView = root.findViewById(R.id.group_content_enroll_date);
        time_textView = root.findViewById(R.id.group_content_time);
        numpeople_textView = root.findViewById(R.id.group_content_numpeople);
        btn_enroll = root.findViewById(R.id.group_content_enroll);
        participant_gridView = root.findViewById(R.id.participant_list);
        position_edit = root.findViewById(R.id.group_content_position);
        writer_textView = root.findViewById(R.id.group_content_writer);
        slidingUpPanelLayout = root.findViewById(R.id.group_content_slidingLayout);

        final participant_list_adapter adapter = new participant_list_adapter();
        participant_gridView.setAdapter(adapter);

        // 선택한 게시물 document reference
        mPostReference = mainActivity.db.collection(path).document(post_id);
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title_textView.setText("제목 : " + document.get("title", String.class));
                        category_textView.setText("카테고리 : " + document.get("category", String.class));
                        place_textView.setText("모집장소 : " + document.get("place", String.class));
                        date_textView.setText("모집일 : " + document.get("date", String.class));
                        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
                        String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());
                        enroll_date_textView.setText("작성일 : " + dateString);
                        time_textView.setText("모집시간 : " + document.get("time", String.class));
                        writer_textView.setText("작성자 : " + document.get("writer").toString());
                        numpeople_textView.setText("참가인원 : " + Integer.toString(document.get("now_people", Integer.class))+"/" + Integer.toString(document.get("num_people", Integer.class)));
                        tmp_participants = (Map<String, Map<String, String>>)document.get("participants");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tmp_participants.forEach((key, value) -> adapter.addItem(value.get("name"), value.get("position"), value.get("phonenumber")));
                        }
                        adapter.notifyDataSetChanged();

                        /*
                        * 매번 user에서 불러와서 읽었을 때의 코드 수정 함.
                        for (int i = 0; i < tmp_participants.size(); i++)
                        {
                            mainActivity.db.collection("user").document(tmp_participants.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> tmp_task) {
                                    if (tmp_task.isSuccessful()) {
                                        DocumentSnapshot tmp_document = tmp_task.getResult();
                                        if (tmp_document.exists()) {
                                            adapter.addItem(tmp_document.get("계급").toString() + " " + tmp_document.get("이름").toString(), tmp_document.get("phonenumber").toString());
                                            adapter.notifyDataSetChanged();
                                        } else {

                                        }
                                    } else {

                                    }
                                }
                            });
                        }
                        */

                        // user id가 participation 목록에 있으면 버튼 text를 취소로 바꾸기.
                        if (tmp_participants.containsKey(mainActivity.getUid()))
                        {
                            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            btn_enroll.setText("참가취소");
                            slidingUpPanelLayout.setTouchEnabled(false);
                        }
                        else
                        {
                            slidingUpPanelLayout.setTouchEnabled(true);
                        }


                    } else {

                    }
                } else {

                }
            }
        });

        // 다른 곳 눌렀을 때 숨기게
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        btn_enroll.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // user ID를 통해 검색하고 list에 있으면 없애고, list에 없으면 추가하기)
                String mUid = mainActivity.getUid();
                if (tmp_participants.containsKey(mainActivity.getUid()))
                { // 이미 참가자에 uid가 있는 경우 : array에서 삭제
                    mPostReference.update("participants."+mUid , FieldValue.delete());
                    mPostReference.update("now_people", FieldValue.increment(-1));
                    // fragment 새로고침
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.detach(group_content.this).attach(group_content.this).commit();
                }
                else {
                    if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
                    {
                        Map<String, String> my_info = new HashMap<>();
                        my_info.put("name", mainActivity.preferences.getString("이름", "홍길동"));
                        my_info.put("phonenumber", mainActivity.preferences.getString("phonenumber", "01012341234"));
                        my_info.put("position", position_edit.getText().toString());
                        mPostReference.update("participants." + mUid, my_info);
                        mPostReference.update("now_people", FieldValue.increment(1));

                        // fragment 새로고침
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.detach(group_content.this).attach(group_content.this).commit();
                    }
                    else
                    {
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                }
            }
        });
        return root;
    }
}
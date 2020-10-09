package com.team9797.ToMAS.ui.home.group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

public class group_content extends Fragment {

    MainActivity mainActivity;
    TextView title_textView;
    TextView category_textView;
    TextView place_textView;
    TextView date_textView;
    TextView time_textView;
    TextView numpeople_textView;
    Button btn_enroll ;

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
        time_textView = root.findViewById(R.id.group_content_time);
        numpeople_textView = root.findViewById(R.id.group_content_numpeople);
        btn_enroll = root.findViewById(R.id.group_content_enroll);

        // 선택한 게시물 document reference
        DocumentReference mPostReference = mainActivity.db.collection(path).document(post_id);
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title_textView.setText(document.get("title", String.class));
                        category_textView.setText(document.get("category", String.class));
                        place_textView.setText(document.get("place", String.class));
                        date_textView.setText(document.get("date", String.class));
                        time_textView.setText(document.get("time", String.class));
                        numpeople_textView.setText(Integer.toString(document.get("now_people", Integer.class))+"/" + Integer.toString(document.get("num_people", Integer.class)));
                    } else {

                    }
                } else {

                }
            }
        });

        // user id가 participation 목록에 있으면 버튼 text를 취소로 바꾸기.
        /*
        * 예시 코드
        if (post.stars.indexOf(getUid())>-1) {
            //if (post.stars.containsKey(getUid())) {
                viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
            } else {
                viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
            }
         */

        // maybe : fragment 새로고침 해야 할 수도 있음.


        return root;
    }
    public void group_exercise_enroll(View view)
    {
        // user ID를 통해 검색하고 list에 있으면 없애고, list에 없으면 추가하기)
        //test

        // post 보내기
        // need to fix : participation list에 추가하는 법 검색해서 넣기
        /*
        mainActivity.db.collection(path).document()
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

         */
        //finish();
        // need to fix finish되서 돌아갈 때 게시판 리스트 최신화하기.
    }
}
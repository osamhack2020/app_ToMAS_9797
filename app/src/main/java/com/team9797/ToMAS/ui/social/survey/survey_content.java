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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class survey_content extends Fragment implements Html.ImageGetter {

    MainActivity mainActivity;
    //FragmentManager fragmentManager;
    String post_id;
    String path;
    TextView title_textView;
    TextView html_textView;
    TextView writer_textView;
    TextView category_textView;
    TextView place_textView;
    TextView date_textView;
    TextView due_date_textView;
    TextView highest_price_textView;
    ListView tender_listView;
    EditText price_editText;
    SlidingUpPanelLayout slidingUpPanelLayout;
    Button enroll_button;
    ArrayList<String> tmp_participants;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.survey_content, container, false);

        mainActivity = (MainActivity)getActivity();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");

        // get View
        title_textView = root.findViewById(R.id.survey_content_title);
        html_textView = root.findViewById(R.id.survey_content_html);
        writer_textView = root.findViewById(R.id.survey_content_writer);
        category_textView = root.findViewById(R.id.survey_content_category);
        due_date_textView = root.findViewById(R.id.survey_content_due_date);
        tender_listView = root.findViewById(R.id.market_content_tender_list);

        // 선택한 게시물 document reference
        DocumentReference mPostReference = mainActivity.db.collection(path).document(post_id);
        // id를 바탕으로
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // need to fix : 커스텀 객체 생성해서 받아보자.
                        html_textView.setText(Html.fromHtml(document.get("html", String.class), survey_content.this, null));
                        title_textView.setText(document.get("title", String.class));
                        category_textView.setText(document.get("category", String.class));
                        place_textView.setText(document.get("place", String.class));
                        date_textView.setText(document.get("date", String.class));
                        due_date_textView.setText(document.get("due_date", String.class));
                        writer_textView.setText(document.get("writer", String.class));
                        mPostReference.collection("participants").orderBy("price", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int tmp_counter = 1;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (tmp_counter == 1)
                                        {
                                            highest_price_textView.setText(Integer.toString(document.get("price", Integer.class)));
                                        }
                                        adapter.addItem(tmp_counter++, document.get("name").toString(), document.get("price", Integer.class));
                                        tmp_participants.add(document.getId());
                                    }
                                    // user id가 participation 목록에 있으면 버튼 text를 취소로 바꾸기.
                                    if (tmp_participants.indexOf(mainActivity.getUid()) > -1)
                                    {
                                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                        enroll_button.setText("입찰취소");
                                        slidingUpPanelLayout.setTouchEnabled(false);
                                    }
                                    else
                                    {
                                        slidingUpPanelLayout.setTouchEnabled(true);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {

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
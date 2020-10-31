package com.team9797.ToMAS.ui.home.market;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarketContent extends Fragment {

    MainActivity mainActivity;
    //FragmentManager fragmentManager;
    String post_id;
    String path;
    TextView title_textView;
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
    Editor renderer;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.market_content, container, false);

        mainActivity = (MainActivity)getActivity();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");
        //fragmentManager = getFragmentManager();

        // get View
        title_textView = root.findViewById(R.id.market_content_title);
        writer_textView = root.findViewById(R.id.market_content_writer);
        category_textView = root.findViewById(R.id.market_content_category);
        date_textView = root.findViewById(R.id.market_content_date);
        place_textView = root.findViewById(R.id.market_content_place);
        due_date_textView = root.findViewById(R.id.market_content_due_date);
        tender_listView = root.findViewById(R.id.market_content_tender_list);
        slidingUpPanelLayout = root.findViewById(R.id.market_content_sliding);
        enroll_button = root.findViewById(R.id.market_content_enroll_btn);
        price_editText = root.findViewById(R.id.market_content_tender_price);
        highest_price_textView = root.findViewById(R.id.market_content_highest_price);
        renderer = root.findViewById(R.id.renderer);

        tmp_participants = new ArrayList<>();
        TenderParticipantListAdapter adapter = new TenderParticipantListAdapter();
        tender_listView.setAdapter(adapter);

        highest_price_textView.setText("최고가 : 0");

        // 선택한 게시물 document reference
        DocumentReference mPostReference = mainActivity.db.collection(path).document(post_id);
        // id를 바탕으로
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<Integer, String> headingTypeface = getHeadingTypeface();
                        Map<Integer, String> contentTypeface = getContentface();
                        renderer.setHeadingTypeface(headingTypeface);
                        renderer.setContentTypeface(contentTypeface);
                        renderer.setDividerLayout(R.layout.tmpl_divider_layout);
                        renderer.setEditorImageLayout(R.layout.tmpl_image_view);
                        renderer.setListItemLayout(R.layout.tmpl_list_item);
                        renderer.setEditorListener(new EditorListener() {
                            @Override
                            public void onTextChanged(EditText editText, Editable text) {

                            }

                            @Override
                            public void onUpload(Bitmap image, String uuid) {

                            }

                            @Override
                            public View onRenderMacro(String name, Map<String, Object> settings, int index) {
                                return null;
                            }
                        });
                        renderer.render(document.get("html", String.class));
                        title_textView.setText(document.get("title", String.class));
                        category_textView.setText(document.get("category", String.class));
                        place_textView.setText(document.get("place", String.class));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());
                        date_textView.setText("등록일 : " + dateString);
                        due_date_textView.setText("마감일 : " + document.get("due_date", String.class));
                        writer_textView.setText(document.get("writer", String.class));
                        mPostReference.collection("participants").orderBy("price", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int tmp_counter = 1;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (tmp_counter == 1)
                                        {
                                            highest_price_textView.setText("최고가 : " + Integer.toString(document.get("price", Integer.class)));
                                        }
                                        adapter.addItem(tmp_counter++, document.get("name").toString(), document.get("price", Integer.class));
                                        tmp_participants.add(document.getId());
                                    }
                                    // user id가 participation 목록에 있으면 버튼 text를 취소로 바꾸기.
                                    if (tmp_participants.indexOf(mainActivity.getUid()) > -1)
                                    {
                                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                        enroll_button.setText("입찰취소");
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

        // 다른 곳 눌렀을 때 숨기게
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        enroll_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // user ID를 통해 검색하고 list에 있으면 없애고, list에 없으면 추가하기)
                String mUid = mainActivity.getUid();
                if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    if (tmp_participants.indexOf(mUid) > -1)
                    { // 이미 참가자에 uid가 있는 경우 : collection의 document에서 삭제
                        mPostReference.collection("participants").document(mainActivity.getUid()).delete();
                        mPostReference.update("numpeople", FieldValue.increment(-1));
                        Toast.makeText(mainActivity, "취소되었습니다", Toast.LENGTH_SHORT).show();
                        // fragment 새로고침
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.detach(MarketContent.this).attach(MarketContent.this).commit();
                    }
                    else {
                        mainActivity.db.collection("user").document(mainActivity.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        if (document.get("point", Integer.class) < Integer.parseInt(price_editText.getText().toString())) {
                                            Toast.makeText(mainActivity, "포인트가 부족합니다\n잔여 포인트 : " + Integer.toString(document.get("point", Integer.class)), Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            mPostReference.update("numpeople", FieldValue.increment(1));
                                            Map<String, Object> tender = new HashMap<>();
                                            //example : need to fix
                                            tender.put("name", mainActivity.preferences.getString("이름", "홍길동"));
                                            int tmp_price = Integer.parseInt(price_editText.getText().toString());
                                            tender.put("price", tmp_price);
                                            mPostReference.collection("participants").document(mainActivity.getUid())
                                                    .set(tender)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(mainActivity, "신청되었습니다", Toast.LENGTH_SHORT).show();
                                                            // fragment 새로고침
                                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            fragmentTransaction.detach(MarketContent.this).attach(MarketContent.this).commit();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
                else
                {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });

        // round로 자르기
        LinearLayout drawer = root.findViewById(R.id.market_content_drawer);
        drawer.setBackgroundResource(R.drawable.buttonshape_type1);
        drawer.setClipToOutline(true);

        return root;
    }

    public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }

    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL,"fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD,"fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC,"fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC,"fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }


}
//code from https://stackoverflow.com/questions/16179285/html-imagegetter-textview/16209680#16209680
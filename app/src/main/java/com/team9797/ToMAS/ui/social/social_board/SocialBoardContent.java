package com.team9797.ToMAS.ui.social.social_board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

public class SocialBoardContent extends Fragment{

    MainActivity mainActivity;
    String post_id;
    String path;
    boolean isRead;
    TextView title_textView;
    TextView writer_textView;
    TextView date_textView;
    Editor renderer;
    Button show_unread_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.social_board_content, container, false);

        mainActivity = (MainActivity)getActivity();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");
        isRead = getArguments().getBoolean("isRead");
        title_textView = root.findViewById(R.id.social_board_content_title);
        writer_textView = root.findViewById(R.id.social_board_content_writer);
        date_textView = root.findViewById(R.id.social_board_content_date);
        show_unread_btn = root.findViewById(R.id.social_board_show_unread);
        renderer = root.findViewById(R.id.renderer);

        // 선택한 게시물 document reference
        DocumentReference mPostReference = mainActivity.db.collection(path).document(post_id);
        // id를 바탕으로
        if (!isRead)
        {
            mPostReference.update("readers", FieldValue.arrayUnion(mainActivity.getUid()));
        }


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
                        title_textView.setText("제목 : " + document.get("title", String.class));
                        writer_textView.setText("작성자 : " + document.get("writer", String.class));
                        // get date from timestamp
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());
                        date_textView.setText("작성일 : " + dateString);
                        ArrayList<String> read_list = (ArrayList<String>)document.get("readers");
                        show_unread_btn.setOnClickListener(new View.OnClickListener() {
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
                                                if (read_list.indexOf(document.getId()) == -1)
                                                    unread_list.add(document.get("name").toString());
                                            }
                                            final CharSequence[] items =  unread_list.toArray(new String[unread_list.size()]);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                                            builder.setTitle("미확인자 명단");
                                            builder.setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        
                                                    }
                                                });
                                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int pos) {
                                                    String selectedText = items[pos].toString();
                                                    Toast.makeText(mainActivity, selectedText, Toast.LENGTH_SHORT).show();
                                                    // need to fix : 이거 누르면 사용자 정보 창 띄워서 확인하게
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
                    } else {

                    }
                } else {

                }
            }
        });


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
package com.team9797.ToMAS.PostBoard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.PostBoard.comment.CommentListAdapter;
import com.team9797.ToMAS.PostBoard.comment.RegisterBoardContentComment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class BoardContent extends Fragment{

    MainActivity mainActivity;
    String post_id;
    String path;
    TextView title_textView;
    Editor renderer;
    TextView writer_textView;
    TextView date_textView;
    TextView num_comments_textView;
    Button comment_button;
    Button delete_button;
    Button update_button;
    RecyclerView comment_recyclerView;
    String writer_id;
    FragmentManager fragmentManager;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.board_content, container, false);

        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");
        title_textView = root.findViewById(R.id.board_content_title);
        comment_button = root.findViewById(R.id.board_content_add_comment);
        comment_recyclerView = root.findViewById(R.id.board_content_comments_recylcerView);
        writer_textView = root.findViewById(R.id.board_content_writer);
        date_textView = root.findViewById(R.id.board_content_date);
        num_comments_textView = root.findViewById(R.id.board_content_num_comments);
        delete_button = root.findViewById(R.id.board_content_delete_btn);
        update_button = root.findViewById(R.id.board_content_update_btn);
        renderer = root.findViewById(R.id.renderer);


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
                        writer_textView.setText(document.get("writer", String.class));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());
                        date_textView.setText("작성일 : " + dateString);
                        num_comments_textView.setText("댓글 수 : " + Integer.toString(document.get("num_comments", Integer.class)));
                        writer_id = document.get("user_id", String.class);
                        if (writer_id.equals(mainActivity.getUid()))
                        {
                            update_button.setVisibility(View.VISIBLE);
                            update_button.setEnabled(true);
                            delete_button.setVisibility(View.VISIBLE);
                            delete_button.setEnabled(true);
                            update_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mainActivity, RegisterBoardContent.class);
                                    intent.putExtra("path", path);
                                    intent.putExtra("post_id", post_id);
                                    startActivityForResult(intent, 11112);
                                }
                            });
                            delete_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPostReference.collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                mPostReference.collection("comments").document(document.getId()).delete();
                                            }
                                            mPostReference.delete();
                                        } else {
                                            //
                                        }
                                    }
                                });
                                fragmentManager.beginTransaction().remove(BoardContent.this).commit();
                                fragmentManager.popBackStack();
                            }
                        });
                        }
                    } else {

                    }
                } else {

                }
            }
        });
        CommentListAdapter adapter = new CommentListAdapter(path, post_id, mainActivity, mainActivity.getUid(), writer_id);

        comment_recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        comment_recyclerView.setAdapter(adapter);
        mPostReference.collection("comments").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());
                                adapter.add_item(document.get("title", String.class), document.get("writer", String.class), dateString, document.get("html", String.class), document.getId(), document.get("user_id", String.class));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            //
                        }
                    }
                });
        mPostReference.update("clicks", FieldValue.increment(1));

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RegisterBoardContentComment.class);
                intent.putExtra("path", path);
                intent.putExtra("post_id", post_id);
                startActivityForResult(intent, 11112);
            }
        });

        //화면제대로 안보이면 fragment 업데이트 해야 할 수도 있음.

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 11112) && (resultCode == mainActivity.RESULT_OK)) {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
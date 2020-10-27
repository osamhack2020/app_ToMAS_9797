package com.team9797.ToMAS.postBoard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.team9797.ToMAS.postBoard.comment.comment_list_adapter;
import com.team9797.ToMAS.postBoard.comment.register_board_content_comment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class board_content extends Fragment implements Html.ImageGetter {

    MainActivity mainActivity;
    String post_id;
    String path;
    TextView title_textView;
    TextView html_textView;
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
        html_textView = root.findViewById(R.id.board_content_textview);
        comment_button = root.findViewById(R.id.board_content_add_comment);
        comment_recyclerView = root.findViewById(R.id.board_content_comments_recylcerView);
        writer_textView = root.findViewById(R.id.board_content_writer);
        date_textView = root.findViewById(R.id.board_content_date);
        num_comments_textView = root.findViewById(R.id.board_content_num_comments);
        delete_button = root.findViewById(R.id.board_content_delete_btn);
        update_button = root.findViewById(R.id.board_content_update_btn);


        // 선택한 게시물 document reference
        DocumentReference mPostReference = mainActivity.db.collection(path).document(post_id);
        // id를 바탕으로
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        html_textView.setText(Html.fromHtml(document.get("html", String.class), board_content.this, null));
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
                                    Intent intent = new Intent(mainActivity, register_board_content.class);
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
                                                //Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                                    fragmentManager.beginTransaction().remove(board_content.this).commit();
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
        comment_list_adapter adapter = new comment_list_adapter(path, post_id, mainActivity, mainActivity.getUid());

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
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        mPostReference.update("clicks", FieldValue.increment(1));

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, register_board_content_comment.class);
                intent.putExtra("path", path);
                intent.putExtra("post_id", post_id);
                startActivityForResult(intent, 11112);
            }
        });

        //화면제대로 안보이면 fragment 업데이트 해야 할 수도 있음.

        return root;
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = html_textView.getText();
                html_textView.setText(t);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 11112) && (resultCode == mainActivity.RESULT_OK)) {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
    }

}
//code from https://stackoverflow.com/questions/16179285/html-imagegetter-textview/16209680#16209680
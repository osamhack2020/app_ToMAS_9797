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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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

public class social_board_content extends Fragment implements Html.ImageGetter {

    MainActivity mainActivity;
    String post_id;
    String path;
    TextView title_textView;
    TextView html_textView;
    TextView writer_textView;
    TextView date_textView;
    Button show_unread_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.social_board_content, container, false);

        mainActivity = (MainActivity)getActivity();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");
        isRead = getArguments().getBoolean("isRead");
        title_textView = root.findViewById(R.id.social_board_content_title);
        html_textView = root.findViewById(R.id.social_board_content_textview);
        writer_textView = root.findViewById(R.id.social_board_content_writer);
        date_textView = root.findViewById(R.id.social_board_content_date);
        show_unread_btn = root.findViewById(R.id.social_board_show_unread);

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
                        html_textView.setText(Html.fromHtml(document.get("html", String.class), social_board_content.this, null));
                        title_textView.setText(document.get("title", String.class));
                        writer_textView.setText(document.get("writer", String.class));
                        // get date from timestamp
                        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
                        String dateString = formatter.format(new Date(Long.parseLong(document.get("timestamp", Long.class))));
                        date_textView.setText(dateString);
                        ArrayList<String> read_list = (ArrayList<String>)document.get("readers");
                        show_unread_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String people_path = path.substring(0, path.length - 4) + "부대원";
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
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
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
}
//code from https://stackoverflow.com/questions/16179285/html-imagegetter-textview/16209680#16209680
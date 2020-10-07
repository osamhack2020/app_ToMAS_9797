package com.team9797.ToMAS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class board_content extends Fragment implements Html.ImageGetter {

    MainActivity mainActivity;
    //FragmentManager fragmentManager;
    String post_id;
    String path;
    TextView title_textView;
    TextView html_textView;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.board_content, container, false);

        mainActivity = (MainActivity)getActivity();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");
        //fragmentManager = getFragmentManager();
        title_textView = root.findViewById(R.id.board_content_title);
        html_textView = root.findViewById(R.id.board_content_textview);


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
                        Log.d("QQQ", document.get("html", String.class));
                        html_textView.setText(Html.fromHtml(document.get("html", String.class), board_content.this, null));
                    } else {

                    }
                } else {

                }
            }
        });
        mPostReference.update("clicks", FieldValue.increment(1));

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
            Log.d("QQ", "doInBackground " + source);
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
            Log.d("QQ", "onPostExecute drawable " + mDrawable);
            Log.d("QQ", "onPostExecute bitmap " + bitmap);
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
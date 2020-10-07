package com.team9797.ToMAS;

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

public class board_content extends Fragment {

    MainActivity mainActivity;
    //FragmentManager fragmentManager;
    String post_id;
    String path;
    TextView title_textView;
    WebView html_webView;

    // need to fix 댓글 보기 기능 추가해야 함.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.board_content, container, false);

        mainActivity = (MainActivity)getActivity();
        post_id = getArguments().getString("post_id");
        path = getArguments().getString("path");
        //fragmentManager = getFragmentManager();
        title_textView = root.findViewById(R.id.board_content_title);
        html_webView = root.findViewById(R.id.board_content_webview);

        html_webView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        html_webView.getSettings().setJavaScriptEnabled(true);

        if(Build.VERSION.SDK_INT >= 21) {
            html_webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

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
                        html_webView.loadData(document.get("html", String.class), "text/html; charset=utf-8", "UTF-8");
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
}

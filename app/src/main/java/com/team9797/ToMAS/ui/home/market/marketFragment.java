package com.team9797.ToMAS.ui.home.market;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.home.market.belong_tree.belong_tree_dialog;

import org.w3c.dom.Text;

public class marketFragment extends Fragment{
    MainActivity mainActivity;
    belong_tree_dialog tree_dialog;
    FragmentManager fragmentManager;
    String path;
    ListView market_listView;
    TextView tree_textView;
    market_sample_list_adapter list_adapter;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_market, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

        market_listView = root.findViewById(R.id.market_list);
        tree_textView = root.findViewById(R.id.fragment_market_tree_text);
        default_setting();
        tree_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tree_dialog = new belong_tree_dialog(mainActivity);
                tree_dialog.show(fragmentManager, "소속트리");
                tree_dialog.setDialogResult(new belong_tree_dialog.tree_dialog_result() {
                    @Override
                    public void get_result(String result) {
                        tree_textView.setText(getPath(result));
                        set_list();
                    }
                });
            }
        });

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }



    // 자기의 소속을 바탕으로 default setting 처리
    public void default_setting()
    {
        // sharedPreference에서 가져와서 넣어주기
        String tmp_path = mainActivity.preferences.getString("소속", "");
        tree_textView.setText(getPath(tmp_path));
        set_list();
    }

    public String getPath(String str)
    {
        String stringed_path = "";
        String[] tmp = str.split("/");
        String market_path = str.substring(0, str.length() - tmp[tmp.length - 1].length());
        market_path += "market";
        for (int i = 1; i<tmp.length; i++)
        {
            // document를 만들기 위해 tmp로 사용했던 path를 무시한다.
            if (i%2 == 0)
                stringed_path += tmp[i] + " ";
        }
        path = market_path;
        return stringed_path;
    }

    public void set_list()
    {
        // custom listview를 생성해서 만들어야됨.
        list_adapter = new market_sample_list_adapter(mainActivity, fragmentManager);
        market_listView.setAdapter(list_adapter);

        // firestore에서 market list 불러오기.
        mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tmp = document.getId();
                        Log.d("AAA", tmp);
                        final market_list_adapter tmp_sample_list_adapter = new market_list_adapter();
                        // firestore sample list 불러오기
                        String tmp_path = path + "/" + tmp + "/" + tmp;
                        mainActivity.db.collection(tmp_path).orderBy("timestamp", Query.Direction.DESCENDING).limit(5).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                mainActivity.db.collection(tmp_path).document(document.getId()).collection("participants").orderBy("price", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot price_document : task.getResult()) {
                                                                tmp_sample_list_adapter.addItem(document.get("title").toString(), document.get("numpeople", Integer.class), document.get("due_date").toString(), document.get("writer").toString(), price_document.get("price", Integer.class), document.getId());
                                                            }
                                                            tmp_sample_list_adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        list_adapter.addItem(tmp, tmp_sample_list_adapter, tmp_path);
                    }
                    list_adapter.notifyDataSetChanged();

                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
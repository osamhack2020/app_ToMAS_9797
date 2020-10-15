package com.team9797.ToMAS.ui.home.market;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.home.market.belong_tree.belong_tree_dialog;

import org.w3c.dom.Text;

public class marketFragment extends Fragment implements belong_tree_dialog.NoticeDialogListener{
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
                tree_dialog = new belong_tree_dialog();
                tree_dialog.show(fragmentManager, "소속트리");
            }
        });

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        path = tree_dialog.getPath();
        tree_textView.setText(path);
        set_list();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    // 자기의 소속을 바탕으로 default setting 처리
    public void default_setting()
    {
        // sharedPreference에서 가져와서 넣어주기
        path = "armyunit" + mainActivity.preferences.getString("소속", "5군단/105정보통신단/105정보통신단");
        tree_textView.setText(getPath(mainActivity.preferences.getString("소속", "5군단/105정보통신단/105정보통신단")));
        set_list();
    }

    public String getPath(String str)
    {
        String stringed_path = "";
        String[] tmp = str.split("/");
        for (int i = 0; i<tmp.length; i++)
        {
            stringed_path += tmp[i] + " ";
        }
        return stringed_path;
    }

    public void set_list()
    {
        // custom listview를 생성해서 만들어야됨.
        list_adapter = new market_sample_list_adapter(mainActivity, fragmentManager);
        market_listView.setAdapter(list_adapter);

        // firestore에서 market list 불러오기.
        path += "/market/market";
        mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tmp = document.getId();
                        final market_list_adapter tmp_sample_list_adapter = new market_list_adapter();
                        // firestore sample list 불러오기
                        mainActivity.db.collection(path + "/" + tmp + "/" + tmp)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            int count = 0;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (count > 5)
                                                    break;
                                                tmp_sample_list_adapter.addItem(document.get("title").toString(), document.get("numpeople", Integer.class), document.get("date").toString(), document.get("name").toString(), document.get("price", Integer.class), document.getId());
                                                count++;
                                            }
                                            tmp_sample_list_adapter.notifyDataSetChanged();
                                        } else {
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        list_adapter.addItem(tmp, tmp_sample_list_adapter, path);
                    }
                    list_adapter.notifyDataSetChanged();

                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
package com.team9797.ToMAS.ui.home.market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.postBoard.register_board_content;
import com.team9797.ToMAS.ui.home.group.recruit_register_fragment;
import com.team9797.ToMAS.ui.home.market.belong_tree.belong_tree_dialog;

public class market_category_fragment extends Fragment {
    MainActivity mainActivity;
    belong_tree_dialog dialog;
    FragmentManager fragmentManager;
    FloatingActionButton fab;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.market_category_fragment, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        path = getArguments().getString("path");
/*
        dialog = new belong_tree_dialog();
        dialog.show(fragmentManager, "dialog");
*/
        fab = root.findViewById(R.id.fab_market_category);
        final ListView market_listView = root.findViewById(R.id.market_category_list);

        // custom listview를 생성해서 만들어야됨.
        final market_list_adapter list_adapter = new market_list_adapter();
        market_listView.setAdapter(list_adapter);

        // firestore에서 market list 불러오기.
        mainActivity.db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_adapter.addItem(document.get("title").toString(), document.get("numpeople", Integer.class), document.get("due_date").toString(), document.get("writer").toString(), document.get("price", Integer.class), document.getId());
                            }
                            list_adapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // click함수에서 key값을 넘겨서 게시판 db에서 가져온 데이터를 넣어야 함.
        market_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new market_content();
                // 게시판 id와 path를 받아와서 board_content fragment로 넘긴다.
                // maybe 이거 구조를 검색해서 바꿔야 할 듯
                Bundle args = new Bundle();
                args.putString("post_id", list_adapter.listViewItemList.get(position).getId());
                args.putString("path", path);
                change_fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, register_market_content.class);
                String[] tmp = path.split("/");
                String parent_path = path.substring(0, path.length() - tmp[tmp.length - 1].length()*2 -2);
                intent.putExtra("path", parent_path);
                startActivity(intent);
            }
        });



        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }
}
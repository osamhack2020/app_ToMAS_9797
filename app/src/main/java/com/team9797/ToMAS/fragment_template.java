package com.team9797.ToMAS;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.postBoard.board_content;
import com.team9797.ToMAS.postBoard.board_list_adapter;
import com.team9797.ToMAS.postBoard.template2_list_adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class fragment_template extends Fragment {

    MainActivity mainActivity;
    int fragment_style;
    String title;
    String path;
    View root;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ArrayList<String> child_list = new ArrayList<String>();
    ArrayList<Integer> child_fragment_list = new ArrayList<>();
    LayoutInflater inflater;
    ViewGroup container;

    public void show_field()
    {
        switch (fragment_style)
        {
            case 1:
                root = inflater.inflate(R.layout.template1, container, false);
                ListView tmp_listview = (ListView)root.findViewById(R.id.template1_listView);
                final ArrayAdapter<String> listview_adapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, child_list); // simple_list_item layout를 바꿔야 style이 바뀜
                tmp_listview.setAdapter(listview_adapter);
                // 구조 다시 바꿈. fragment_template에서 child_list를 firebase에서 읽어옴.
                // firebase에서 child_list채우기
                mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        child_list.clear();
                        child_fragment_list.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            child_list.add(document.getId());
                            child_fragment_list.add(document.get("fragment_style", Integer.class));
                        }
                        listview_adapter.notifyDataSetChanged();
                    } else {
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
                tmp_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        // 다음 child를 만들고 arg 넘기는 과정
                        // need to fix db에서 받아와서 분기해야댐.
                        Fragment change_fragment = new fragment_template();
                        Bundle args = new Bundle();
                        args.putInt("fragment_style", child_fragment_list.get(i));
                        args.putString("title", child_list.get(i));
                        args.putString("path", path);
                        change_fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
                    }
                });
                break;
            case 2:
                root = inflater.inflate(R.layout.template2, container, false);
                final ListView template2_list = root.findViewById(R.id.template2_list);

                // custom listview를 생성해서 만들어야됨.
                final template2_list_adapter template2_adapter = new template2_list_adapter(mainActivity, fragmentManager);
                template2_list.setAdapter(template2_adapter);
                // fragment_style 2에서는 template2_list_adapter에서 클릭을 처리한다.

                // firestore에서 subject list 불러오기.
                mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String tmp = document.getId();
                                final board_list_adapter tmp_sample_list_adapter = new board_list_adapter();
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
                                                        Log.d("QQQ", document.get("title").toString());
                                                        tmp_sample_list_adapter.addItem(document.get("title").toString(), document.get("sub").toString(), document.get("date").toString(), document.get("name").toString(), document.get("clicks").toString(), document.getId());
                                                        count++;
                                                    }
                                                    tmp_sample_list_adapter.notifyDataSetChanged();
                                                } else {
                                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                template2_adapter.addItem(tmp, tmp_sample_list_adapter, path);
                            }
                            template2_adapter.notifyDataSetChanged();

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                break;
            case 3:
                root = inflater.inflate(R.layout.template3, container, false);
                ListView tmp3_listview = root.findViewById(R.id.template3_listView);
                final board_list_adapter adapter = new board_list_adapter();
                tmp3_listview.setAdapter(adapter);

                // need to fix addItem에 서버에서 받아온 db를 넣어야 함.
                mainActivity.db.collection(path)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    adapter.addItem(document.get("title").toString(), document.get("sub").toString(), document.get("date").toString(), document.get("name").toString(), document.get("clicks").toString(), document.getId());
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

                // click함수에서 key값을 넘겨서 게시판 db에서 가져온 데이터를 넣어야 함.
                tmp3_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        Fragment change_fragment = new board_content();
                        // 게시판 id와 path를 받아와서 board_content fragment로 넘긴다.
                        // maybe 이거 구조를 검색해서 바꿔야 할 듯
                        Bundle args = new Bundle();
                        args.putString("post_id", adapter.listViewItemList.get(position).getId());
                        args.putString("path", path);
                        change_fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
                    }
                });

                // fab버튼 관리
                FloatingActionButton fab = root.findViewById(R.id.fab_board_register);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mainActivity, register_board_content.class);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    }
                });

                break;
            default:
                break;
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        mainActivity = (MainActivity)getActivity();
        fragment_style = getArguments().getInt("fragment_style");
        title = getArguments().getString("title");
        path = getArguments().getString("path");
        path = path + "/" + title + "/" + title;
        fragmentManager = getFragmentManager();
        show_field();

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }

    public void refresh(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }
}
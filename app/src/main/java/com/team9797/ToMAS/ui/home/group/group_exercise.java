package com.team9797.ToMAS.ui.home.group;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.HashMap;
import java.util.Map;

public class group_exercise extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String title;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // init fragment
        View root = inflater.inflate(R.layout.group_exercise, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fab = root.findViewById(R.id.fab_group_exercise_register);
        recyclerView = root.findViewById(R.id.group_exercise_recyclerview);

        // argument 받아오기
        title = getArguments().getString("title");
        // 여기선 path 받아올 필요가 없음.
        path = "mainpage/인원모집/인원모집/운동/운동";

        final group_list_adapter adapter = new group_list_adapter(path, fragmentManager);

        // recycler view 사용하기
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);

        // need to fix addItem에 서버에서 받아온 db를 넣어야 함.
        mainActivity.db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                adapter.add_item(document.get("title").toString(), document.get("now_people", Integer.class), document.get("num_people", Integer.class), document.get("place").toString(), document.get("date").toString(), document.get("time").toString(), document.getId());
                                Log.d("QQ", document.get("title").toString());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // fab버튼 관리
        FloatingActionButton fab = root.findViewById(R.id.fab_group_exercise_register);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment change_fragment = new recruit_register_fragment();
                fragmentTransaction.addToBackStack(null);
                Bundle args = new Bundle();
                args.putString("path", path);
                change_fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });


        return root;
    }
}
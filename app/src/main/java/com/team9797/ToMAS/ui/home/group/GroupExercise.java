package com.team9797.ToMAS.ui.home.group;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.home.market.belong_tree.BelongTreeDialog;

public class GroupExercise extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView tree_textView;
    BelongTreeDialog tree_dialog;

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
        tree_textView = root.findViewById(R.id.tree_textView);

        // argument 받아오기
        title = getArguments().getString("title");

        tree_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tree_dialog = new BelongTreeDialog(mainActivity);
                tree_dialog.show(fragmentManager, "소속트리");
                tree_dialog.setDialogResult(new BelongTreeDialog.tree_dialog_result() {
                    @Override
                    public void get_result(String result) {
                        tree_textView.setText(getPath(result));
                        set_list();
                    }
                });
            }
        });
        default_setting();


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.push_title(title);
        mainActivity.set_title();
    }

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
        String tmp_path = str.substring(0, str.length() - tmp[tmp.length - 1].length());
        tmp_path += "인원모집/" + title + "/" + title;
        for (int i = 1; i<tmp.length; i++)
        {
            // document를 만들기 위해 tmp로 사용했던 path를 무시한다.
            if (i%2 == 0)
                stringed_path += tmp[i] + " ";
        }
        path = tmp_path;
        return stringed_path;
    }

    public void set_list()
    {
        final group_list_adapter adapter = new group_list_adapter(path, fragmentManager);

        // recycler view 사용하기
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);

        // need to fix addItem에 서버에서 받아온 db를 넣어야 함.
        mainActivity.db.collection(path).orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                adapter.add_item(document.get("title").toString(), document.get("now_people", Integer.class), document.get("num_people", Integer.class), document.get("place").toString(), document.get("date").toString(), document.get("time").toString(), document.getId());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // fab버튼 관리
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment change_fragment = new RegisterGroupContent();
                fragmentTransaction.addToBackStack(null);
                Bundle args = new Bundle();
                args.putString("path", path);
                change_fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });
    }
}
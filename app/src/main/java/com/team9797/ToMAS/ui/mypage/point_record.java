package com.team9797.ToMAS.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class point_record extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // init fragment
        View root = inflater.inflate(R.layout.group_exercise, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        recyclerView = root.findViewById(R.id.group_exercise_recyclerview);

        final point_record_list_adapter adapter = new point_record_list_adapter();

        // recycler view 사용하기
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);

        // need to fix addItem에 서버에서 받아온 db를 넣어야 함.
        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("point_record")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
                                String dateString = formatter.format(new Date(document.get("timestamp", Long.class)));
                                adapter.add_item(document.get("content").toString(), dateString, document.get("change").toString());
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        return root;
    }
}
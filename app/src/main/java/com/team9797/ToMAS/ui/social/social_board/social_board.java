package com.team9797.ToMAS.ui.social.social_board;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class social_board extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    String path;
    String union;

    public social_board()
    {

    }
    public social_board(String tmp_union)
    {
        union = tmp_union;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.social_board, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        
        //get Views
        recyclerView = root.findViewById(R.id.social_board_recycler);
        fab = root.findViewById(R.id.fab_social_board_register);

        if (union.equals("big"))
        {
            path = mainActivity.preferences.getString("소속", "");
            String[] tmp = path.split("/");
            path = path.substring(0, path.length() - tmp[tmp.length - 1].length()*2 - 2 - tmp[tmp.length - 3].length());
            path += "공지사항";
        }
        else
        {
            path = mainActivity.preferences.getString("소속", "");
            String[] tmp = path.split("/");
            path = path.substring(0, path.length() - tmp[tmp.length - 1].length());
            path += "공지사항";
        }

        if (mainActivity.preferences.getString("권한", "").equals("사용자"))
        {
            fab.hide();
        }
        else
        {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mainActivity, register_social_board_content.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                }
            });
        }
        
        social_board_list_adapter adapter = new social_board_list_adapter(path, fragmentManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);

        mainActivity.db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
                                String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());
                                ArrayList<String> reader_list = (ArrayList<String>)document.get("readers");
                                adapter.add_item(document.get("title").toString(), document.get("writer").toString(), dateString, reader_list.indexOf(mainActivity.getUid())>-1, document.getId());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
package com.team9797.ToMAS.ui.social.enroll_social_manager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

public class enroll_social_manager extends Fragment {

    MainActivity mainActivity;
    Button enroll_button;
    ListView listView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.enroll_social_manager, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

        //get Views
        listView = root.findViewById(R.id.enroll_social_manager_list);
        enroll_button = root.findViewById(R.id.enroll_manager);

        enroll_social_manager_list_adapter adapter = new enroll_social_manager_list_adapter();
        listView.setAdapter(adapter);

        path = mainActivity.preferences.getString("소속", "armyunit/5군단/5군단");
        String[] tmp = path.split("/");
        path = path.substring(0, path.length() - tmp[tmp.length - 1].length());
        Log.d("path", path);
        mainActivity.db.collection(path + "people")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("first", document.getId());
                                mainActivity.db.collection("user").document(document.getId())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                         @Override
                                         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                             if (task.isSuccessful()) {
                                                 DocumentSnapshot tmp_document = task.getResult();
                                                 Log.d("second", tmp_document.getId());
                                                 if (tmp_document.get("authority").toString().equals("user")) {
                                                     if (tmp_document.exists()) {
                                                         adapter.addItem(getPath(tmp_document.get("소속").toString()), tmp_document.get("계급").toString(), tmp_document.get("이름").toString(), document.getId());
                                                     }
                                                     adapter.notifyDataSetChanged();
                                                 }
                                             }
                                         }
                                     });

                            }
                        } else {
                        }
                    }
                });
        enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < adapter.selected_id.size() ; i++)
                {
                    Log.d("AA", adapter.selected_id.get(i));
                    mainActivity.db.collection("user").document(adapter.selected_id.get(i)).update("authority", "QQQ")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("AAA", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("AAA", "Error writing document", e);
                                }
                            });
                }
            }
        });



        return root;
    }

    public String getPath(String str)
    {
        String stringed_path = "";
        String[] tmp = str.split("/");
        for (int i = 1; i<tmp.length; i++)
        {
            // document를 만들기 위해 tmp로 사용했던 path를 무시한다.
            if (i%2 == 0)
                stringed_path += tmp[i] + " ";
        }
        return stringed_path;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }
}
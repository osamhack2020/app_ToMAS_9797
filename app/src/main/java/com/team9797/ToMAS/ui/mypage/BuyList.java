package com.team9797.ToMAS.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.HashMap;
import java.util.Map;

public class BuyList extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // init fragment
        View root = inflater.inflate(R.layout.buy_list, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        listView = root.findViewById(R.id.buy_list);

        final BuyListAdapter list_adapter = new BuyListAdapter();
        listView.setAdapter(list_adapter);

        // firestore에서 market list 불러오기.
        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("buy_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_adapter.addItem(document.get("title").toString(), document.get("due_date").toString(), document.getId());
                            }
                            list_adapter.notifyDataSetChanged();
                        } else {
                            //
                        }
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainActivity.db.collection("user").document(mainActivity.getUid()).collection("buy_list").document(list_adapter.listViewItemList.get(i).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ShowBuyDialog dialog = new ShowBuyDialog(mainActivity, document.get("title").toString(), document.get("due_date").toString(), document.get("place").toString(), document.get("price", Integer.class));
                                dialog.show(mainActivity.getSupportFragmentManager(), "구매확정");
                                dialog.setDialogResult(new ShowBuyDialog.show_buy_dialog_result() {
                                    @Override
                                    public void get_result() {
                                        // point_record 업데이트
                                        Toast.makeText(mainActivity, "포인트가 정산되었습니다", Toast.LENGTH_SHORT).show();
                                        Map<String, Object> post = new HashMap<>();
                                        post.put("content", "플리마켓 구매 : " + document.get("title").toString());
                                        post.put("timestamp", FieldValue.serverTimestamp());
                                        post.put("change", "-" + Integer.toString(document.get("price", Integer.class)));
                                        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("point_record").document()
                                                .set(post)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                        Map<String, Object> post2 = new HashMap<>();
                                        post2.put("content", "플리마켓 판매 : " + document.get("title").toString());
                                        post2.put("timestamp", FieldValue.serverTimestamp());
                                        post2.put("change", "+" + Integer.toString(document.get("price", Integer.class)));
                                        mainActivity.db.collection("user").document(document.get("seller_id").toString()).collection("point_record").document()
                                                .set(post2)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                        // point 업데이트
                                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("point", FieldValue.increment(document.get("price", Integer.class)));
                                        mainActivity.db.collection("user").document(document.get("seller_id").toString()).update("point", FieldValue.increment(document.get("price", Integer.class)*-1));
                                        // user 내의 구매확정 목록 삭제
                                        mainActivity.db.collection("user").document(document.get("seller_id").toString()).collection("market").document(document.get("post_id").toString()).delete();
                                        mainActivity.db.collection("user").document(mainActivity.getUid()).collection("buy_list").document(list_adapter.listViewItemList.get(i).getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // fragment 뒤로가기
                                                        fragmentManager.beginTransaction().remove(BuyList.this).commit();
                                                        fragmentManager.popBackStack();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                    }
                                });

                            }
                        }
                    }
                });
            }
        });
        return root;
    }
}
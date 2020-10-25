package com.team9797.ToMAS.ui.home.market.belong_tree;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.loginactivity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;


public class belong_tree_dialog extends DialogFragment {

    Context context;
    RecyclerView rv;
    TreeViewAdapter adapter;
    String last_select;
    tree_dialog_result mdialogResult;
    List<TreeNode> nodes;
    boolean check_first;
    RecyclerView.ViewHolder before_holder = null;

    public interface tree_dialog_result{
        void get_result(String result);
    }
    public void setDialogResult(tree_dialog_result dialogResult)
    {
        mdialogResult = dialogResult;
    }

    public belong_tree_dialog()
    {

    }

    public belong_tree_dialog(Context tmp_context)
    {
        context = tmp_context;
    }


    public void init_tree(TreeNode<Dir> node)
    {
        // 자기자신의 path까지 node로 저장하고 이를 firebase path에 넘겨줌.
        FirebaseFirestore.getInstance().collection(node.getPath())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // 소속 tree에는 market이나 중대게시판 안보이게 처리
                                String tmp_path = node.getPath() + "/" + document.getId() + "/" + document.getId();
                                TreeNode<Dir> tmp_node = new TreeNode<>(new Dir(document.getId()), tmp_path);
                                init_tree(tmp_node);
                                node.addChild(tmp_node);
                            }
                            if (check_first) {
                                adapter.refresh(nodes);
                                check_first = false;
                            }
                        } else {
                        }
                    }
                });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.belong_tree_dialog, null);
        builder.setView(rootView);

        // get view
        rv = rootView.findViewById(R.id.belong_tree_recyclerview);

        nodes = new ArrayList<>();
        TreeNode<Dir> app = new TreeNode<>(new Dir("소속"), "armyunit");
        nodes.add(app);
        check_first = true;
        init_tree(app);

        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(), new DirectoryNodeBinder()));
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);

        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                last_select = node.getPath();
                //Update and toggle the node.
                onToggle(!node.isExpand(), holder);
                if (before_holder != null)
                {
                    Log.d("AA","brforeHolder is not null");
                    before_holder.itemView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                }
                before_holder = holder;
                holder.itemView.setBackgroundColor(Color.parseColor("#FFEB3B"));
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                dirViewHolder.itemView.setBackgroundColor(Color.YELLOW);
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        rv.setAdapter(adapter);

        // click listener 연결
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mdialogResult.get_result(last_select);
                dismiss();
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public String getPath()
    {
        String stringed_path = "";
        String[] tmp = this.last_select.split("/");
        for (int i = 1; i<tmp.length; i++)
        {
            stringed_path += tmp[i] + " ";
        }
        return stringed_path;
    }

}

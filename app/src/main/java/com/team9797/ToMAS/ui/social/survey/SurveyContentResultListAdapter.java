package com.team9797.ToMAS.ui.social.survey;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class SurveyContentResultListAdapter extends RecyclerView.Adapter<SurveyContentResultListAdapter.MyViewHolder> {
    public ArrayList<String> mDataset;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String path;
    String post_id;

    public SurveyContentResultListAdapter(String tmp_path, String tmp_post_id, FragmentManager tmp_fragmentManager)
    {
        mDataset = new ArrayList<>();
        path = tmp_path;
        post_id = tmp_post_id;
        fragmentManager = tmp_fragmentManager;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView title_textView;

        public MyViewHolder(View view) {
            super(view);
            title_textView = (TextView) view.findViewById(android.R.id.text1);
        }
    }

    @Override
    public SurveyContentResultListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false) ;
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title_textView.setText("user" + Integer.toString(position+1));
        // 여기서 onclick 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new SurveyContentResultIndividual();
                Bundle args = new Bundle();
                args.putString("post_id", post_id);
                args.putString("participant_id", mDataset.get(position));
                args.putString("path", path);
                change_fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add_item(String participant_id)
    {
        mDataset.add(participant_id);
    }

}

package com.team9797.ToMAS.ui.social;

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
import com.team9797.ToMAS.ui.home.group.group_content;
import com.team9797.ToMAS.ui.home.group.group_list_item;

import java.util.ArrayList;

public class survey_list_adapter extends RecyclerView.Adapter<survey_list_adapter.MyViewHolder> {
    public ArrayList<group_list_item> mDataset;

    public survey_list_adapter()
    {
        mDataset = new ArrayList<>();

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView item_title;
        TextView item_numpeople;
        TextView item_place;
        TextView item_date;
        TextView item_time;

        public MyViewHolder(View view) {
            super(view);
            item_title = (TextView) view.findViewById(R.id.group_item_title);
            item_numpeople = (TextView) view.findViewById(R.id.group_item_numpeople);
            item_place = (TextView) view.findViewById(R.id.group_item_place);
            item_date = (TextView) view.findViewById(R.id.group_item_date);
            item_time = (TextView) view.findViewById(R.id.group_item_time);
        }
    }

    @Override
    public survey_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.group_list_item, parent, false) ;
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.item_title.setText(mDataset.get(position).getTitle());
        holder.item_numpeople.setText(Integer.toString(mDataset.get(position).getNowpeople()) + "/" + Integer.toString(mDataset.get(position).getNumpeople()));
        holder.item_place.setText(mDataset.get(position).getPlace());
        holder.item_date.setText(mDataset.get(position).getDate());
        holder.item_time.setText(mDataset.get(position).getTime());

        // 여기서 onclick 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new group_content();
                Bundle args = new Bundle();
                args.putString("post_id", mDataset.get(position).getId());
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

    public void add_item(String title, int nowpeple, int numpeople, String place, String date, String time, String post_id)
    {
        group_list_item item = new group_list_item();

        item.setTitle(title);
        item.setNowpeople(nowpeple);
        item.setNumpeople(numpeople);
        item.setPlace(place);
        item.setDate(date);
        item.setTime(time);
        item.setId(post_id);

        mDataset.add(item);
    }

}

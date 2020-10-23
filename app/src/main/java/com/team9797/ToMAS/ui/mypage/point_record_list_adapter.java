package com.team9797.ToMAS.ui.mypage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class point_record_list_adapter extends RecyclerView.Adapter<point_record_list_adapter.MyViewHolder> {
    public ArrayList<point_record_list_item> mDataset;

    public point_record_list_adapter()
    {
        mDataset = new ArrayList<>();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView item_title;
        TextView item_date;
        TextView item_change;

        public MyViewHolder(View view) {
            super(view);
            item_title = (TextView) view.findViewById(R.id.point_record_item_title);
            item_date = (TextView) view.findViewById(R.id.point_record_item_date);
            item_change = (TextView) view.findViewById(R.id.point_record_item_change);
        }
    }

    @Override
    public point_record_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.point_record_list_item, parent, false) ;
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.item_title.setText(mDataset.get(position).getTitle());
        holder.item_date.setText(mDataset.get(position).getDate());
        holder.item_change.setText(mDataset.get(position).getChange());
        if (mDataset.get(position).getChange().charAt(0) == '-')
        {
            // need to fix
            holder.item_change.setTextColor(Color.RED);
        }
        else
        {
            holder.item_change.setTextColor(Color.GREEN);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add_item(String title, String date, String change)
    {
        point_record_list_item item = new point_record_list_item();

        item.setTitle(title);
        item.setDate(date);
        item.setChange(change);

        mDataset.add(item);
    }

}

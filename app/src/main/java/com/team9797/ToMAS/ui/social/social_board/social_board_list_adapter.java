package com.team9797.ToMAS.ui.social.social_board;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class social_board_list_adapter extends RecyclerView.Adapter<social_board_list_adapter.MyViewHolder> {
    public ArrayList<social_board_list_item> mDataset;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String path;
    Context context;

    public social_board_list_adapter(String tmp_path, FragmentManager tmp_fragmentManager)
    {
        mDataset = new ArrayList<>();
        path = tmp_path;
        fragmentManager = tmp_fragmentManager;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView item_title;
        TextView item_date;
        TextView item_writer;
        ImageView item_check;

        public MyViewHolder(View view) {
            super(view);
            item_title = (TextView) view.findViewById(R.id.social_board_item_title);
            item_writer = (TextView) view.findViewById(R.id.social_board_item_writer);
            item_date = (TextView) view.findViewById(R.id.social_board_item_date);
            item_check = view.findViewById(R.id.check_icon_view);
        }
    }

    @Override
    public social_board_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.social_board_list_item, parent, false) ;
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.item_title.setText("제목 : " + mDataset.get(position).getTitle());
        holder.item_date.setText("등록일  : " + mDataset.get(position).getDate());
        holder.item_writer.setText("작성자 : " + mDataset.get(position).getWriter());

        if (mDataset.get(position).getRead())
        {
            holder.item_check.setColorFilter(ContextCompat.getColor(context, R.color.green));
        }

        // 여기서 onclick 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new social_board_content();
                Bundle args = new Bundle();
                args.putString("post_id", mDataset.get(position).getId());
                args.putString("path", path);
                args.putBoolean("isRead", mDataset.get(position).getRead());
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

    public void add_item(String title, String writer, String date, boolean isRead, String post_id)
    {
        social_board_list_item item = new social_board_list_item();

        item.setTitle(title);
        item.setDate(date);
        item.setWriter(writer);
        item.setId(post_id);
        item.setRead(isRead);

        mDataset.add(item);
    }

}

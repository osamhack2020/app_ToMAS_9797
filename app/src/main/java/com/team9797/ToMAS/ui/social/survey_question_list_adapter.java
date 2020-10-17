package com.team9797.ToMAS.ui.social;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.ui.home.group.group_content;
import com.team9797.ToMAS.ui.home.group.group_list_item;

import java.util.ArrayList;

public class survey_question_list_adapter extends RecyclerView.Adapter<survey_question_list_adapter.MyViewHolder> {
    Activity activity;
    public int count = 0;

    public survey_question_list_adapter(Activity a)
    {
        activity = a;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView item_title;
        RadioButton radioButton_1;
        RadioButton radioButton_2;
        Button button;
        LinearLayout container;

        public MyViewHolder(View view) {
            super(view);
            item_title = (TextView) view.findViewById(R.id.survey_question_list_item_title);
            radioButton_1 = view.findViewById(R.id.survey_question_radio1);
            radioButton_2 = view.findViewById(R.id.survey_question_radio2);
            button = view.findViewById(R.id.button);
            container = view.findViewById(R.id.survey_question_list_item_container);
        }
    }

    @Override
    public survey_question_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.survey_question_list_item, parent, false) ;
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.radioButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.container.removeAllViews();
                //holder.container.addView();

            }
        });
        holder.radioButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.container.removeAllViews();
                EditText tmp_edit = new EditText(activity);
                holder.container.addView(tmp_edit);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return count;
    }

    public void add_item()
    {

        count++;
    }

}

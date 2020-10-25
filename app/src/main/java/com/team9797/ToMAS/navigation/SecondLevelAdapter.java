package com.team9797.ToMAS.navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.postBoard.fragment_template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListDataHeader;
    private Map<String, List<String>> ListDataChild;
    MainActivity mainActivity;
    HashMap<String, String> path_map;
    public SecondLevelAdapter(Context context, List<String> ListDataHeader, Map<String, List<String>>ListDataChild, HashMap<String, String> tmp_path_map, MainActivity tmp_mainActivity) {
        this.context = context;
        this.ListDataHeader = ListDataHeader;
        this.ListDataChild = ListDataChild;
        mainActivity = tmp_mainActivity;
        path_map = tmp_path_map;

    }


    @Override
    public int getGroupCount() {
        return this.ListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        try{
            return this.ListDataChild.get(this.ListDataHeader.get(i)).size();

        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Object getGroup(int i) {
        return this.ListDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.ListDataChild.get(this.ListDataHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(i);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_group_second, parent, false);
        }
        TextView lblListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        // 여기서 크기나 색 바꾸기.
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(i, i1);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
        }
        TextView txtListChild = (TextView)convertView.findViewById(R.id.lblListItem);
        //txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtListChild.setText(childText);
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.closeDrawer();
                Fragment change_fragment = new fragment_template();
                Bundle args = new Bundle();
                args.putString("title", childText);
                args.putInt("fragment_style", 3);
                args.putString("path", path_map.get(childText));
                change_fragment.setArguments(args);
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack("TAG");
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, change_fragment, "TAG").commit();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}


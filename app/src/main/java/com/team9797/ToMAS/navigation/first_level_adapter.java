package com.team9797.ToMAS.navigation;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class first_level_adapter extends BaseExpandableListAdapter {
    private Context context; //final 붙여야 했는데 문법오류
    private List<String> listDataHeader;
    private HashMap<String, List<String>> list_second_level_map;
    private HashMap<String, List<String>> list_third_level_map;

    public first_level_adapter(Context context, List<String> listDataHeader) {
        this.context = context;
        this.listDataHeader = new ArrayList<>();
        this.listDataHeader.addAll(listDataHeader);
        //init second level
        String[] second_header = null;
        list_second_level_map = new HashMap<>();
        int num_first_layer = listDataHeader.size();
        for (int i = 0; i < num_first_layer; i++)
        {
            String second_content = listDataHeader.get(i); // need to fix
            switch (second_content)
            {
                case "자기개발":
                    second_header = new String[]{"수능", "자격증", "전공", "언어시험"}; // need to fix
                    break;
                case "소통게시판":
                    second_header = new String[]{"IT", "운동", "음악", "오토바이"};
                    break;
                case "플리마켓":
                    second_header = new String[]{"플리"};
                    break;
                case "인원모집":
                    second_header = new String[]{"운동", "동아리", "대회", "기타"};
                    break;
            }
            list_second_level_map.put(listDataHeader.get(i), Arrays.asList(second_header));
        }

        //init third level
        String[] third_header = null;
        List<String> listChild;
        list_third_level_map = new HashMap<>();
        for (Object o : list_second_level_map.entrySet()){
            Map.Entry entry = (Map.Entry)o;
            Object object = entry.getValue();
            if (object instanceof List)
            {
                List<String> stringList = new ArrayList<>();
                Collections.addAll(stringList,(String[])((List) object).toArray());
                for (int i = 0; i < stringList.size(); i++)
                {
                    String third_content = stringList.get(i); // need to fix
                    switch (third_content)
                    {
                        case "수능":
                            third_header = new String[]{"국어", "수학", "영어", "과학탐구", "사회탐구", "한국사", "제2외국어"}; // need to fix
                            break;
                        case "자격증":
                            third_header = new String[]{"컴퓨터활용능력", "정보처리기사"};
                            break;
                        case "전공":
                            third_header = new String[]{"대학수학", "대학물리", "코딩"};
                            break;
                        default:
                            third_header = new String[]{"토익", "토플"};
                            break;
                    }
                    listChild = Arrays.asList(third_header);
                    list_third_level_map.put(stringList.get(i), listChild);
                }
            }
        }
    }

    @Override
    public int getGroupCount()
    {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i)
    {
        /*
        if (this.listDataChild.get(this.listDataHeader.get(i)) == null) {
            return 0;
        }
        else
        {
            return this.listDataChild.get(this.listDataHeader.get(i)).size();
        }
         */
        return 1;
    }

    @Override
    public String getGroup(int i) {
        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1)
    {
        return i1; //need to fix
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1)
    {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {
        /*
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.context);
        String parentNode = (String)getGroup(i);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.context, list_second_level_map.get(parentNode), list_third_level_map));
        secondLevelExpListView.setGroupIndicator(null);
        return secondLevelExpListView;
        */

        String headerTitle = (String)getGroup(i);
        if (convertView == null)
        {
            LayoutInflater expand_Inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = expand_Inflater.inflate(R.layout.drawer_list_group, parent, false);
        }
        TextView txtListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
        txtListHeader.setTypeface(null, Typeface.BOLD);
        txtListHeader.setText(headerTitle);
        return convertView;

    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup parent) {
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.context);
        String parentNode = (String)getGroup(i);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.context, list_second_level_map.get(parentNode), list_third_level_map));
        secondLevelExpListView.setGroupIndicator(null);
        return secondLevelExpListView;
        /*
        final String childText = getChild(i, i1);
        if (view == null){
            LayoutInflater expand_Inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = expand_Inflater.inflate(R.layout.fragment_child, null);
        }
        TextView txtListChild = (TextView)view.findViewById(R.id.expand_list_item);
        txtListChild.setText(childText);
        return view;
        */

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

package com.team9797.ToMAS.ui.social.enroll_social_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class enroll_social_manager_list_adapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<enroll_social_manager_list_item> listViewItemList = new ArrayList<enroll_social_manager_list_item>() ;
    public ArrayList<String> selected_id = new ArrayList<>();

    // ListViewAdapter의 생성자
    public enroll_social_manager_list_adapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.enroll_social_manager_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_belong = (TextView) convertView.findViewById(R.id.list_item_belong);
        TextView item_class = (TextView) convertView.findViewById(R.id.list_item_class);
        TextView item_name = (TextView) convertView.findViewById(R.id.list_item_name);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        enroll_social_manager_list_item listViewItem = listViewItemList.get(position);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selected_id.add(listViewItem.getId());
                }else{
                    selected_id.remove(listViewItem.getId());
                }
            }
        });


        // 아이템 내 각 위젯에 데이터 반영
        item_belong.setText(listViewItem.getBelong());
        item_class.setText(listViewItem.getTmpClass());
        item_name.setText(listViewItem.getName());
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String belong, String tmp_class, String name, String uid) {
        enroll_social_manager_list_item item = new enroll_social_manager_list_item();

        item.setBelong(belong);
        item.setTmpClass(tmp_class);
        item.setName(name);
        item.setId(uid);

        listViewItemList.add(item);
    }
}
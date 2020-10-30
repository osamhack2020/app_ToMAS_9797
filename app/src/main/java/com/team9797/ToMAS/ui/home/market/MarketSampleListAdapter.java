package com.team9797.ToMAS.ui.home.market;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class MarketSampleListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MarketListAdapter> adapter = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;
    // ListViewAdapter의 생성자
    public MarketSampleListAdapter() {

    }
    public MarketSampleListAdapter(MainActivity mainActivity, FragmentManager fragmentManager)
    {
        this.mainActivity = mainActivity;
        this.fragmentManager = fragmentManager;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return title.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.market_sample_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_title = (TextView) convertView.findViewById(R.id.market_sample_title);
        ListView item_list = (ListView) convertView.findViewById(R.id.market_sample_list);

        item_title.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment change_fragment = new MarketCategoryFragment();
                Bundle args = new Bundle();

                args.putString("title", title.get(position));
                args.putString("path", path.get(position));
                change_fragment.setArguments(args);

                mainActivity.push_title(title.get(position));
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        // 각 subject 아래에 예시로 있는 게시판 list
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int list_position, long id) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                Fragment change_fragment = new MarketContent();

                // 게시판 id와 path를 받아와서 market_content fragment로 넘긴다.
                // maybe 이거 구조를 검색해서 바꿔야 할 듯
                Bundle args = new Bundle();
                args.putString("post_id", adapter.get(position).listViewItemList.get(list_position).getId());
                args.putString("path", path.get(position));
                change_fragment.setArguments(args);

                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        //market_list_item listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_title.setText(title.get(position));
        item_list.setAdapter(adapter.get(position));
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
        return title.get(position) ;
    }
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String tmp_title, MarketListAdapter tmp_adapter, String tmp_path) {
        title.add(tmp_title);
        adapter.add(tmp_adapter);
        path.add(tmp_path);
    }
}
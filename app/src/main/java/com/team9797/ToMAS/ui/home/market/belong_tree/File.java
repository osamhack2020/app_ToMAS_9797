package com.team9797.ToMAS.ui.home.market.belong_tree;


import com.team9797.ToMAS.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class File implements LayoutItemType {
    public String fileName;

    public File(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.treeview_file_item;
    }
}

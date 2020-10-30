package com.team9797.ToMAS.navigation;

import android.content.Context;
import android.widget.ExpandableListView;

public class CustomExpListView extends ExpandableListView {
    public CustomExpListView(Context context)
    {
        super(context);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(9999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}


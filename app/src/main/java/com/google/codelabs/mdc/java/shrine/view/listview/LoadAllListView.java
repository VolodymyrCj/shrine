package com.google.codelabs.mdc.java.shrine.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class LoadAllListView extends ListView {

    public LoadAllListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoadAllListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadAllListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
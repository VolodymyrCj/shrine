package com.google.codelabs.mdc.java.shrine.view.listview;

import android.view.View;

/**
 * @author caojin
 * @date 2017/12/11
 */

public abstract class EasyExpandableHolder extends BaseHolder {
    public abstract View createView(int groupPosition, int childPosition);

    public abstract void refreshView(int groupPosition, int childPosition, Object item);
}

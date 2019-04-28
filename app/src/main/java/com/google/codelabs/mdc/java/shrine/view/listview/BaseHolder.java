package com.google.codelabs.mdc.java.shrine.view.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author caojin
 * @date 2017/12/11
 */

public abstract class BaseHolder {
    protected Context mContext;
    protected View mView;

    public void init(Context context) {
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(getLayoutId(), null);
    }

    public abstract @LayoutRes
    int getLayoutId();
}

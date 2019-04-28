package com.google.codelabs.mdc.java.shrine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author caojin
 * @date 2018/6/19
 */
public class MaskNestedScrollView extends NestedScrollView {
    private boolean mMask;

    public MaskNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MaskNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void mask(boolean mask) {
        this.mMask = mask;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMask) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}

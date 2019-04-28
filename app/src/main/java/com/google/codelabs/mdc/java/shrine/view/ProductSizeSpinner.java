package com.google.codelabs.mdc.java.shrine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.view.listview.EasyAdapter;
import com.google.codelabs.mdc.java.shrine.view.listview.EasyHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caojin
 * @date 2018/6/24
 */
public class ProductSizeSpinner extends android.support.v7.widget.AppCompatSpinner {
    private List<String> mDataList;

    public ProductSizeSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupEntry();
    }

    private void setupEntry() {
        mDataList = new ArrayList<>(8);
        mDataList.add("00");
        mDataList.add("02");
        mDataList.add("04");
        mDataList.add("06");
        mDataList.add("08");
        mDataList.add("10");
        mDataList.add("12");
        mDataList.add("14");
        setAdapter(new EasyAdapter<String>(getContext(), mDataList) {
            @Override
            public EasyHolder<String> getHolder(int type) {
                return new EasyHolder<String>() {
                    @Override
                    public View createView(int position) {
                        return mView;
                    }

                    @Override
                    public void refreshView(int position, String item) {
                        ((TextView) mView).setText(item);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_spinner_shopping_cart_size;
                    }
                };
            }
        });
    }
}

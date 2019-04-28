package com.google.codelabs.mdc.java.shrine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
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
public class ProductColorSpinner extends android.support.v7.widget.AppCompatSpinner {
    private List<Integer> mDataList;

    public ProductColorSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupEntry();
    }

    private void setupEntry() {
        mDataList = new ArrayList<>(3);
        mDataList.add(R.drawable.product_detail_black_normal);
        mDataList.add(R.drawable.product_detail_yellow_normal);
        mDataList.add(R.drawable.product_detail_green_normal);
        setAdapter(new EasyAdapter<Integer>(getContext(), mDataList) {
            @Override
            public EasyHolder<Integer> getHolder(int type) {
                return new EasyHolder<Integer>() {
                    @Override
                    public View createView(int position) {
                        return mView;
                    }

                    @Override
                    public void refreshView(int position, Integer item) {
                        ((ImageView) mView).setImageResource(item);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_spinner_shopping_cart_color;
                    }
                };
            }
        });
    }
}

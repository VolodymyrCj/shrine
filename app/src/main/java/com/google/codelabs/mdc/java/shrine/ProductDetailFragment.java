package com.google.codelabs.mdc.java.shrine;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;
import com.google.codelabs.mdc.java.shrine.util.DensityUtil;

/**
 * @author caojin
 * @date 2018/6/21
 */
public class ProductDetailFragment extends Fragment {
    private MainActivity mActivity;

    private String mUrl;
    private String mTitle;
    private String mPrice;

    public static ProductDetailFragment newInstance(String url, String title, String price) {

        Bundle args = new Bundle();
        args.putString("url",url);
        args.putString("title", title);
        args.putString("price", price);

        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.product_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        mUrl = arguments.getString("url");
        mTitle = arguments.getString("title");
        mPrice = arguments.getString("price");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_frag, container, false);

        Toolbar toolbar = view.findViewById(R.id.app_bar);
        mActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.pop();
            }
        });

        TextView tvProductDetailTitle = view.findViewById(R.id.tvProductDetailTitle);
        TextView tvProductDetailPrice = view.findViewById(R.id.tvProductDetailPrice);
        NetworkImageView ivProductDetailImg = view.findViewById(R.id.ivProductDetailImg);

        tvProductDetailTitle.setText(mTitle);
        tvProductDetailPrice.setText(mPrice);
        ImageRequester.getInstance().setImageFromUrl(ivProductDetailImg, mUrl);

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        TranslateAnimation translateAnimation = null;
        if (enter) {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        } else {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        }
        translateAnimation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        return translateAnimation;
    }
}

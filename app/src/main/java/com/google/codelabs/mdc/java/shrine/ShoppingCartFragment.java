package com.google.codelabs.mdc.java.shrine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;
import com.google.codelabs.mdc.java.shrine.util.DensityUtil;
import com.google.codelabs.mdc.java.shrine.view.listview.EasyAdapter;
import com.google.codelabs.mdc.java.shrine.view.listview.EasyHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caojin
 * @date 2018/6/23
 */
public class ShoppingCartFragment extends Fragment {
    private static final long DURATION = 600;
    private static final long PHASE_DURATION = 300;

    private MainActivity mActivity;

    private int mHeaderHeight;
    private int mHeaderMinShownWidth;
    private int mItemFinalWidth;
    private View mHeaderShoppingCart;

    private float mHideX;
    private float mHideY;
    private float mShowX;
    private float mShowY;

    private List<ProductCart> mDataList = new ArrayList<>();

    private View mContentView;
    // TODO: 2018/6/24 添加频次过快，可能出现指针覆盖，导致被覆盖的指针所指向的view不会被移除
    private ImageView mItemView;

    private EasyAdapter<ProductCart> mAdapter;

    private TextView tvShoppingCartCount;
    private TextView tvShoppingCartTotal;
    private TextView tvShoppingCartSubtotal;
    private TextView tvShoppingCartShipping;
    private TextView tvShoppingCartTax;

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    public void addItem(ProductEntry item, float x, float y, int width, int height) {
        // 只变动商品个数，尺寸和颜色此处不填
        int index = -1;
        if ((index = mDataList.indexOf(item)) == -1) {
            ProductCart productCart = new ProductCart();
            productCart.setProductEntry(item);
            productCart.setCount(1);
            mDataList.add(productCart);
        } else {
            ProductCart productCart = mDataList.get(index);
            productCart.setCount(productCart.getCount() + 1);
        }

        animateItem(item.url, x, y, width, height);
        animateCart();

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        updateListViewExtraInfo();
    }

    private void updateListViewExtraInfo() {
        tvShoppingCartCount.setText(String.format(getString(R.string.shopping_cart_count), mDataList.size()));
        float subtotal = 0;
        for (ProductCart item : mDataList) {
            String priceS = item.getProductEntry().price;
            float price = Float.parseFloat(priceS.substring(1, priceS.length()));
            subtotal += (price * item.getCount());
        }
        float shipping = 10;
        float tax = 30.6f;
        float total = subtotal + shipping + tax;
        tvShoppingCartShipping.setText("$" + shipping);
        tvShoppingCartTax.setText("$" + tax);
        tvShoppingCartSubtotal.setText("$" + subtotal);
        tvShoppingCartTotal.setText("$" + total);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_cart_frag, container, false);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.shopping_cart_header_height);
        mHeaderMinShownWidth = mHeaderHeight / 2;
        mItemFinalWidth = getResources().getDimensionPixelSize(R.dimen.shopping_cart_header_item_width);
        setupContent(view);
        setupListView(view);

        tvShoppingCartCount = view.findViewById(R.id.tvShoppingCartCount);
        tvShoppingCartTotal = view.findViewById(R.id.tvShoppingCartTotal);
        tvShoppingCartSubtotal = view.findViewById(R.id.tvShoppingCartSubtotal);
        tvShoppingCartShipping = view.findViewById(R.id.tvShoppingCartShipping);
        tvShoppingCartTax = view.findViewById(R.id.tvShoppingCartTax);

        updateListViewExtraInfo();
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

    private void setupContent(View root) {
        mContentView = root.findViewById(R.id.contentShoppingCart);

        mHideX = DensityUtil.getScreenWidth(mActivity) - mHeaderMinShownWidth;
        mHideY = DensityUtil.getScreenHeight(mActivity) - mHeaderMinShownWidth;
        mShowX = 0;
        mShowY = 0;
        mContentView.setX(mHideX);
        mContentView.setY(mHideY);

        mHeaderShoppingCart = root.findViewById(R.id.headerShoppingCart);
        mHeaderShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateContent(mContentView, true);
            }
        });
        root.findViewById(R.id.ivCollapseShoppingCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateContent(mContentView, false);
            }
        });

        // 额外增加header的高度
        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.height = DensityUtil.getScreenHeight(mActivity) + mHeaderHeight;
        mContentView.setLayoutParams(layoutParams);
    }

    private void animateContent(View view, boolean show) {
        float phase1X = mHideX;
        float phase1Y = mHideY;
        float phase2X = mShowX + (mHideX - mShowX) / 5 * 2;
        float phase2Y = mShowY + (mHideY - mShowY) / 5 * 4;
        float phase3X = mShowX + (mHideX - mShowX) / 5;
        float phase3Y = mHideY + (mShowY - mHideY) / 5 * 2;
        float phase4X = mShowX;
        float phase4Y = mShowY - mHeaderHeight;
        ObjectAnimator animator;
        ObjectAnimator animator1;
        ObjectAnimator animator2;
        if (show) {
            animator = ObjectAnimator.ofFloat(view, View.X, phase1X, phase2X, phase3X, phase4X);
            animator1 = ObjectAnimator.ofFloat(view, View.Y, phase1Y, phase2Y, phase3Y, phase4Y);

            animator2 = ObjectAnimator.ofFloat(mHeaderShoppingCart, View.ALPHA, 1, 0);
        } else {
            animator = ObjectAnimator.ofFloat(view, View.X, phase4X, phase3X, phase2X, phase1X);
            animator1 = ObjectAnimator.ofFloat(view, View.Y, phase4Y, phase3Y, phase2Y, phase1Y);

            animator2 = ObjectAnimator.ofFloat(mHeaderShoppingCart, View.ALPHA, 0, 1);
        }

        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animator1).with(animator2);
        set.start();
    }

    private ImageView buildItemView(String url, int width, int height) {
        NetworkImageView iv = new NetworkImageView(mActivity);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        iv.setLayoutParams(params);
        iv.setElevation(DensityUtil.dp2px(mActivity, 8));
        ImageRequester.getInstance().setImageFromUrl(iv, url);
        return iv;
    }

    private void remoteItemView() {
        if (mItemView != null) {
            ViewGroup parent = (ViewGroup) getView();
            // 移除item
            parent.removeView(mItemView);
            mItemView = null;
        }
    }

    private void animateItem(String url, float itemX, float itemY, int width, int height) {
        mItemView = buildItemView(url, width, height);
        ViewGroup parent = (ViewGroup) getView();
        // 增加item
        parent.addView(mItemView);
        mItemView.setX(itemX);
        mItemView.setY(itemY);

        mItemView.setPivotX(0);
        mItemView.setPivotY(0);

        float startWidth = width;
        float endWidth = mItemFinalWidth;
        float startHeight = height;
        float endHeight = endWidth;

        LinearInterpolator interpolator = new LinearInterpolator();
        float x = ((float) PHASE_DURATION) / DURATION;
        float interpolation = interpolator.getInterpolation(x);
        float widthX = startWidth + (endWidth - startWidth) * interpolation;
        float heightX = startHeight + (endHeight - startHeight) * interpolation;

        float phaseX = DensityUtil.getScreenWidth(mActivity) - widthX;
        float phaseY = itemY + startHeight - heightX;

        float extra = (mHeaderMinShownWidth - mItemFinalWidth) / 2;
        float finalX = DensityUtil.getScreenWidth(mActivity) - mHeaderMinShownWidth + extra;
        float finalY = DensityUtil.getScreenHeight(mActivity) - mHeaderMinShownWidth + extra;

        ObjectAnimator animator = ObjectAnimator.ofFloat(mItemView, View.X, itemX, phaseX, finalX);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mItemView, View.Y, itemY, phaseY, finalY);
        float phaseScaleX = widthX / startWidth;
        float phaseScaleY = heightX / startHeight;
        float finalScaleX = endWidth / startWidth;
        float finalScaleY = endHeight / startHeight;
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mItemView, View.SCALE_X, mItemView.getScaleX(), phaseScaleX, finalScaleX);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mItemView, View.SCALE_Y, mItemView.getScaleY(), phaseScaleY, finalScaleY);

        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animator1).with(animator2).with(animator3);
        set.setDuration(DURATION);
        set.setInterpolator(interpolator);
        set.start();
    }

    private void animateCart() {
        // 额外：移动contentView
        float contentViewStartX = DensityUtil.getScreenWidth(mActivity) - mHeaderMinShownWidth;
        float contentViewEndX = contentViewStartX - mHeaderMinShownWidth;
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mContentView, View.X, contentViewStartX, contentViewEndX);
        animator4.setDuration(PHASE_DURATION);

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mContentView, View.X, contentViewEndX, contentViewStartX);
        animator5.setDuration(PHASE_DURATION);
        animator5.setStartDelay(DURATION);

        ObjectAnimator animator6 = null;
        if (mItemView != null) {
            float extra = (mHeaderMinShownWidth - mItemFinalWidth) / 2;
            float itemViewStartX = DensityUtil.getScreenWidth(mActivity) - mHeaderMinShownWidth + extra;
            float itemViewEndX = itemViewStartX + mHeaderMinShownWidth;
            animator6 = ObjectAnimator.ofFloat(mItemView, View.X, itemViewStartX, itemViewEndX);
            animator6.setDuration(PHASE_DURATION);
            animator6.setStartDelay(DURATION);
        }

        final AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = set.play(animator4).with(animator5);
        if (animator6 != null) {
            builder.with(animator6);
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                remoteItemView();
                set.removeAllListeners();
            }
        });
        set.start();
    }

    private void setupListView(View view) {
        ListView lvShoppingCart = view.findViewById(R.id.lvShoppingCart);
        mAdapter = new EasyAdapter<ProductCart>(mActivity, mDataList) {
            @Override
            public EasyHolder<ProductCart> getHolder(int type) {
                return new EasyHolder<ProductCart>() {
                    private NetworkImageView ivShoppingCartItemImg;
                    private TextView ivShoppingCartItemName;
                    private TextView ivShoppingCartItemPrice;

                    @Override
                    public View createView(int position) {
                        ivShoppingCartItemImg = mView.findViewById(R.id.ivShoppingCartItemImg);
                        ivShoppingCartItemName = mView.findViewById(R.id.ivShoppingCartItemName);
                        ivShoppingCartItemPrice = mView.findViewById(R.id.ivShoppingCartItemPrice);
                        return mView;
                    }

                    @Override
                    public void refreshView(int position, ProductCart item) {
                        ProductEntry productEntry = item.getProductEntry();
                        ImageRequester.getInstance().setImageFromUrl(ivShoppingCartItemImg, productEntry.url);
                        ivShoppingCartItemName.setText(productEntry.title);
                        ivShoppingCartItemPrice.setText(productEntry.price);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_shopping_cart_product;
                    }
                };
            }
        };
        lvShoppingCart.setAdapter(mAdapter);
    }
}

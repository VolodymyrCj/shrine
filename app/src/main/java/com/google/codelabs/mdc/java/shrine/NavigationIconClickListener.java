package com.google.codelabs.mdc.java.shrine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * {@link android.view.View.OnClickListener} used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
public class NavigationIconClickListener implements View.OnClickListener {

    private final AnimatorSet animatorSet = new AnimatorSet();
    private Context context;
    private MaskNestedScrollView sheet;
    private Toolbar mToolbar;
    private Interpolator interpolator;
    private int height;
    private boolean backdropShown = false;
    private Drawable openIcon;
    private Drawable closeIcon;
    private Drawable mForegroundColorDrawable;

    NavigationIconClickListener(Context context, MaskNestedScrollView sheet, Toolbar toolbar) {
        this(context, sheet, toolbar, null);
    }

    NavigationIconClickListener(Context context, MaskNestedScrollView sheet, Toolbar toolbar, @Nullable Interpolator interpolator) {
        this(context, sheet, toolbar, interpolator, null, null);
    }

    NavigationIconClickListener(
            Context context, MaskNestedScrollView sheet, Toolbar toolbar, @Nullable Interpolator interpolator,
            @Nullable Drawable openIcon, @Nullable Drawable closeIcon) {
        this.context = context;
        this.sheet = sheet;
        mToolbar = toolbar;
        this.interpolator = interpolator;
        this.openIcon = openIcon;
        this.closeIcon = closeIcon;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        mForegroundColorDrawable = new ColorDrawable(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onClick(View view) {
        backdropShown = !backdropShown;

        // Cancel the existing animations
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();

        updateIcon();

        final int translateY = height -
                context.getResources().getDimensionPixelSize(R.dimen.shr_product_grid_reveal_height);

        ObjectAnimator animator = ObjectAnimator.ofFloat(sheet, "translationY", backdropShown ? translateY : 0);
        animatorSet.play(animator);
        setupForeground(animator, translateY);

        animator.setDuration(500);
        animator.setInterpolator(interpolator);
        animator.start();
    }

    private void setupForeground(ObjectAnimator animator, final int translateY) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sheet.mask(backdropShown);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int alpha = (int) ((float) valueAnimator.getAnimatedValue() / translateY * 127);
                    mForegroundColorDrawable.setAlpha(alpha);
                    sheet.setForeground(mForegroundColorDrawable);
                }
            });
        }
    }

    private void updateIcon() {
        if (openIcon != null && closeIcon != null) {
            if (backdropShown) {
                mToolbar.setNavigationIcon(closeIcon);
            } else {
                mToolbar.setNavigationIcon(openIcon);
            }
        }
    }
}

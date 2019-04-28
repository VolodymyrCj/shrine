package com.google.codelabs.mdc.java.shrine;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.codelabs.mdc.java.shrine.network.ProductEntry;
import com.google.codelabs.mdc.java.shrine.staggeredgridlayout.StaggeredProductCardRecyclerViewAdapter;
import com.google.codelabs.mdc.java.shrine.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductGridFragment extends Fragment {
    private List<ProductEntry> mProductEntries = new ArrayList<>();
    private StaggeredProductCardRecyclerViewAdapter mAdapter;
    private int mCategory = 0;

    private Toolbar mToolbar;
    private NavigationIconClickListener mNavigationIconClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.shr_product_grid_fragment, container, false);

        // Set up the tool bar
        setUpToolbar(view);

        setupCategoryRadioGroup(view);

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position % 3 == 2 ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        updateProductEntry();
        mAdapter = new StaggeredProductCardRecyclerViewAdapter(
                mProductEntries);
        mAdapter.setOnItemClickListener(new StaggeredProductCardRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductEntry product = mProductEntries.get(position);
                ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(product.url, product.title, product.price);
                ((MainActivity) getActivity()).navigateTo(productDetailFragment, true);
            }

            @Override
            public void onCartClick(View view, int position) {
                ProductEntry product = mProductEntries.get(position);
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                ((MainActivity) getActivity()).addItemOnShoppingCartFragment(product, rect.left, rect.top,
                        view.getWidth(), view.getHeight());
            }
        });
        recyclerView.setAdapter(mAdapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.product_grid).setBackground(getContext().getDrawable(R.drawable.shr_product_grid_background_shape));
        }

        return view;
    }

    private void updateProductEntry() {
        mProductEntries.clear();
        mProductEntries.addAll(ProductEntry.initProductEntryList(getResources(), mCategory));
    }

    private void setupCategoryRadioGroup(View view) {
        RadioGroup rgCategory = view.findViewById(R.id.rgCategory);
        rgCategory.check(rgCategory.getChildAt(0).getId());

        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                if (radioButton.isChecked()) {
                    mCategory = radioGroup.indexOfChild(radioButton);
                    updateProductEntry();
                    mAdapter.notifyDataSetChanged();

                    mNavigationIconClickListener.onClick(mToolbar);
                }
            }
        });
    }

    private void setUpToolbar(View view) {
        mToolbar = view.findViewById(R.id.app_bar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(mToolbar);
        }

        mNavigationIconClickListener = new NavigationIconClickListener(
                getContext(),
                (MaskNestedScrollView) view.findViewById(R.id.product_grid),
                mToolbar,
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.shr_branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.shr_close_menu));
        mToolbar.setNavigationOnClickListener(mNavigationIconClickListener); // Menu close icon
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

}

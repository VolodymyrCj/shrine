package com.google.codelabs.mdc.java.shrine.staggeredgridlayout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

import java.util.List;

/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1
 * item in the second column, and so on.
 */
public class StaggeredProductCardRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredProductCardViewHolder> implements View.OnClickListener {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public StaggeredProductCardRecyclerViewAdapter(List<ProductEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @NonNull
    @Override
    public StaggeredProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.shr_staggered_product_card_first;
        if (viewType == 1) {
            layoutId = R.layout.shr_staggered_product_card_second;
        } else if (viewType == 2) {
            layoutId = R.layout.shr_staggered_product_card_third;
        }

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StaggeredProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StaggeredProductCardViewHolder holder, final int position) {
        if (productList != null && position < productList.size()) {
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
            holder.productImage.setOnClickListener(this);
            holder.productImage.setTag(position);
            holder.productAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onCartClick(holder.productImage, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            Object tag = view.getTag();
            if (tag != null && tag instanceof Integer) {
                mListener.onItemClick(view, (Integer) tag);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);

        void onCartClick(View view , int position);
    }
}

package com.google.codelabs.mdc.java.shrine;

import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

import java.util.Objects;

/**
 * @author caojin
 * @date 2018/6/24
 */
public class ProductCart {
    private ProductEntry productEntry;
    private int size;
    private int color;
    private int count;

    public ProductCart() {}

    public ProductEntry getProductEntry() {
        return productEntry;
    }

    public void setProductEntry(ProductEntry productEntry) {
        this.productEntry = productEntry;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductCart that = (ProductCart) o;
        return Objects.equals(productEntry, that.productEntry);
    }

    @Override
    public int hashCode() {

        return Objects.hash(productEntry);
    }

    @Override
    public String toString() {
        return "ProductCart{" +
                "productEntry=" + productEntry +
                ", size=" + size +
                ", color=" + color +
                ", count=" + count +
                '}';
    }
}

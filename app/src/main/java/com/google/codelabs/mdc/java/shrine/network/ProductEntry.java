package com.google.codelabs.mdc.java.shrine.network;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.google.codelabs.mdc.java.shrine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A product entry in the list of products.
 */
public class ProductEntry {

    private static final String TAG = ProductEntry.class.getSimpleName();

    public final String title;
    public final Uri dynamicUrl;
    public final String url;
    public final String price;
    public final String description;

    public ProductEntry(
            String title, String dynamicUrl, String url, String price, String description) {
        this.title = title;
        this.dynamicUrl = Uri.parse(dynamicUrl);
        this.url = url;
        this.price = price;
        this.description = description;
    }

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
     */
    public static List<ProductEntry> initProductEntryList(Resources resources, int category) {
        int categoryId = -1;
        switch (category) {
            case 0:
                categoryId = R.raw.product_featured;
                break;
            case 1:
                categoryId = R.raw.product_apartment;
                break;
            case 2:
                categoryId = R.raw.product_accessory;
                break;
            case 3:
                categoryId = R.raw.product_shoe;
                break;
            case 4:
                categoryId = R.raw.product_top;
                break;
            case 5:
                categoryId = R.raw.product_bottom;
                break;
            case 6:
                categoryId = R.raw.product_dress;
                break;
        }
        InputStream inputStream = resources.openRawResource(categoryId);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonProductsString = writer.toString();
        Gson gson = new Gson();
        Type productListType = new TypeToken<ArrayList<ProductEntry>>() {
        }.getType();
        return gson.fromJson(jsonProductsString, productListType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntry that = (ProductEntry) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(dynamicUrl, that.dynamicUrl) &&
                Objects.equals(url, that.url) &&
                Objects.equals(price, that.price) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, dynamicUrl, url, price, description);
    }
}
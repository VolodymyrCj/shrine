package com.google.codelabs.mdc.java.shrine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

public class MainActivity extends AppCompatActivity implements NavigationHost {
    private ShoppingCartFragment mShoppingCartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shr_main_activity);

        if (savedInstanceState == null) {
            mShoppingCartFragment = ShoppingCartFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new ProductGridFragment())
                    .add(R.id.containerShoppingCart, mShoppingCartFragment)
                    .commit();
        }
    }

    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public void pop() {
        getSupportFragmentManager().popBackStack();
    }

    public void addItemOnShoppingCartFragment(ProductEntry item, float x, float y, int width, int height) {
        if (mShoppingCartFragment != null) {
            mShoppingCartFragment.addItem(item, x, y, width, height);
        }
    }
}

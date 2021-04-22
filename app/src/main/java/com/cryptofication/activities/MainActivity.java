package com.cryptofication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.cryptofication.R;
import com.cryptofication.adapters.RecyclerViewCryptoListAdapter;
import com.cryptofication.background.FetchDataAPI;
import com.cryptofication.classes.DataClass;
import com.cryptofication.objects.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    final private DataClass dc = new DataClass();

    private RecyclerView rwCrypto;
    private SwipeRefreshLayout srlReloadData;
    private RecyclerViewCryptoListAdapter rwCryptoAdapter;
    private RecyclerView.LayoutManager rwCryptoManager;

    private MenuItem mnFilter, itemName, itemSymbol, itemPrice, itemAscending, itemDescending;
    private ArrayList<MenuItem> menuItemsOptions = new ArrayList<>();
    private ArrayList<MenuItem> menuItemsOrder = new ArrayList<>();

    private int lastSelectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
        setContentView(R.layout.activity_main);
        references();

        // Insert custom toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar_home);
        getSupportActionBar().setElevation(10);

        // SwipeRefreshLayout listener and customization
        srlReloadData.setOnRefreshListener(this);
        srlReloadData.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        // Fetching data from server
        srlReloadData.post(this::loadDataCrypto);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu and find items on it
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        mnFilter = menu.findItem(R.id.mnFilter);
        itemName = menu.findItem(R.id.mnFilterOptionName);
        itemSymbol = menu.findItem(R.id.mnFilterOptionSymbol);
        itemPrice = menu.findItem(R.id.mnFilterOptionPrice);
        menuItemsOptions.add(itemName);
        menuItemsOptions.add(itemSymbol);
        menuItemsOptions.add(itemPrice);
        itemAscending = menu.findItem(R.id.mnFilterOrderAscending);
        itemDescending = menu.findItem(R.id.mnFilterOrderDescending);
        menuItemsOrder.add(itemAscending);
        menuItemsOrder.add(itemDescending);
        lastSelectedItem = itemName.getItemId();

        final MenuItem searchItem = menu.findItem(R.id.mnSearch);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // SearchView and searchItem listeners
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rwCryptoAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("MainActivity", "Opened search");
                searchView.onActionViewExpanded();
                return true; // True to be able to open
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("MainActivity", "Closed search");
                rwCryptoAdapter.getFilter().filter("");
                searchView.onActionViewCollapsed();
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true; // True as we want to be able to close it
            }
        });
        return true;
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int order = 0;
        int itemId = item.getItemId();
        int itemTypeSelected = 0;

        if (itemId == R.id.mnFilterOptionName) {
            if (lastSelectedItem == R.id.mnFilterOptionName) {
                if (itemAscending.isChecked()) {
                    itemDescending.setChecked(true);
                    order = 1;
                } else {
                    itemAscending.setChecked(true);
                    order = 0;
                }
            } else {
                item.setChecked(true);
                itemAscending.setChecked(true);
                order = 0;
            }
            lastSelectedItem = itemId;
            itemTypeSelected = 0;
        } else if (itemId == R.id.mnFilterOptionSymbol) {
            if (lastSelectedItem == R.id.mnFilterOptionSymbol) {
                if (itemAscending.isChecked()) {
                    itemDescending.setChecked(true);
                    order = 1;
                } else {
                    itemAscending.setChecked(true);
                    order = 0;
                }
            } else {
                item.setChecked(true);
                itemAscending.setChecked(true);
                order = 0;
            }
            lastSelectedItem = itemId;
            itemTypeSelected = 1;
        } else if (itemId == R.id.mnFilterOptionPrice) {
            if (lastSelectedItem == R.id.mnFilterOptionPrice) {
                if (itemAscending.isChecked()) {
                    itemDescending.setChecked(true);
                    order = 1;
                } else {
                    itemAscending.setChecked(true);
                    order = 0;
                }
            } else {
                item.setChecked(true);
                itemAscending.setChecked(true);
                order = 0;
            }
            lastSelectedItem = itemId;
            itemTypeSelected = 2;
        } else if (itemId == R.id.mnFilterOrderAscending) {
            itemAscending.setChecked(true);
            order = 0;
            if (itemName.isChecked()) {
                itemTypeSelected = 0;
            } else if (itemSymbol.isChecked()) {
                itemTypeSelected = 1;
            } else {
                itemTypeSelected = 2;
            }
        } else if (itemId == R.id.mnFilterOrderDescending) {
            itemDescending.setChecked(true);
            order = 1;
            if (itemName.isChecked()) {
                itemTypeSelected = 0;
            } else if (itemSymbol.isChecked()) {
                itemTypeSelected = 1;
            } else {
                itemTypeSelected = 2;
            }
        } else {
            Toast.makeText(this, "xd", Toast.LENGTH_LONG).show();
        }

        if (itemId != R.id.mnFilter) {
            changeSortRecyclerView(itemTypeSelected, order);
        }
        return true;

    }

    @Override
    public void onRefresh() {
        // When refreshing, load crypto data again
        loadDataCrypto();
    }

    private void loadDataCrypto() {
        // Showing refresh animation before making api call
        srlReloadData.setRefreshing(true);

        // Get the data from the API and put the returned list into the list in DataClass
        try {
            dc.cryptoList = (List<Post>) new FetchDataAPI().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Check cryptoList size
        Log.d("MainActivity", "cryptoList size: " + dc.cryptoList.size());

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(this);
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(this, new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);

        // Stopping swipe refresh
        srlReloadData.setRefreshing(false);
    }

    private void changeSortRecyclerView(int type, int order) {
        switch (type) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Post::getName));
                    if (order == 1) {
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Post::getSymbol));
                    if (order == 1) {
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Post::getCurrentPrice));
                    if (order == 1) {
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            default:
                break;
        }
        // Showing refresh animation before making api call
        srlReloadData.setRefreshing(true);

        // Check cryptoList size
        Log.d("MainActivity", "cryptoList size: " + dc.cryptoList.size());

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(this);
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(this, new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);

        // Stopping swipe refresh
        srlReloadData.setRefreshing(false);
    }

    private void references() {
        rwCrypto = findViewById(R.id.rwCryptoListList);
        srlReloadData = findViewById(R.id.srlReloadData);
    }
}
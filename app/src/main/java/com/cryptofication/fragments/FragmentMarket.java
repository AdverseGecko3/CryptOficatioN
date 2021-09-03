package com.cryptofication.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cryptofication.classes.ContextApplication;
import com.cryptofication.classes.DataClass;
import com.cryptofication.R;

import androidx.appcompat.app.ActionBar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.cryptofication.adapters.RecyclerViewCryptoListAdapter;
import com.cryptofication.background.FetchDataAPI;
import com.cryptofication.objects.Crypto;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FragmentMarket extends Fragment {

    final private DataClass dc = new DataClass();

    private RecyclerView rwCrypto;
    private SwipeRefreshLayout srlReloadMarket;
    private RecyclerViewCryptoListAdapter rwCryptoAdapter;
    private RecyclerView.LayoutManager rwCryptoManager;

    private MenuItem itemName;
    private MenuItem itemSymbol;
    private MenuItem itemPrice;
    private MenuItem itemAscending;
    private MenuItem itemDescending;
    private final ArrayList<MenuItem> menuItemsOptions = new ArrayList<>();
    private final ArrayList<MenuItem> menuItemsOrder = new ArrayList<>();

    private int lastSelectedFilterItem = 0, type = 0, order = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_market, container, false);
        references(fView);
        setHasOptionsMenu(true);

        // Insert custom toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowCustomEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setCustomView(R.layout.toolbar_home);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setElevation(10);

        // SwipeRefreshLayout listener and customization
        srlReloadMarket.setOnRefreshListener(() -> {
            // When refreshing, load crypto data again
            if (itemSymbol.isChecked()) {
                type = 1;
            } else if (itemPrice.isChecked()) {
                type = 2;
            }
            if (itemDescending.isChecked()) {
                order = 1;
            }

            loadDataCrypto();
        });
        srlReloadMarket.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        // Fetching data from server
        srlReloadMarket.post(this::loadDataCrypto);
        return fView;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        // Inflate menu and find items on it
        inflater.inflate(R.menu.menu, menu);

        itemName = menu.findItem(R.id.mnFilterOptionName);
        itemSymbol = menu.findItem(R.id.mnFilterOptionSymbol);
        itemPrice = menu.findItem(R.id.mnFilterOptionPrice);
        MenuItem itemPrice = menu.findItem(R.id.mnFilterOptionPrice);
        menuItemsOptions.add(itemName);
        menuItemsOptions.add(itemSymbol);
        menuItemsOptions.add(itemPrice);
        itemAscending = menu.findItem(R.id.mnFilterOrderAscending);
        itemDescending = menu.findItem(R.id.mnFilterOrderDescending);
        menuItemsOrder.add(itemAscending);
        menuItemsOrder.add(itemDescending);
        lastSelectedFilterItem = itemName.getItemId();

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
            public boolean onQueryTextChange(String query) {
                rwCryptoAdapter.getFilter().filter(query);
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
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.mnFilterOptionName:
                if (lastSelectedFilterItem == R.id.mnFilterOptionName) {
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
                lastSelectedFilterItem = itemId;
                type = 0;
                break;
            case R.id.mnFilterOptionSymbol:
                if (lastSelectedFilterItem == R.id.mnFilterOptionSymbol) {
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
                lastSelectedFilterItem = itemId;
                type = 1;
                break;
            case R.id.mnFilterOptionPrice:
                if (lastSelectedFilterItem == R.id.mnFilterOptionPrice) {
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
                lastSelectedFilterItem = itemId;
                type = 2;
                break;
            case R.id.mnFilterOrderAscending:
                itemAscending.setChecked(true);
                order = 0;
                if (itemName.isChecked()) {
                    type = 0;
                } else if (itemSymbol.isChecked()) {
                    type = 1;
                } else {
                    type = 2;
                }
                break;
            case R.id.mnFilterOrderDescending:
                itemDescending.setChecked(true);
                order = 1;
                if (itemName.isChecked()) {
                    type = 0;
                } else if (itemSymbol.isChecked()) {
                    type = 1;
                } else {
                    type = 2;
                }
                break;
            default:
                break;
        }

        if (itemId == R.id.mnFilterOptionName || itemId == R.id.mnFilterOptionSymbol ||
                itemId == R.id.mnFilterOptionPrice || itemId == R.id.mnFilterOrderAscending ||
                itemId == R.id.mnFilterOrderDescending) {
            changeSortRecyclerView(type, order);
        }
        return true;

    }

    private void loadDataCrypto() {
        // Showing refresh animation before making api call
        srlReloadMarket.setRefreshing(true);

        try {
            String userCurrency = "";
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextApplication.getAppContext());
            Log.d("SharedPreferences", sharedPreferences.getString("prefCurrency", ""));
            userCurrency = sharedPreferences.getString("prefCurrency", "");
            dc.cryptoList = (List<Crypto>) new FetchDataAPI().execute(userCurrency).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        changeSortRecyclerView(type, order);

        // Check cryptoList size
        Log.d("MainActivity", "cryptoList size: " + dc.cryptoList.size());

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(getContext());
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(getContext(), new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);

        // Stopping swipe refresh
        srlReloadMarket.setRefreshing(false);
    }

    private void changeSortRecyclerView(int type, int order) {
        switch (type) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getName));
                    if (order == 1) {
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getSymbol));
                    if (order == 1) {
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getCurrentPrice));
                    if (order == 1) {
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            default:
                break;
        }
        // Showing refresh animation before making api call
        srlReloadMarket.setRefreshing(true);

        // Check cryptoList size
        Log.d("MainActivity", "cryptoList size: " + dc.cryptoList.size());

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(getContext());
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(getContext(), new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);

        // Stopping swipe refresh
        srlReloadMarket.setRefreshing(false);
    }

    private void references(View view) {
        rwCrypto = view.findViewById(R.id.rwCryptoListList);
        srlReloadMarket = view.findViewById(R.id.srlReloadMarket);
    }
}

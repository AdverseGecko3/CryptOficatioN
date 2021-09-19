package com.cryptofication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.cryptofication.classes.Constants;
import com.cryptofication.classes.ContextApplication;
import com.cryptofication.classes.DataClass;
import com.cryptofication.R;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.Toast;

import com.cryptofication.adapters.RecyclerViewCryptoListAdapter;
import com.cryptofication.background.FetchDataAPI;
import com.cryptofication.objects.Crypto;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class FragmentMarket extends Fragment {

    final private DataClass dc = new DataClass();

    private RecyclerView rwCrypto;
    private SwipeRefreshLayout srlReloadMarket;
    private RecyclerViewCryptoListAdapter rwCryptoAdapter;
    private RecyclerView.LayoutManager rwCryptoManager;
    private ColorDrawable rwSwipeBackground;
    private Drawable rwSwipeIcon;

    private MenuItem itemName, itemSymbol, itemPrice, itemAscending, itemDescending;

    private int lastSelectedFilterItem = 0;

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

        // Set the color and icon of canvas when swiping item
        rwSwipeBackground = new ColorDrawable(getResources().getColor(R.color.yellow_favorite));
        rwSwipeIcon = AppCompatResources.getDrawable(ContextApplication.getAppContext(), R.drawable.ic_star);

        assert rwSwipeIcon != null;
        rwSwipeIcon = DrawableCompat.wrap(rwSwipeIcon);
        DrawableCompat.setTint(rwSwipeIcon, getResources().getColor(R.color.super_dark_purple));

        // SwipeRefreshLayout listener and customization
        srlReloadMarket.setOnRefreshListener(() -> {
            // When refreshing, load crypto data again
            if (itemSymbol.isChecked()) {
                dc.orderOption = 1;
            } else if (itemPrice.isChecked()) {
                dc.orderOption = 2;
            }
            if (itemDescending.isChecked()) {
                dc.orderFilter = 1;
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

        referencesOptionsMenu(menu);

        lastSelectedFilterItem = itemName.getItemId();

        final MenuItem itemSearch = menu.findItem(R.id.mnSearch);
        final SearchView viewSearch = (SearchView) itemSearch.getActionView();

        // SearchView and itemSearch listeners
        viewSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        viewSearch.setIconified(false);

        viewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        itemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("MainActivity", "Opened search");
                viewSearch.onActionViewExpanded();
                return true; // True to be able to open
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("MainActivity", "Closed search");
                rwCryptoAdapter.getFilter().filter("");
                viewSearch.onActionViewCollapsed();
                viewSearch.setQuery("", false);
                viewSearch.clearFocus();
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
                        dc.orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        dc.orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    dc.orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                dc.orderOption = 0;
                break;
            case R.id.mnFilterOptionSymbol:
                if (lastSelectedFilterItem == R.id.mnFilterOptionSymbol) {
                    if (itemAscending.isChecked()) {
                        itemDescending.setChecked(true);
                        dc.orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        dc.orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    dc.orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                dc.orderOption = 1;
                break;
            case R.id.mnFilterOptionPrice:
                if (lastSelectedFilterItem == R.id.mnFilterOptionPrice) {
                    if (itemAscending.isChecked()) {
                        itemDescending.setChecked(true);
                        dc.orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        dc.orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    dc.orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                dc.orderOption = 2;
                break;
            case R.id.mnFilterOrderAscending:
                itemAscending.setChecked(true);
                dc.orderFilter = 0;
                if (itemName.isChecked()) {
                    dc.orderOption = 0;
                } else if (itemSymbol.isChecked()) {
                    dc.orderOption = 1;
                } else {
                    dc.orderOption = 2;
                }
                break;
            case R.id.mnFilterOrderDescending:
                itemDescending.setChecked(true);
                dc.orderFilter = 1;
                if (itemName.isChecked()) {
                    dc.orderOption = 0;
                } else if (itemSymbol.isChecked()) {
                    dc.orderOption = 1;
                } else {
                    dc.orderOption = 2;
                }
                break;
            default:
                break;
        }

        if (itemId == R.id.mnFilterOptionName || itemId == R.id.mnFilterOptionSymbol ||
                itemId == R.id.mnFilterOptionPrice || itemId == R.id.mnFilterOrderAscending ||
                itemId == R.id.mnFilterOrderDescending) {
            changeSortRecyclerView(dc.orderOption, dc.orderFilter);
        }
        return true;
    }

    ItemTouchHelper.SimpleCallback ithCallback = new ItemTouchHelper.SimpleCallback (0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            int iconMargin = (itemView.getHeight() - rwSwipeIcon.getIntrinsicHeight()) / 2;
            if (dX > 0) {
                rwSwipeBackground.setBounds(itemView.getLeft(), itemView.getTop(), Math.round(dX), itemView.getBottom());
                rwSwipeIcon.setBounds(itemView.getLeft() + iconMargin, itemView.getTop() + iconMargin,
                        itemView.getLeft() + iconMargin + rwSwipeIcon.getIntrinsicHeight(), itemView.getBottom() - iconMargin);
            } else {
                rwSwipeBackground.setBounds(itemView.getRight() + Math.round(dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                rwSwipeIcon.setBounds(itemView.getRight() - iconMargin - rwSwipeIcon.getIntrinsicHeight(), itemView.getTop() + iconMargin,
                        itemView.getRight() - iconMargin, itemView.getBottom() - iconMargin);
            }

            rwSwipeBackground.draw(c);
            c.save();
            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(), Math.round(dX), itemView.getBottom());
            } else {
                c.clipRect(itemView.getRight() + Math.round(dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            }
            rwSwipeIcon.draw(c);
            c.restore();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("itemSwipe", "Item position: " + viewHolder.getBindingAdapterPosition() +
                    " - Item name:" + dc.cryptoList.get(viewHolder.getBindingAdapterPosition()));
            rwCryptoAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
            Snackbar.make(viewHolder.itemView, dc.cryptoList.get(viewHolder.getBindingAdapterPosition()).getName() + " added to favorites",
                    Snackbar.LENGTH_LONG).setAction("UNDO", v -> {
                        /*
                        Restore favorite item
                        */
                    }).show();
        }
    };

    private void loadDataCrypto() {
        // Showing refresh animation before making api call
        srlReloadMarket.setRefreshing(true);

        Intent intent = new Intent(ContextApplication.getAppContext(),
                FetchDataAPI.class);
        ListResultReceiver resultReceiver = new ListResultReceiver(new Handler());
        intent.putExtra("receiver", resultReceiver);
        ContextApplication.getAppContext().startService(intent);

        // Check cryptoList size
        Log.d("MainActivity", "cryptoList size: " + dc.cryptoList.size());
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
        new ItemTouchHelper(ithCallback).attachToRecyclerView(rwCrypto);

        // Stopping swipe refresh
        srlReloadMarket.setRefreshing(false);
    }

    private void firstSortRecyclerView(int type, int order) {
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
        new ItemTouchHelper(ithCallback).attachToRecyclerView(rwCrypto);

        // Change the firstRun to false
        DataClass.firstRun = false;

        stopReloadingMarket();
    }

    private void postFetchAPI() {
        if (DataClass.firstRun) {
            Log.d("firstRunPost", "App first run");
            SharedPreferences userPrefs = requireActivity().getSharedPreferences(ContextApplication.
                    getAppContext().getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
            String userFilterOption = userPrefs.getString(Constants.PREF_FILTER_OPTION,
                    getString(R.string.SETTINGS_FILTER_OPTION_VALUE_DEFAULT));
            String userFilterOrder = userPrefs.getString(Constants.PREF_FILTER_ORDER,
                    getString(R.string.SETTINGS_FILTER_ORDER_VALUE_DEFAULT));
            changeSelectionFilters(Integer.parseInt(userFilterOption), Integer.parseInt(userFilterOrder));
            firstSortRecyclerView(Integer.parseInt(userFilterOption), Integer.parseInt(userFilterOrder));
        } else {
            changeSortRecyclerView(dc.orderOption, dc.orderFilter);
        }

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(getContext());
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(getContext(), new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);

        stopReloadingMarket();
    }

    private void changeSelectionFilters(int userFilterOption, int userFilterOrder) {

    }

    private void stopReloadingMarket() {
        // Stopping swipe refresh
        srlReloadMarket.setRefreshing(false);
    }

    private void references(View view) {
        rwCrypto = view.findViewById(R.id.rwCryptoListList);
        srlReloadMarket = view.findViewById(R.id.srlReloadMarket);
    }

    private void referencesOptionsMenu(Menu menu) {
        itemName = menu.findItem(R.id.mnFilterOptionName);
        itemSymbol = menu.findItem(R.id.mnFilterOptionSymbol);
        itemPrice = menu.findItem(R.id.mnFilterOptionPrice);
        itemAscending = menu.findItem(R.id.mnFilterOrderAscending);
        itemDescending = menu.findItem(R.id.mnFilterOrderDescending);
    }

    private class ListResultReceiver extends ResultReceiver {

        public ListResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case 1:
                    Toast.makeText(ContextApplication.getAppContext(), "Error.\nCheck your internet connection.",
                            Toast.LENGTH_LONG).show();
                    stopReloadingMarket();
                    break;
                case 0:
                    dc.cryptoList = resultData.getParcelableArrayList("cryptoList");
                    Log.d("BackgroundCryptoS", String.valueOf(dc.cryptoList.size()));
                    postFetchAPI();
                    break;
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }
}

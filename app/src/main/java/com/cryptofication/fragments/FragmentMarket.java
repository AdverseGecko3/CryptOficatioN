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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class FragmentMarket extends Fragment {

    final private DataClass dc = new DataClass();

    private RecyclerView rwCrypto;
    private SwipeRefreshLayout srlReloadList;
    private RecyclerViewCryptoListAdapter rwCryptoAdapter;
    private RecyclerView.LayoutManager rwCryptoManager;
    private ColorDrawable rwSwipeBackground;
    private Drawable rwSwipeIcon;

    private MenuItem itemName, itemSymbol, itemPrice, itemPercentage, itemAscending, itemDescending;

    private int lastSelectedFilterItem = 0;
    private int orderOption, orderFilter;

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

        // Check if variables were saved when returning to FragmentMarket
        Log.d("onCreateView", "lastSelectedFilteredItem: " + lastSelectedFilterItem + " - orderOption: "
                + orderOption + " - orderFilter: " + orderFilter);

        // Set the color and icon of canvas when swiping item
        rwSwipeBackground = new ColorDrawable(getResources().getColor(R.color.yellow_favorite));
        rwSwipeIcon = AppCompatResources.getDrawable(ContextApplication.getAppContext(), R.drawable.ic_star);

        // SwipeRefreshLayout listener and customization
        srlReloadList.setOnRefreshListener(() -> {
            // When refreshing, load crypto data again
            if (itemSymbol.isChecked()) {
                orderOption = 1;
            } else if (itemPrice.isChecked()) {
                orderOption = 2;
            } else if (itemPercentage.isChecked()) {
                orderOption = 3;
            }
            if (itemDescending.isChecked()) {
                orderFilter = 1;
            }

            loadDataCrypto();
        });
        srlReloadList.setColorSchemeResources(R.color.purple_app_accent);

        // Fetching data from server
        srlReloadList.post(this::loadDataCrypto);
        return fView;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        // Inflate menu and find items on it
        inflater.inflate(R.menu.menu, menu);
        referencesOptionsMenu(menu);

        // Find itemSearch and viewSearch in the toolbar
        final MenuItem itemSearch = menu.findItem(R.id.mnSearch);
        final SearchView viewSearch = (SearchView) itemSearch.getActionView();

        // If it is the first run load the starting filters from the SharedPreferences
        if (DataClass.firstRun) {
            Log.d("onCreateOptionsMenu", "First run");
            SharedPreferences userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                    getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
            String userFilterOption = userPrefs.getString(Constants.PREF_FILTER_OPTION, getString(R.string.SETTINGS_FILTER_OPTION_VALUE_DEFAULT));
            String userFilterOrder = userPrefs.getString(Constants.PREF_FILTER_ORDER, getString(R.string.SETTINGS_FILTER_ORDER_VALUE_DEFAULT));

            switch (Integer.parseInt(userFilterOption)) {
                case 0:
                    itemName.setChecked(true);
                    orderOption = 0;
                    // Get the ID of the item selected previously of the new one
                    lastSelectedFilterItem = itemName.getItemId();
                    break;
                case 1:
                    itemSymbol.setChecked(true);
                    orderOption = 1;
                    // Get the ID of the item selected previously of the new one
                    lastSelectedFilterItem = itemSymbol.getItemId();
                    break;
                case 2:
                    itemPrice.setChecked(true);
                    orderOption = 2;
                    // Get the ID of the item selected previously of the new one
                    lastSelectedFilterItem = itemPrice.getItemId();
                    break;
                case 3:
                    itemPercentage.setChecked(true);
                    orderOption = 3;
                    // Get the ID of the item selected previously of the new one
                    lastSelectedFilterItem = itemPercentage.getItemId();
                    break;
                default:
                    break;
            }
            switch (Integer.parseInt(userFilterOrder)) {
                case 0:
                    itemAscending.setChecked(true);
                    orderFilter = 1;
                    break;
                case 1:
                    itemDescending.setChecked(true);
                    orderFilter = 1;
                    break;
                default:
                    break;
            }

            // Set first run to false
            DataClass.firstRun = false;
        }
        // If it's not the first run, load the previously saved variables
        else {
            switch (orderOption) {
                case 0:
                    itemName.setChecked(true);
                    break;
                case 1:
                    itemSymbol.setChecked(true);
                    break;
                case 2:
                    itemPrice.setChecked(true);
                    break;
                case 3:
                    itemPercentage.setChecked(true);
                    break;
                default:
                    break;
            }
            switch (orderFilter) {
                case 0:
                    itemAscending.setChecked(true);
                    break;
                case 1:
                    itemDescending.setChecked(true);
                    break;
                default:
                    break;
            }
            // Get the ID of the item selected previously of the new one
            lastSelectedFilterItem = itemName.getItemId();
        }

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
                // Apply the query as the user makes some change on the filter (writes something)
                rwCryptoAdapter.getFilter().filter(query);
                return false;
            }
        });
        itemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Open the search
                Log.d("itemSearch", "Opened search");
                viewSearch.onActionViewExpanded();
                return true; // True to be able to open
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Close the search, empty the field and clear the focus
                Log.d("itemSearch", "Closed search");
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
        //  Get the ID of the selected item
        int itemId = item.getItemId();

        // Depending the selected item, change the variable orderOption or orderFilter
        switch (itemId) {
            case R.id.mnFilterOptionName:
                if (lastSelectedFilterItem == R.id.mnFilterOptionName) {
                    if (itemAscending.isChecked()) {
                        itemDescending.setChecked(true);
                        orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                orderOption = 0;
                break;
            case R.id.mnFilterOptionSymbol:
                if (lastSelectedFilterItem == R.id.mnFilterOptionSymbol) {
                    if (itemAscending.isChecked()) {
                        itemDescending.setChecked(true);
                        orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                orderOption = 1;
                break;
            case R.id.mnFilterOptionPrice:
                if (lastSelectedFilterItem == R.id.mnFilterOptionPrice) {
                    if (itemAscending.isChecked()) {
                        itemDescending.setChecked(true);
                        orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                orderOption = 2;
                break;
            case R.id.mnFilterOptionPercentage:
                if (lastSelectedFilterItem == R.id.mnFilterOptionPercentage) {
                    if (itemAscending.isChecked()) {
                        itemDescending.setChecked(true);
                        orderFilter = 1;
                    } else {
                        itemAscending.setChecked(true);
                        orderFilter = 0;
                    }
                } else {
                    item.setChecked(true);
                    itemAscending.setChecked(true);
                    orderFilter = 0;
                }
                lastSelectedFilterItem = itemId;
                orderOption = 3;
                break;
            case R.id.mnFilterOrderAscending:
                itemAscending.setChecked(true);
                orderFilter = 0;
                if (itemName.isChecked()) {
                    orderOption = 0;
                } else if (itemSymbol.isChecked()) {
                    orderOption = 1;
                } else if (itemPrice.isChecked()) {
                    orderOption = 2;
                } else {
                    orderOption = 3;
                }
                break;
            case R.id.mnFilterOrderDescending:
                itemDescending.setChecked(true);
                orderFilter = 1;
                if (itemName.isChecked()) {
                    orderOption = 0;
                } else if (itemSymbol.isChecked()) {
                    orderOption = 1;
                } else if (itemPrice.isChecked()) {
                    orderOption = 2;
                } else {
                    orderOption = 3;
                }
                break;
            default:
                break;
        }

        if (itemId == R.id.mnFilterOptionName || itemId == R.id.mnFilterOptionSymbol ||
                itemId == R.id.mnFilterOptionPrice || itemId == R.id.mnFilterOptionPercentage ||
                itemId == R.id.mnFilterOrderAscending || itemId == R.id.mnFilterOrderDescending) {
            changeSortRecyclerView(orderOption, orderFilter);
        }
        return true;
    }

    // Add an ItemTouchHelper to the RecyclerView, no matter if swipes to left or right
    ItemTouchHelper.SimpleCallback ithCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // Get the item swiped and the margin size of the icon
            View itemView = viewHolder.itemView;
            int iconMargin = (itemView.getHeight() - rwSwipeIcon.getIntrinsicHeight()) / 2;

            // If the item is swiped to the left
            if (dX > 0) {
                rwSwipeBackground.setBounds(itemView.getLeft(), itemView.getTop(), Math.round(dX), itemView.getBottom());
                rwSwipeIcon.setBounds(itemView.getLeft() + iconMargin, itemView.getTop() + iconMargin,
                        itemView.getLeft() + iconMargin + rwSwipeIcon.getIntrinsicHeight(), itemView.getBottom() - iconMargin);
            }
            // If the item is swiped to the right
            else {
                rwSwipeBackground.setBounds(itemView.getRight() + Math.round(dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                rwSwipeIcon.setBounds(itemView.getRight() - iconMargin - rwSwipeIcon.getIntrinsicHeight(), itemView.getTop() + iconMargin,
                        itemView.getRight() - iconMargin, itemView.getBottom() - iconMargin);
            }

            // Finally draw the background
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
            // Get the position and the crypto symbol of the item
            int position = viewHolder.getBindingAdapterPosition();
            String cryptoSymbol = dc.cryptoList.get(position).getSymbol();
            Log.d("itemSwipe", "Item position: " + position + " - Item symbol: " + cryptoSymbol);

            // Add the item to the database, at the Favorites table (cryptoSymbol and the  current date)
            int resultInsert = DataClass.db.insertToFavorites(cryptoSymbol,
                    new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis()));

            // Manage the returned int
            switch (resultInsert) {
                case -1:
                    // The item was already in the database
                    rwCryptoAdapter.notifyItemChanged(position);
                    Snackbar.make(viewHolder.itemView, cryptoSymbol + " already in favorites",
                            Snackbar.LENGTH_LONG).show();
                    break;
                case 0:
                    // The item couldn't be added to the database
                    Snackbar.make(viewHolder.itemView, "An error occurred while adding " + cryptoSymbol + " to favorites",
                            Snackbar.LENGTH_LONG).show();
                    break;
                case 1:
                    // The item has been added to the database successfully. Add the action to undo the action
                    rwCryptoAdapter.notifyItemChanged(position);
                    Snackbar.make(viewHolder.itemView, cryptoSymbol + " added to favorites",
                            Snackbar.LENGTH_LONG).setAction("UNDO", v -> {
                        // When undo is clicked, delete the item from table Favorites
                        int resultDelete = DataClass.db.deleteFromFavorites(cryptoSymbol);

                        // Manage the returned int
                        switch (resultDelete) {
                            case 0:
                                // The item couldn't be deleted
                                Toast.makeText(getActivity(), cryptoSymbol + " couldn't be removed", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                // The item has been deleted successfully
                                Toast.makeText(getActivity(), cryptoSymbol + " removed from Favorites successfully", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void loadDataCrypto() {
        // Showing refresh animation
        srlReloadList.setRefreshing(true);

        // Start API IntentService, att
        Intent intent = new Intent(ContextApplication.getAppContext(),
                FetchDataAPI.class);
        ListResultReceiver resultReceiver = new ListResultReceiver(new Handler());
        intent.putExtra("receiver", resultReceiver);
        ContextApplication.getAppContext().startService(intent);

        // Check cryptoList size
        Log.d("loadDataCrypto", "cryptoList size: " + dc.cryptoList.size());
    }

    private void postFetchAPI() {
        changeSortRecyclerView(orderOption, orderFilter);

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(getContext());
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(getContext(), new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);

        stopReloadingMarket();
    }

    private void changeSortRecyclerView(int type, int order) {
        // Reorder cryptoList
        switch (type) {
            case 0:
                // Ordered by name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getName));
                    if (order == 1) {
                        // Ordered descending
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 1:
                // Ordered by symbol
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getSymbol));
                    if (order == 1) {
                        // Ordered descending
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 2:
                // Ordered by price
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getCurrentPrice));
                    if (order == 1) {
                        // Ordered descending
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            case 3:
                // Ordered by change percentage
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Crypto::getPriceChangePercentage24h));
                    if (order == 1) {
                        // Ordered descending
                        Collections.reverse(dc.cryptoList);
                    }
                }
                break;
            default:
                break;
        }
        // Showing refresh animation before making api call
        srlReloadList.setRefreshing(true);

        // Check cryptoList size
        Log.d("changeSortRecyclerView", "cryptoList size: " + dc.cryptoList.size());

        // Initialize RecyclerView manager and adapter
        rwCryptoManager = new LinearLayoutManager(getContext());
        rwCryptoAdapter = new RecyclerViewCryptoListAdapter(getContext(), new ArrayList<>(dc.cryptoList));

        // Set RecyclerView manager and adapter
        rwCrypto.setHasFixedSize(true);
        rwCrypto.setLayoutManager(rwCryptoManager);
        rwCrypto.setAdapter(rwCryptoAdapter);
        new ItemTouchHelper(ithCallback).attachToRecyclerView(rwCrypto);

        // Stopping swipe refresh
        srlReloadList.setRefreshing(false);
    }

    private void stopReloadingMarket() {
        // Stopping swipe refresh
        srlReloadList.setRefreshing(false);
    }

    private void references(View view) {
        rwCrypto = view.findViewById(R.id.rwMarketCryptoList);
        srlReloadList = view.findViewById(R.id.srlMarketReload);
    }

    private void referencesOptionsMenu(Menu menu) {
        itemName = menu.findItem(R.id.mnFilterOptionName);
        itemSymbol = menu.findItem(R.id.mnFilterOptionSymbol);
        itemPrice = menu.findItem(R.id.mnFilterOptionPrice);
        itemPercentage = menu.findItem(R.id.mnFilterOptionPercentage);
        itemAscending = menu.findItem(R.id.mnFilterOrderAscending);
        itemDescending = menu.findItem(R.id.mnFilterOrderDescending);
    }

    private class ListResultReceiver extends ResultReceiver {

        public ListResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Manage when data has been fetched from the API (or an error has occurred)
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

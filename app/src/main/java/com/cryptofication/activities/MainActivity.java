package com.cryptofication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    final private DataClass dc = new DataClass();

    private RecyclerView rwCrypto;
    private SwipeRefreshLayout srlReloadData;
    private RecyclerViewCryptoListAdapter rwCryptoAdapter;
    private RecyclerView.LayoutManager rwCryptoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
        setContentView(R.layout.activity_main);
        references();

        // Insert custom toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar_home);
        getSupportActionBar().setElevation(10);

        // SwipeRefreshLayout listener and customization
        srlReloadData.setOnRefreshListener(this);
        srlReloadData.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        srlReloadData.post(() -> {
            // Fetching data from server
            loadDataCrypto();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu and find items on it
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
                return true; // KEEP IT TO TRUE OR IT WON'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("MainActivity", "Closed search");
                rwCryptoAdapter.getFilter().filter("");
                searchView.onActionViewCollapsed();
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnFilterOptionName:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    changeSortRecyclerView(1);
                }
                return true;
            case R.id.mnFilterOptionSymbol:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    changeSortRecyclerView(2);
                }
                return true;
            case R.id.mnFilterOptionPrice:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    changeSortRecyclerView(3);
                }
                return true;
            case R.id.mnManageFavorites:
                return true;
            case R.id.mnAbout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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

    private void changeSortRecyclerView(int type) {
        switch (type) {
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Post::getName));
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Post::getSymbol));
                }
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dc.cryptoList.sort(Comparator.comparing(Post::getCurrentPrice));
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
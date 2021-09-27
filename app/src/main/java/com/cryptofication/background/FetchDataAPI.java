package com.cryptofication.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cryptofication.R;
import com.cryptofication.classes.Constants;
import com.cryptofication.classes.ContextApplication;
import com.cryptofication.interfaces.CoinGeckoAPI;
import com.cryptofication.objects.Crypto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchDataAPI extends IntentService {

    public FetchDataAPI() {
        super(FetchDataAPI.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Initialize the receiver and prepare it to bo able to send data
        ResultReceiver receiver = null;
        if (intent != null) {
            receiver = intent.getParcelableExtra("receiver");
        }
        Bundle bundleToReceiver = new Bundle();

        // Build an OkHttpClient for timeouts
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        // Build the retrofit adding the CoinGecko base url, he OkHttpClient and GsonConverter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the CoinGeckoAPI interface
        CoinGeckoAPI coinGeckoAPI = retrofit.create(CoinGeckoAPI.class);

        String userCurrency, userItemsPage;

        // Get the default currency and items per page from the SharedPreferences
        SharedPreferences sharedPreferences = ContextApplication.getAppContext().getSharedPreferences(ContextApplication.getAppContext().
                getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
        Log.d("SharedPreferences", sharedPreferences.getString("prefCurrency", ""));
        userCurrency = sharedPreferences.getString(Constants.PREF_CURRENCY, getString(R.string.SETTINGS_CURRENCY_VALUE_DEFAULT));
        userItemsPage = sharedPreferences.getString(Constants.PREF_ITEMS_PAGE, getString(R.string.SETTINGS_ITEMS_PAGE_VALUE_DEFAULT));
        Log.d("FetchDataAPI - Objects", String.valueOf(userCurrency));

        // Make the call to the API
        Call<List<Crypto>> call = coinGeckoAPI.getPosts(String.valueOf(userCurrency), "market_cap_desc", userItemsPage, "1", "false");
        Log.d("BackgroundService", "Call received: " + call);

        List<Crypto> cryptoList = new ArrayList<>();
        try {
            // Manage the API response whether it is successful or not
            Response<List<Crypto>> response = call.execute();
            if (!response.isSuccessful()) {
                // If is not successful send an empty Bundle
                Log.d("BackgroundService", String.valueOf(response.code()));
                receiver.send(1, Bundle.EMPTY);
            } else {
                // If it is successful send the List of crypto
                List<Crypto> postsList = response.body();
                for (Crypto post : postsList) {
                    Log.d("BackgroundService", post.toString());
                    cryptoList.add(post);
                }
                Log.d("BackgroundCrypto", String.valueOf(cryptoList.size()));
                bundleToReceiver.putParcelableArrayList("cryptoList", new ArrayList<Crypto>(cryptoList));
                receiver.send(0, bundleToReceiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("BackgroundService", "IOException");
            receiver.send(1, Bundle.EMPTY);
        }
    }
}

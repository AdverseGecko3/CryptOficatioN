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
        ResultReceiver receiver = null;
        if (intent != null) {
            receiver = intent.getParcelableExtra("receiver");
        }
        Bundle bundleToReceiver = new Bundle();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoinGeckoAPI coinGeckoAPI = retrofit.create(CoinGeckoAPI.class);
        String userCurrency = "";
        SharedPreferences sharedPreferences = ContextApplication.getAppContext().getSharedPreferences(ContextApplication.getAppContext().
                getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
        Log.d("SharedPreferences", sharedPreferences.getString("prefCurrency", ""));
        userCurrency = sharedPreferences.getString(Constants.PREF_CURRENCY, "");
        Log.d("FetchDataAPI - Objects", String.valueOf(userCurrency));
        Call<List<Crypto>> call = coinGeckoAPI.getPosts(String.valueOf(userCurrency), "market_cap_desc", "250", "1", "false");
        Log.d("BackgroundService", "Call received: " + call);

        List<Crypto> cryptoList = new ArrayList<>();
        try {
            Response<List<Crypto>> response = call.execute();
            if (!response.isSuccessful()) {
                Log.d("BackgroundService", String.valueOf(response.code()));
                receiver.send(1, Bundle.EMPTY);
            } else {
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
        }
    }
}

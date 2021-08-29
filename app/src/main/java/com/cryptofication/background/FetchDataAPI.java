package com.cryptofication.background;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.cryptofication.R;
import com.cryptofication.classes.ContextApplication;
import com.cryptofication.interfaces.CoinGeckoAPI;
import com.cryptofication.objects.Crypto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchDataAPI extends AsyncTask {

    @Override
    protected List<Crypto> doInBackground(Object[] objects) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoinGeckoAPI coinGeckoAPI = retrofit.create(CoinGeckoAPI.class);
        Log.d("objetitos", String.valueOf(objects[0]));
        Call<List<Crypto>> call = coinGeckoAPI.getPosts(String.valueOf(objects[0]), "market_cap_desc", "250", "1", "false");
        Log.d("BackgroundService", "Call received" + call);

        List<Crypto> cryptoList = new ArrayList<>();

        try {
            Response<List<Crypto>> response = call.execute();
            if (!response.isSuccessful()) {
                Log.d("BackgroundService", String.valueOf(response.code()));
            } else {
                List<Crypto> postsList = response.body();
                Log.d("BackgroundService", postsList.toString());
                for (Crypto post : postsList) {
                    Log.d("BackgroundService", post.toString());
                    cryptoList.add(post);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cryptoList;
    }
}

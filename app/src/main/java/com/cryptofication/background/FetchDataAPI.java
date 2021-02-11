package com.cryptofication.background;

import android.os.AsyncTask;
import android.util.Log;

import com.cryptofication.interfaces.CoinGeckoAPI;
import com.cryptofication.objects.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchDataAPI extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        CoinGeckoAPI coinGeckoAPI = retrofit.create(CoinGeckoAPI.class);

        Call<List<Post>> call = coinGeckoAPI.getPosts();
        Log.d("BackgroundService", "Call received" + call);

        List<Post> cryptoList = new ArrayList<>();

        try {
            Response<List<Post>> response = call.execute();
            if (!response.isSuccessful()) {
                Log.d("BackgroundService", String.valueOf(response.code()));
            } else {
                List<Post> postsList = response.body();
                Log.d("BackgroundService", postsList.toString());
                for (Post post: postsList) {
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

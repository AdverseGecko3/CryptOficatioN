package com.cryptofication.interfaces;

import com.cryptofication.objects.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinGeckoAPI {

    @GET("coins/markets?vs_currency=eur&order=market_cap_desc&per_page=250&page=1&sparkline=false")
    Call<List<Post>> getPosts();
}

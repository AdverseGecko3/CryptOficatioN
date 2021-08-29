package com.cryptofication.interfaces;

import com.cryptofication.objects.Crypto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinGeckoAPI {
    /* Required params: vs_currency, order, per_page, page, sparkline
    Check the params at https://www.coingecko.com/es/api/documentation
    ?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=false */
    @GET("coins/markets")
    Call<List<Crypto>> getPosts(@Query("vs_currency") String currency, @Query("order") String order,
                                @Query("per_page") String perPage, @Query("page") String page,
                                @Query("sparkline") String sparkline);

}

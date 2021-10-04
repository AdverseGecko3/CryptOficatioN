package com.cryptofication.background;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cryptofication.objects.Crypto;

import java.util.ArrayList;
import java.util.List;

public class FetchDataAPIService extends IntentService {

    public FetchDataAPIService() {
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

        List<Crypto> cryptoList = new FetchDataAPI().getDataAPI();
        Log.d("FetchDataAPI - Service", String.valueOf(cryptoList.size()));

        if (cryptoList.isEmpty()) {
            if (receiver != null) {
                receiver.send(1, Bundle.EMPTY);
            }
        } else {
            if (receiver != null) {
                bundleToReceiver.putParcelableArrayList("cryptoList", new ArrayList<>(cryptoList));
                receiver.send(0, bundleToReceiver);
            }
        }
    }
}
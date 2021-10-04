package com.cryptofication.activities;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptofication.R;
import com.cryptofication.background.FetchDataAPI;
import com.cryptofication.classes.ContextApplication;
import com.cryptofication.dialogs.DialogLoading;
import com.cryptofication.fragments.FragmentMarket;
import com.cryptofication.objects.Crypto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends Activity {

    private final int SPLASH_DURATION = 3000;

    private ImageView ivLogo;
    private TextView tvPowered;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        references();
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        ivLogo.setAnimation(anim);
        tvPowered.setAnimation(anim);

        final Intent intent = new Intent(this, MainActivity.class);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                sleep(SPLASH_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // onPreExecute
            runOnUiThread(() -> loadingDialog = new DialogLoading().showDialog(SplashActivity.this));

            // Start API IntentService, att
            List<Crypto> cryptoList = new FetchDataAPI().getDataAPI();

            // Check cryptoList size
            Log.d("loadDataCryptoSplash", "cryptoList size: " + cryptoList.size());

            // onPostExecute
            runOnUiThread(() -> loadingDialog.dismiss());

            intent.putParcelableArrayListExtra("cryptoData", (ArrayList<Crypto>) cryptoList);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
        });
    }

    private void references() {
        ivLogo = findViewById(R.id.ivSplashLogo);
        tvPowered = findViewById(R.id.tvSplashPoweredBy);
    }
}
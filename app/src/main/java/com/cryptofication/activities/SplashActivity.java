package com.cryptofication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.cryptofication.R;
import com.cryptofication.classes.Preferences;

public class SplashActivity extends Activity {

    private final int SPLASH_DURATION = 2000;

    private ImageView ivLogo;
    private TextView tvPowered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Preferences preferences = new Preferences();
        if (preferences.loadScheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        references();
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        ivLogo.setAnimation(anim);
        tvPowered.setAnimation(anim);

        final Intent intent = new Intent(this, MainActivity.class);
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent.putExtra("lastActivity", "splash"));
                    overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
                    finish();
                }
            }
        };
        timer.start();
    }

    private void references() {
        ivLogo = findViewById(R.id.ivSplashLogo);
        tvPowered = findViewById(R.id.tvSplashPoweredBy);
    }
}
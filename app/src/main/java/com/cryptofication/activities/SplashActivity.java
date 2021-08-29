package com.cryptofication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cryptofication.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DURATION = 3000;

    private ImageView ivLogo;
    private TextView tvPowered;

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
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
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
package com.cryptofication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptofication.R;
import com.cryptofication.classes.Constants;
import com.cryptofication.classes.DataClass;
import com.cryptofication.classes.DatabaseClass;
import com.cryptofication.fragments.FragmentMarket;
import com.cryptofication.fragments.FragmentSettings;
import com.cryptofication.fragments.FragmentFavorites;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private int fragmentShow;

    private final FragmentMarket fragmentMarket = new FragmentMarket();
    private final FragmentFavorites fragmentFavorites = new FragmentFavorites();
    private final FragmentSettings fragmentSettings = new FragmentSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
        setContentView(R.layout.activity_main);
        references();
        Log.d("firstRunMainCreate", "App first run");

        // Initialize database
        DataClass.db = new DatabaseClass(this,"CryptOficatioN Database", null, 1);

        BottomNavigationView bottomNavigation = findViewById(R.id.navBottom);
        bottomNavigation.setSelectedItemId(Constants.MARKETS);
        DataClass.newItem = bottomNavigation.getSelectedItemId();
        bottomNavigation.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
                .replace(fragmentShow, fragmentMarket)
                .disallowAddToBackStack()
                .commit();
    }

    private void references() {
        fragmentShow = R.id.fragmentShow;
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case Constants.MARKETS:
                        selectedFragment = fragmentMarket;
                        DataClass.oldItem = DataClass.newItem;
                        DataClass.newItem = Constants.MARKETS;
                        break;
                    case Constants.FAVORITES:
                        selectedFragment = fragmentFavorites;
                        DataClass.oldItem = DataClass.newItem;
                        DataClass.newItem = Constants.FAVORITES;
                        break;
                    case Constants.SETTINGS:
                        selectedFragment = fragmentSettings;
                        DataClass.oldItem = DataClass.newItem;
                        DataClass.newItem = Constants.SETTINGS;
                        break;
                    default:
                        break;
                }
                fragmentTransaction(selectedFragment);
                return true;
            };

    private void fragmentTransaction(Fragment selectedFragment) {
        switch (DataClass.oldItem) {
            case Constants.MARKETS:
                switch (DataClass.newItem) {
                    case Constants.MARKETS:
                        getSupportFragmentManager().beginTransaction()
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    case Constants.FAVORITES:
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    default:
                        break;
                }
                break;
            case Constants.FAVORITES:
                switch (DataClass.newItem) {
                    case Constants.MARKETS:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    case Constants.FAVORITES:
                        getSupportFragmentManager().beginTransaction()
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    default:
                        break;
                }
                break;
            case Constants.SETTINGS:
                switch (DataClass.newItem) {
                    case Constants.MARKETS:
                    case Constants.FAVORITES:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction()
                                .replace(fragmentShow, selectedFragment)
                                .disallowAddToBackStack()
                                .commit();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Create dialog to confirm the dismiss
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        TextView titleExit = new TextView(this);
        titleExit.setText(getString(R.string.EXIT));
        titleExit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        titleExit.setGravity(Gravity.CENTER);
        titleExit.setTextSize(25);
        titleExit.setPadding(titleExit.getLineHeight() / 2, titleExit.getLineHeight() / 2,
                titleExit.getLineHeight() / 2, titleExit.getLineHeight() / 2);

        builder.setCustomTitle(titleExit)
                .setMessage(getString(R.string.CONFIRMATION_EXIT))
                .setNegativeButton(getString(R.string.NO), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.YES), (dialog, which) -> super.onBackPressed())
                .create();
        final AlertDialog dialog = builder.show();

        // Change the buttons color and weight
        Button btnYes = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNo = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnYes.setTextColor(ResourcesCompat.getColor(getResources(), R.color.purple_app_accent, null));
        btnNo.setTextColor(ResourcesCompat.getColor(getResources(), R.color.purple_app_accent, null));

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnYes.getLayoutParams();
        layoutParams.weight = 10;
        btnYes.setLayoutParams(layoutParams);
        btnNo.setLayoutParams(layoutParams);

        // Show the dialog
        dialog.show();
    }
}
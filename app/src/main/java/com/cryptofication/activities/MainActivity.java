package com.cryptofication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.cryptofication.R;
import com.cryptofication.classes.Constants;
import com.cryptofication.classes.DataClass;
import com.cryptofication.classes.DatabaseClass;
import com.cryptofication.fragments.FragmentMarket;
import com.cryptofication.fragments.FragmentTrade;
import com.cryptofication.fragments.FragmentSettings;
import com.cryptofication.fragments.FragmentFavorites;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static DatabaseClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in_slow, R.anim.anim_fade_out_slow);
        setContentView(R.layout.activity_main);
        db = new DatabaseClass(this,"Gecko's Database", null, 1);
        BottomNavigationView bot_nav = findViewById(R.id.nav_bottom);
        bot_nav.setSelectedItemId(Constants.MARKETS);
        DataClass.newItem = bot_nav.getSelectedItemId();
        bot_nav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentShow,
                new FragmentMarket()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case Constants.MARKETS:
                        selectedFragment = new FragmentMarket();
                        DataClass.oldItem = DataClass.newItem;
                        DataClass.newItem = Constants.MARKETS;
                        break;
                    case Constants.CONVERSION:
                        selectedFragment = new FragmentTrade();
                        DataClass.oldItem = DataClass.newItem;
                        DataClass.newItem = Constants.CONVERSION;
                        break;
                    case Constants.FAVORITES:
                        selectedFragment = new FragmentFavorites();
                        DataClass.oldItem = DataClass.newItem;
                        DataClass.newItem = Constants.FAVORITES;
                        break;
                    case Constants.SETTINGS:
                        selectedFragment = new FragmentSettings();
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    case Constants.CONVERSION:
                    case Constants.FAVORITES:
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    default:
                        break;
                }
                break;
            case Constants.CONVERSION:
                switch (DataClass.newItem) {
                    case Constants.MARKETS:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    case Constants.CONVERSION:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    case Constants.FAVORITES:
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    default:
                        break;
                }
                break;
            case Constants.FAVORITES:
                switch (DataClass.newItem) {
                    case Constants.MARKETS:
                    case Constants.CONVERSION:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    case Constants.FAVORITES:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    default:
                        break;
                }
                break;
            case Constants.SETTINGS:
                switch (DataClass.newItem) {
                    case Constants.MARKETS:
                    case Constants.CONVERSION:
                    case Constants.FAVORITES:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    case Constants.SETTINGS:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentShow,
                                selectedFragment).commit();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
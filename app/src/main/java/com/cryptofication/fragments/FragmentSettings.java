package com.cryptofication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.cryptofication.R;
import com.cryptofication.classes.Constants;
import com.cryptofication.classes.ContextApplication;

public class FragmentSettings extends PreferenceFragmentCompat {

    private ListPreference lpCurrency, lpFilterOption, lpFilterOrder, lpItemsPage;
    private SwitchPreference spScheme;

    private SharedPreferences userPrefs;
    private SharedPreferences.Editor userPrefsEditor;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        references();
        loadPreferences();
        userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
        preferenceChangeListener = (sharedPreferences, key) -> {

            switch (key) {
                case Constants.PREF_CURRENCY:
                    Log.d("prefSelected", lpCurrency.getTitle() + " - " + lpCurrency.getValue());
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putString(Constants.PREF_CURRENCY, lpCurrency.getValue());
                    userPrefsEditor.apply();
                    break;
                case Constants.PREF_SCHEME:
                    Log.d("prefSelected", spScheme.getTitle() + " - " + spScheme.isChecked());
                    userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                            getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putBoolean(Constants.PREF_SCHEME, spScheme.isChecked());
                    /*if (spScheme.isChecked()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }*/
                    userPrefsEditor.apply();
                    break;
                case Constants.PREF_FILTER_OPTION:
                    Log.d("prefSelected", lpFilterOption.getTitle() + " - " + lpFilterOption.getValue());
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putString(Constants.PREF_FILTER_OPTION, lpFilterOption.getValue());
                    userPrefsEditor.apply();
                    break;
                case Constants.PREF_FILTER_ORDER:
                    Log.d("prefSelected", lpFilterOrder.getTitle() + " - " + lpFilterOrder.getValue());
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putString(Constants.PREF_FILTER_ORDER, lpFilterOrder.getValue());
                    userPrefsEditor.apply();
                    break;
                case Constants.PREF_ITEMS_PAGE:
                    Log.d("prefSelected", lpItemsPage.getTitle() + " - " + lpItemsPage.getValue());
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putString(Constants.PREF_ITEMS_PAGE, lpItemsPage.getValue());
                    userPrefsEditor.apply();
                    break;
                default:
                    break;
            }
        };
    }

    private void references() {
        lpCurrency = findPreference(Constants.PREF_CURRENCY);
        spScheme = findPreference(Constants.PREF_SCHEME);
        lpFilterOption = findPreference(Constants.PREF_FILTER_OPTION);
        lpFilterOrder = findPreference(Constants.PREF_FILTER_ORDER);
        lpItemsPage = findPreference(Constants.PREF_ITEMS_PAGE);
    }

    private void loadPreferences() {
        userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
        String userCurrency = userPrefs.getString(Constants.PREF_CURRENCY, getString(R.string.SETTINGS_CURRENCY_VALUE_DEFAULT));
        boolean userScheme = userPrefs.getBoolean(Constants.PREF_SCHEME, true);
        String userFilterOption = userPrefs.getString(Constants.PREF_FILTER_OPTION, getString(R.string.SETTINGS_FILTER_OPTION_VALUE_DEFAULT));
        String userFilterOrder = userPrefs.getString(Constants.PREF_FILTER_ORDER, getString(R.string.SETTINGS_FILTER_ORDER_VALUE_DEFAULT));
        String userItemsPage = userPrefs.getString(Constants.PREF_ITEMS_PAGE, getString(R.string.SETTINGS_ITEMS_PAGE_VALUE_DEFAULT));

        lpCurrency.setValue(userCurrency);
        spScheme.setChecked(userScheme);
        lpFilterOption.setValue(userFilterOption);
        lpFilterOrder.setValue(userFilterOrder);
        lpItemsPage.setValue(userItemsPage);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}

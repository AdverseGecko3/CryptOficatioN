package com.cryptofication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.cryptofication.R;
import com.cryptofication.classes.ContextApplication;

public class FragmentSettings extends PreferenceFragmentCompat {

    private ListPreference lpCurrency;
    private SwitchPreference spScheme;

    private SharedPreferences userPrefs;
    private SharedPreferences.Editor userPrefsEditor;

    public static final String PREF_CURRENCY = "prefCurrency";
    public static final String PREF_SCHEME = "prefScheme";

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        references();
        loadPreferences();
        preferenceChangeListener = (sharedPreferences, key) -> {

            switch (key) {
                case PREF_CURRENCY:
                    Log.d("prefSelected", lpCurrency.getTitle() + " - " + lpCurrency.getValue());
                    userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                            getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putString(PREF_CURRENCY, lpCurrency.getValue());
                    userPrefsEditor.apply();
                    break;
                case PREF_SCHEME:
                    Log.d("prefSelected", spScheme.getTitle() + " - " + spScheme.isChecked());
                    userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                            getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
                    userPrefsEditor = userPrefs.edit();
                    userPrefsEditor.putBoolean(PREF_SCHEME, spScheme.isChecked());
                    if (spScheme.isChecked()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    userPrefsEditor.apply();
                    break;
                default:
                    break;
            }
        };
    }

    private void references() {
        lpCurrency = findPreference(PREF_CURRENCY);
        spScheme = findPreference(PREF_SCHEME);
    }

    private void loadPreferences() {
        userPrefs = requireActivity().getSharedPreferences(ContextApplication.getAppContext().
                getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
        String userCurrency = userPrefs.getString(PREF_CURRENCY, "usd");
        boolean userScheme = userPrefs.getBoolean(PREF_SCHEME, true);

        lpCurrency.setValue(userCurrency);
        spScheme.setChecked(userScheme);
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

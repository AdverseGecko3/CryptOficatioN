package com.cryptofication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.cryptofication.R;

import java.util.Objects;

public class FragmentSettings extends PreferenceFragmentCompat {

    public static final String PREF_CURRENCY = "prefCurrency";

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        /*preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(PREF_CURRENCY))  {
                SharedPreferences userPrefs = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
                userPrefsEditor.putString(PREF_CURRENCY, getSharedPreferences);
            }
        };*/
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

package com.cryptofication.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.cryptofication.R;
import com.cryptofication.classes.Constants;
import com.cryptofication.classes.ContextApplication;

public class FragmentSettings extends PreferenceFragmentCompat {

    private ListPreference lpCurrency, lpFilterOption, lpFilterOrder, lpItemsPage;
    private SwitchPreference spScheme;
    private Preference pAbout, pCredits;

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

        pAbout.setOnPreferenceClickListener(preference -> {
            // Create dialog to confirm the dismiss
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    R.style.CustomAlertDialog);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_about, null);
            builder.setView(dialogView);
            builder.setNeutralButton(getString(R.string.CLOSE), (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the button color and weight
            Button btnDismiss = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            btnDismiss.setTextColor(ResourcesCompat.getColor(getResources(), R.color.purple_toolbar, null));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnDismiss.getLayoutParams();
            layoutParams.weight = 10;
            btnDismiss.setLayoutParams(layoutParams);

            ImageView ivLinkedIn = dialog.findViewById(R.id.ivDialogAboutLinkedIn);
            ImageView ivInstagram = dialog.findViewById(R.id.ivDialogAboutInstagram);
            ImageView ivTwitter = dialog.findViewById(R.id.ivDialogAboutTwitter);

            if (ivLinkedIn != null) {
                ivLinkedIn.setOnClickListener(view -> {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://in/eric-barrero")));
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/eric-barrero")));
                    }
                });
            }
            if (ivInstagram != null) {
                ivInstagram.setOnClickListener(view -> {
                    Intent intentInstagram = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/adversegecko3"));
                    intentInstagram.setPackage("com.instagram.android");
                    try {
                        startActivity(intentInstagram);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/adversegecko3")));
                    }
                });
            }
            if (ivTwitter != null) {
                ivTwitter.setOnClickListener(view -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=adversegecko3")));
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/adversegecko3")));
                    }
                });
            }

            // Show the dialog
            dialog.show();
            return false;
        });
        pCredits.setOnPreferenceClickListener(preference -> {
            // Create dialog to confirm the dismiss
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    R.style.CustomAlertDialog);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_credits, null);
            builder.setView(dialogView);
            builder.setNeutralButton(getString(R.string.CLOSE), (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the button color and weight
            Button btnDismiss = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            btnDismiss.setTextColor(ResourcesCompat.getColor(getResources(), R.color.purple_toolbar, null));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnDismiss.getLayoutParams();
            layoutParams.weight = 10;
            btnDismiss.setLayoutParams(layoutParams);

            // Show the dialog
            dialog.show();
            return false;
        });
    }

    private void references() {
        lpCurrency = findPreference(Constants.PREF_CURRENCY);
        spScheme = findPreference(Constants.PREF_SCHEME);
        lpFilterOption = findPreference(Constants.PREF_FILTER_OPTION);
        lpFilterOrder = findPreference(Constants.PREF_FILTER_ORDER);
        lpItemsPage = findPreference(Constants.PREF_ITEMS_PAGE);
        pAbout = findPreference(Constants.PREF_ABOUT);
        pCredits = findPreference(Constants.PREF_CREDITS);
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

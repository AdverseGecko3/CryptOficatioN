package com.cryptofication.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.cryptofication.R;
import com.cryptofication.classes.Constants;
import com.cryptofication.classes.Preferences;

import java.util.List;
import java.util.Objects;

public class FragmentSettings extends PreferenceFragmentCompat {

    private ListPreference lpCurrency, lpFilterOption, lpFilterOrder, lpItemsPage;
    private SwitchPreference spScheme;
    private Preference pAbout, pCredits;

    private final Preferences preferences = new Preferences();

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        references();

        // Insert custom toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowCustomEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setCustomView(R.layout.toolbar_home);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setElevation(10);

        loadPreferences();

        preferenceChangeListener = (sharedPreferences, key) -> {

            switch (key) {
                case Constants.PREF_CURRENCY:
                    Log.d("prefSelected", lpCurrency.getTitle() + " - " + lpCurrency.getValue());
                    preferences.editCurrency(lpCurrency.getValue());
                    break;
                case Constants.PREF_SCHEME:
                    Log.d("prefSelected", spScheme.getTitle() + " - " + spScheme.isChecked());
                    preferences.editScheme(spScheme.isChecked());
                    if (spScheme.isChecked()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    requireActivity().recreate();
                    break;
                case Constants.PREF_FILTER_OPTION:
                    Log.d("prefSelected", lpFilterOption.getTitle() + " - " + lpFilterOption.getValue());
                    preferences.editFilterOption(lpFilterOption.getValue());
                    break;
                case Constants.PREF_FILTER_ORDER:
                    Log.d("prefSelected", lpFilterOrder.getTitle() + " - " + lpFilterOrder.getValue());
                    preferences.editFilterOrder(lpFilterOrder.getValue());
                    break;
                case Constants.PREF_ITEMS_PAGE:
                    Log.d("prefSelected", lpItemsPage.getTitle() + " - " + lpItemsPage.getValue());
                    preferences.editItemsPage(lpItemsPage.getValue());
                    break;
                default:
                    break;
            }
        };

        pAbout.setOnPreferenceClickListener(preference -> {
            // Create dialog to confirm the dismiss
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),
                    R.style.CustomAlertDialog);

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_about, null);
            builder.setView(dialogView);
            builder.setNeutralButton(getString(R.string.CLOSE), (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the button color and weight
            Button btnDismiss = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            btnDismiss.setTextColor(ResourcesCompat.getColor(getResources(), R.color.purple_app_accent, null));
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
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),
                    R.style.CustomAlertDialog);

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_credits, null);
            builder.setView(dialogView);
            builder.setNeutralButton(getString(R.string.CLOSE), (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the button color and weight
            Button btnDismiss = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            btnDismiss.setTextColor(ResourcesCompat.getColor(getResources(), R.color.purple_app_accent, null));
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
        List<Object> listPreferences = preferences.loadPreferences();
        lpCurrency.setValue(String.valueOf(listPreferences.get(0)));
        spScheme.setChecked(Boolean.parseBoolean(listPreferences.get(1).toString()));
        lpFilterOption.setValue(String.valueOf(listPreferences.get(2)));
        lpFilterOrder.setValue(String.valueOf(listPreferences.get(3)));
        lpItemsPage.setValue(String.valueOf(listPreferences.get(4)));
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

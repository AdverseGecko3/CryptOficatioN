package com.cryptofication.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cryptofication.R;

import java.util.ArrayList;
import java.util.List;

public class Preferences {
    private static SharedPreferences userPrefs;

    public Preferences() {
        userPrefs = ContextApplication.getAppContext().getSharedPreferences(ContextApplication.getAppContext().
                getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
    }

    public void editCurrency(String currency) {
        SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
        userPrefsEditor.putString(Constants.PREF_CURRENCY, currency);
        userPrefsEditor.apply();
    }

    public void editScheme(boolean scheme) {
        SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
        userPrefsEditor.putBoolean(Constants.PREF_SCHEME, scheme);
        userPrefsEditor.apply();
    }

    public void editFilterOption(String filterOption) {
        SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
        userPrefsEditor.putString(Constants.PREF_FILTER_OPTION, filterOption);
        userPrefsEditor.apply();
    }

    public void editFilterOrder(String filterOrder) {
        SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
        userPrefsEditor.putString(Constants.PREF_FILTER_ORDER, filterOrder);
        userPrefsEditor.apply();
    }

    public void editItemsPage(String itemsPage) {
        SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
        userPrefsEditor.putString(Constants.PREF_ITEMS_PAGE, itemsPage);
        userPrefsEditor.apply();
    }

    public List<Object> loadPreferences() {
        List<Object> preferences = new ArrayList<>();
        preferences.add(userPrefs.getString(Constants.PREF_CURRENCY,
                ContextApplication.getAppContext().getString(R.string.SETTINGS_CURRENCY_VALUE_DEFAULT)));
        preferences.add(userPrefs.getBoolean(Constants.PREF_SCHEME,
                true));
        preferences.add(userPrefs.getString(Constants.PREF_FILTER_OPTION,
                ContextApplication.getAppContext().getString(R.string.SETTINGS_FILTER_OPTION_VALUE_DEFAULT)));
        preferences.add(userPrefs.getString(Constants.PREF_FILTER_ORDER,
                ContextApplication.getAppContext().getString(R.string.SETTINGS_FILTER_ORDER_VALUE_DEFAULT)));
        preferences.add(userPrefs.getString(Constants.PREF_ITEMS_PAGE,
                ContextApplication.getAppContext().getString(R.string.SETTINGS_ITEMS_PAGE_VALUE_DEFAULT)));
        return preferences;
    }

    public boolean loadScheme() {
        return userPrefs.getBoolean(Constants.PREF_SCHEME, true);
    }

}

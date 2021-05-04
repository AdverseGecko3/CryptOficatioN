package com.cryptofication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cryptofication.classes.ContextApplication;
import com.cryptofication.activities.MainActivity;
import com.cryptofication.R;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentFavorites extends Fragment {

    private Button btSearch;
    private TextInputEditText etPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_favorites, container, false);
        /*references(fView);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(fView);
                etPhone.clearFocus();
                String phone = etPhone.getText().toString();
                String ret = MainActivity.db.searchInDatabase(phone);
                Toast.makeText(getActivity().getApplicationContext(), ret, Toast.LENGTH_SHORT).show();
            }
        });*/
        return fView;
    }

    private void references(View view) {
        /*etPhone = view.findViewById(R.id.etPhoneSearch);
        btSearch = view.findViewById(R.id.btSearch);*/
    }

    private static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) ContextApplication.getAppContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

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

import com.cryptofication.classes.DataClass;
import com.cryptofication.activities.MainActivity;
import com.cryptofication.R;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentTrade extends Fragment {

    private Button btDelete;
    private TextInputEditText etPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_trade, container, false);
        /*references(fView);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(fView);
                etPhone.clearFocus();
                String phone = etPhone.getText().toString();
                int ret = MainActivity.db.deleteFromDatabase(phone);
                Toast.makeText(getActivity().getApplicationContext(), ret + " users have been deleted from database", Toast.LENGTH_SHORT).show();
            }
        });*/
        return fView;
    }

    private void references(View view) {
        /*etPhone = view.findViewById(R.id.etPhoneDelete);
        btDelete = view.findViewById(R.id.btDelete);*/
    }

    private static void hideKeyboard(View view) {
        /*InputMethodManager imm = (InputMethodManager) DataClass.mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
    }
}

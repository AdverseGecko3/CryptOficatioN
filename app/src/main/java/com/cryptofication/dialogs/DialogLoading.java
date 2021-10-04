package com.cryptofication.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.cryptofication.R;

public class DialogLoading {

    public AlertDialog showDialog(Activity activity) {
        // Create dialog to confirm the dismiss
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,
                R.style.CustomAlertDialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);
        builder.create();
        final AlertDialog dialog = builder.show();

        // Show the dialog
        dialog.show();
        return dialog;
    }

    public void dismissDialog(AlertDialog dialog) {
        dialog.dismiss();
    }
}

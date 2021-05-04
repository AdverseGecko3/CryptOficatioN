package com.cryptofication.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cryptofication.activities.MainActivity;
import com.cryptofication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FragmentAbout extends Fragment {

    private ListView svList;
    private TextInputEditText etName, etPhone, etMail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_about, container, false);
        references(fView);
        /*List<String> al = MainActivity.db.listDatabase();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.tvlistview, al);
        svList.setAdapter(arrayAdapter);
        svList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Log.i("idItem", String.valueOf(id));
                View v = (LayoutInflater.from(getActivity().getApplicationContext())).inflate(R.layout.ad_update, null);
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity().getApplicationContext());
                ad.setView(v);
                referencesPrompt(v);
                final String[] dataReceive = MainActivity.db.getRowDatabase(id);
                etName.setText(dataReceive[0]);
                etPhone.setText(dataReceive[1]);
                etMail.setText(dataReceive[2]);
                ad.setCancelable(true)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] dataSend = new String[3];
                                dataSend[0] = etName.getText().toString();
                                dataSend[1] = etPhone.getText().toString();
                                dataSend[2] = etMail.getText().toString();
                                if (dataSend[2].contains("@") && dataSend[2].contains(".")) {
                                    Boolean checkNull = false;
                                    for (int i = 0; i < dataSend.length; i++) {
                                        if (dataSend[i].equals("")) {
                                            checkNull = true;
                                        }
                                    }
                                    if (checkNull) {
                                        Toast.makeText(getActivity().getApplicationContext(), "A field can't be empty", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (dataReceive[0].equals(dataSend[0])) {
                                            String msg = MainActivity.db.updatefromDatabase(dataSend, 0);
                                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                        } else if (dataReceive[1].equals(dataSend[1])) {
                                            String msg = MainActivity.db.updatefromDatabase(dataSend, 1);
                                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                        } else if (dataReceive[2].equals(dataSend[2])) {
                                            String msg = MainActivity.db.updatefromDatabase(dataSend, 2);
                                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                        } else {
                                            MainActivity.db.deleteFromDatabase(dataReceive[1]);
                                            MainActivity.db.insertToDatabase(dataSend[0], dataSend[1], dataSend[2]);
                                            Toast.makeText(getActivity().getApplicationContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentShow,
                                                new FragmentAbout()).commit();
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid e-mail", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                AlertDialog dialog = ad.create();
                dialog.show();
            }
        });*/
        return fView;
    }

    private void references(View view) {
        svList = view.findViewById(R.id.svList);
    }

    private void referencesPrompt(View view) {
        /*etName = view.findViewById(R.id.adName);
        etPhone = view.findViewById(R.id.adPhone);
        etMail = view.findViewById(R.id.adMail);*/
    }
}

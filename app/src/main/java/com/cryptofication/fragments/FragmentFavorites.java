package com.cryptofication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cryptofication.R;

public class FragmentFavorites extends Fragment {

    private RecyclerView rwCrypto;
    private SwipeRefreshLayout srlReloadList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_favorites, container, false);
        references(fView);
        setHasOptionsMenu(true);

        return fView;
    }

    private void references(View view) {
        rwCrypto = view.findViewById(R.id.rwMarketCryptoList);
        srlReloadList = view.findViewById(R.id.srlMarketReload);
    }
}

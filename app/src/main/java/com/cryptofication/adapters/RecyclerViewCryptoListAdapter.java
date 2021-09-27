package com.cryptofication.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptofication.R;
import com.cryptofication.classes.ContextApplication;
import com.cryptofication.objects.Crypto;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCryptoListAdapter extends RecyclerView.Adapter<RecyclerViewCryptoListAdapter.ViewHolderCryptoList> implements Filterable, Serializable {

    private final ArrayList<Crypto> cryptoList;
    private final ArrayList<Crypto> cryptoListFull;
    private final Context context;
    private String userCurrency;

    private TextView tvDialogCryptoDetailName;
    private TextView tvDialogCryptoDetailSymbol;
    private TextView tvDialogCryptoDetailMarketCapRank;
    private TextView tvDialogCryptoDetailPriceChangePercentage24h;
    private TextView tvDialogCryptoDetailHigh24h;
    private TextView tvDialogCryptoDetailLow24h;
    private TextView tvDialogCryptoDetailCurrentPrice;
    private Button btnDialogCryptoDetailClose;

    public RecyclerViewCryptoListAdapter(Context context, ArrayList<Crypto> cryptoList) {
        this.context = context;
        this.cryptoList = cryptoList;
        cryptoListFull = new ArrayList<>(cryptoList);
    }

    @NonNull
    @Override
    public ViewHolderCryptoList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.adapter_crypto_list), parent, false);
        return new ViewHolderCryptoList(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCryptoList holder, int position) {
        Log.d("CryptoListAdapter", "onBindViewHolder: called");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextApplication.getAppContext());
        Log.d("algo", sharedPreferences.getString("prefCurrency", ""));
        switch (sharedPreferences.getString("prefCurrency", "")) {
            case "eur":
                userCurrency = context.getString(R.string.CURRENCY_EURO);
                break;
            case "usd":
            default:
                userCurrency = context.getString(R.string.CURRENCY_DOLLAR);
                break;
        }

        Crypto selectedPost = cryptoList.get(position);
        holder.assignData(selectedPost);

        holder.parentLayout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.CustomAlertDialog);
            View v = LayoutInflater.from(context).inflate(R.layout.crypto_detail_dialog, null);
            referencesDetailDialog(v);
            tvDialogCryptoDetailName.setText(selectedPost.getName());
            tvDialogCryptoDetailSymbol.setText(selectedPost.getSymbol());
            tvDialogCryptoDetailMarketCapRank.setText(String.valueOf(selectedPost.getMarketCapRank()));
            tvDialogCryptoDetailPriceChangePercentage24h.setText(String.format("%.2f",
                    selectedPost.getPriceChangePercentage24h()).replaceAll("0+$", "") + "%");
            if (selectedPost.getPriceChangePercentage24h() >= 0) {
                tvDialogCryptoDetailPriceChangePercentage24h.setTextColor(ContextCompat.getColor(context, R.color.green_high));
            } else {
                tvDialogCryptoDetailPriceChangePercentage24h.setTextColor(ContextCompat.getColor(context, R.color.red_low));
            }
            String high24h = String.format("%.10f", selectedPost.getHigh24h()).replaceAll("0+$", "");
            if (high24h.endsWith(".")) {
                high24h = high24h.substring(0, high24h.length() - 1);
            }
            tvDialogCryptoDetailHigh24h.setText(high24h + userCurrency);
            String low24h = String.format("%.10f", selectedPost.getLow24h()).replaceAll("0+$", "");
            if (low24h.endsWith(".")) {
                low24h = low24h.substring(0, low24h.length() - 1);
            }
            tvDialogCryptoDetailLow24h.setText(low24h + userCurrency);
            @SuppressLint("DefaultLocale") String currentPrice = String.format("%.10f",
                    selectedPost.getCurrentPrice()).replaceAll("0+$", "");
            if (currentPrice.endsWith(".")) {
                currentPrice = currentPrice.substring(0, currentPrice.length() - 1);
            }
            tvDialogCryptoDetailCurrentPrice.setText(currentPrice + userCurrency);
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();
            btnDialogCryptoDetailClose.setOnClickListener(view1 -> alertDialog.dismiss());
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Crypto> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(cryptoListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Crypto crypto : cryptoListFull) {
                    if (crypto.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(crypto);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        @SuppressLint("NotifyDataSetChanged")
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            cryptoList.clear();
            cryptoList.addAll((ArrayList<Crypto>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolderCryptoList extends RecyclerView.ViewHolder {
        ImageView ivCryptoIcon;
        TextView tvCryptoSymbol;
        TextView tvCryptoName;
        TextView tvCryptoPrice;
        TextView tvCryptoPriceChange;
        LinearLayout parentLayout;

        public ViewHolderCryptoList(@NonNull View itemView) {
            super(itemView);
            ivCryptoIcon = itemView.findViewById(R.id.ivAdapterCryptoIcon);
            tvCryptoSymbol = itemView.findViewById(R.id.tvAdapterCryptoSymbol);
            tvCryptoName = itemView.findViewById(R.id.tvAdapterCryptoName);
            tvCryptoPrice = itemView.findViewById(R.id.tvAdapterCryptoPrice);
            tvCryptoPriceChange = itemView.findViewById(R.id.tvAdapterCryptoPriceChange);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

        @SuppressLint("SetTextI18n")
        public void assignData(Crypto crypto) {
            Picasso.get().load(crypto.getImage()).into(ivCryptoIcon);

            tvCryptoSymbol.setText(crypto.getSymbol());
            tvCryptoName.setText(crypto.getName());
            @SuppressLint("DefaultLocale") String currentPrice = String.format("%.10f",
                    crypto.getCurrentPrice()).replaceAll("0+$", "");
            if (currentPrice.endsWith(".")) {
                currentPrice = currentPrice.substring(0, currentPrice.length() - 1);
            }
            crypto.setCurrentPrice(Double.parseDouble(currentPrice));
            tvCryptoPrice.setText(currentPrice + userCurrency);
            @SuppressLint("DefaultLocale") String priceChange = String.format("%.2f",
                    crypto.getPriceChangePercentage24h()).replaceAll("0+$", "");
            if (priceChange.endsWith(".")) {
                priceChange = priceChange.substring(0, priceChange.length() - 1);
            }
            crypto.setPriceChangePercentage24h(Double.parseDouble(priceChange));
            tvCryptoPriceChange.setText(priceChange + "%");
            if (crypto.getPriceChangePercentage24h() >= 0) {
                tvCryptoPriceChange.setTextColor(ContextCompat.getColor(context, R.color.green_high));
            } else {
                tvCryptoPriceChange.setTextColor(ContextCompat.getColor(context, R.color.red_low));
            }
        }
    }

    private void referencesDetailDialog(View v) {
        tvDialogCryptoDetailName = v.findViewById(R.id.tvDialogCryptoDetailName);
        tvDialogCryptoDetailSymbol = v.findViewById(R.id.tvDialogCryptoDetailSymbolText);
        tvDialogCryptoDetailMarketCapRank = v.findViewById(R.id.tvDialogCryptoDetailMarketCapRankText);
        tvDialogCryptoDetailPriceChangePercentage24h = v.findViewById(R.id.tvDialogCryptoDetailPriceChangePercentage24hText);
        tvDialogCryptoDetailHigh24h = v.findViewById(R.id.tvDialogCryptoDetailHigh24hText);
        tvDialogCryptoDetailLow24h = v.findViewById(R.id.tvDialogCryptoDetailLow24hText);
        tvDialogCryptoDetailCurrentPrice = v.findViewById(R.id.tvDialogCryptoDetailCurrentPriceText);
        btnDialogCryptoDetailClose = v.findViewById(R.id.btnDialogCryptoDetailClose);
    }
}

package com.cryptofication.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.RecyclerView;

import com.cryptofication.R;
import com.cryptofication.objects.Post;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCryptoListAdapter extends RecyclerView.Adapter<RecyclerViewCryptoListAdapter.ViewHolderCryptoList> implements Filterable, Serializable {

    private ArrayList<Post> cryptoList;
    private ArrayList<Post> cryptoListFull;
    private Context context;

    private TextView tvAdapterDialogCryptoName;
    private TextView tvAdapterDialogCryptoSymbolText;
    private TextView tvAdapterDialogCryptoMarketCapRankText;
    private TextView tvAdapterDialogCryptoPriceChangePercentage24hText;
    private TextView tvAdapterDialogCryptoHigh24hText;
    private TextView tvAdapterDialogCryptoLow24hText;
    private TextView tvAdapterDialogCryptoCurrentPriceText;
    private Button btnAdapterDialogCryptoClose;

    public RecyclerViewCryptoListAdapter(Context context, ArrayList<Post> cryptoList) {
        this.context = context;
        this.cryptoList = cryptoList;
        cryptoListFull = new ArrayList<>(cryptoList);
    }

    @NonNull
    @Override
    public ViewHolderCryptoList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.adapter_crypto_list), parent, false);
        ViewHolderCryptoList holder = new ViewHolderCryptoList(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCryptoList holder, int position) {
        Log.d("CryptoListAdapter", "onBindViewHolder: called");
        Post selectedPost = cryptoList.get(position);
        holder.assignData(selectedPost);

        holder.parentLayout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.CustomAlertDialog);
            View v = LayoutInflater.from(context).inflate(R.layout.crypto_detail_dialog, null);
            references(v);
            tvAdapterDialogCryptoName.setText(selectedPost.getName());
            tvAdapterDialogCryptoSymbolText.setText(selectedPost.getSymbol());
            tvAdapterDialogCryptoMarketCapRankText.setText(String.valueOf(selectedPost.getMarketCapRank()));
            tvAdapterDialogCryptoPriceChangePercentage24hText.setText(String.format("%.2f", selectedPost.getPriceChangePercentage24h()).replaceAll("0+$", "") + "%");
            if (selectedPost.getPriceChangePercentage24h() >= 0) {
                tvAdapterDialogCryptoPriceChangePercentage24hText.setTextColor(ContextCompat.getColor(context, R.color.green_high));
            } else {
                tvAdapterDialogCryptoPriceChangePercentage24hText.setTextColor(ContextCompat.getColor(context, R.color.red_low));
            }
            String high24h = String.format("%.5f", selectedPost.getHigh24h()).replaceAll("0+$", "");
            if (high24h.endsWith(".")) {
                high24h = high24h.substring(0, high24h.length() - 1);
            }
            tvAdapterDialogCryptoHigh24hText.setText(high24h + "€");
            String low24h = String.format("%.5f", selectedPost.getLow24h()).replaceAll("0+$", "");
            if (low24h.endsWith(".")) {
                low24h = low24h.substring(0, low24h.length() - 1);
            }
            tvAdapterDialogCryptoLow24hText.setText(low24h + "€");
            @SuppressLint("DefaultLocale") String currentPrice = String.format("%.5f", selectedPost.getCurrentPrice()).replaceAll("0+$", "");
            if (currentPrice.endsWith(".")) {
                currentPrice = currentPrice.substring(0, currentPrice.length() - 1);
            }
            tvAdapterDialogCryptoCurrentPriceText.setText(currentPrice + "€");
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();
            btnAdapterDialogCryptoClose.setOnClickListener(view1 -> alertDialog.dismiss());
            alertDialog.show();
        });


        holder.parentLayout.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.CustomAlertDialog);
            @SuppressLint("InflateParams") View v = LayoutInflater.from(context).inflate(R.layout.crypto_market_exchange_dialog, null);
            references(v);
            return false;
        });

    }

    private void references(View v) {
        tvAdapterDialogCryptoName = v.findViewById(R.id.tvDialogCryptoDetailName);
        tvAdapterDialogCryptoSymbolText = v.findViewById(R.id.tvDialogCryptoDetailSymbolText);
        tvAdapterDialogCryptoMarketCapRankText = v.findViewById(R.id.tvDialogCryptoDetailMarketCapRankText);
        tvAdapterDialogCryptoPriceChangePercentage24hText = v.findViewById(R.id.tvDialogCryptoDetailPriceChangePercentage24hText);
        tvAdapterDialogCryptoHigh24hText = v.findViewById(R.id.tvDialogCryptoDetailHigh24hText);
        tvAdapterDialogCryptoLow24hText = v.findViewById(R.id.tvDialogCryptoDetailLow24hText);
        tvAdapterDialogCryptoCurrentPriceText = v.findViewById(R.id.tvDialogCryptoDetailCurrentPriceText);
        btnAdapterDialogCryptoClose = v.findViewById(R.id.btnDialogCryptoDetailClose);
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
            List<Post> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(cryptoListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Post crypto : cryptoListFull) {
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
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            cryptoList.clear();
            cryptoList.addAll((List) filterResults.values);
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

        public void assignData(Post post) {
            Picasso.get().load(post.getImage()).into(ivCryptoIcon);

            tvCryptoSymbol.setText(post.getSymbol());
            tvCryptoName.setText(post.getName());
            String currentPrice = String.format("%.5f", post.getCurrentPrice()).replaceAll("0+$", "");
            if (currentPrice.endsWith(".")) {
                currentPrice = currentPrice.substring(0, currentPrice.length() - 1);
            }
            tvCryptoPrice.setText(currentPrice + "€");
            tvCryptoPriceChange.setText(String.format("%.2f", post.getPriceChangePercentage24h()).replaceAll("0+$", "") + "%");
            if (post.getPriceChangePercentage24h() >= 0) {
                tvCryptoPriceChange.setTextColor(ContextCompat.getColor(context, R.color.green_high));
            } else {
                tvCryptoPriceChange.setTextColor(ContextCompat.getColor(context, R.color.red_low));
            }
        }
    }

}

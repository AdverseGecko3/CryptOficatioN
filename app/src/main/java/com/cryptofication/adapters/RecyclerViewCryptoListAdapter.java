package com.cryptofication.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptofication.R;
import com.cryptofication.activities.MainActivity;
import com.cryptofication.objects.Post;

import java.io.Serializable;
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
            String currentPrice = String.format("%.5f", selectedPost.getCurrentPrice()).replaceAll("0+$", "");
            if (currentPrice.endsWith(".")) {
                currentPrice = currentPrice.substring(0, currentPrice.length() - 1);
            }
            tvAdapterDialogCryptoCurrentPriceText.setText(currentPrice + "€");
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();
            btnAdapterDialogCryptoClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });

        /*
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PokemonAdapter", "onClick: clicked row: " + cryptoList.get(position));

                Toast.makeText(context, "onClick: clicked row: " + cryptoList.get(position), Toast.LENGTH_SHORT).show();

                Post selectedPokemon = cryptoList.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("pokemonSelected", (Serializable) selectedPokemon);


                Intent intent = new Intent(context, PokemonActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
        */
    }

    private void references(View v) {
        tvAdapterDialogCryptoName = v.findViewById(R.id.tvAdapterDialogCryptoName);
        tvAdapterDialogCryptoSymbolText = v.findViewById(R.id.tvAdapterDialogCryptoSymbolText);
        tvAdapterDialogCryptoMarketCapRankText = v.findViewById(R.id.tvAdapterDialogCryptoMarketCapRankText);
        tvAdapterDialogCryptoPriceChangePercentage24hText = v.findViewById(R.id.tvAdapterDialogCryptoPriceChangePercentage24hText);
        tvAdapterDialogCryptoHigh24hText = v.findViewById(R.id.tvAdapterDialogCryptoHigh24hText);
        tvAdapterDialogCryptoLow24hText = v.findViewById(R.id.tvAdapterDialogCryptoLow24hText);
        tvAdapterDialogCryptoCurrentPriceText = v.findViewById(R.id.tvAdapterDialogCryptoCurrentPriceText);
        btnAdapterDialogCryptoClose = v.findViewById(R.id.btnAdapterDialogCryptoClose);
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
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

        TextView tvCryptoSymbol;
        TextView tvCryptoName;
        TextView tvCryptoPrice;
        TextView tvCryptoPriceChange;
        LinearLayout parentLayout;

        public ViewHolderCryptoList(@NonNull View itemView) {
            super(itemView);
            tvCryptoSymbol = itemView.findViewById(R.id.tvAdapterCryptoSymbol);
            tvCryptoName = itemView.findViewById(R.id.tvAdapterCryptoName);
            tvCryptoPrice = itemView.findViewById(R.id.tvAdapterCryptoPrice);
            tvCryptoPriceChange = itemView.findViewById(R.id.tvAdapterCryptoPriceChange);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

        public void assignData(Post post) {
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

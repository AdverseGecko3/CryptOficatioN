package com.cryptofication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptofication.R;
import com.cryptofication.objects.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCryptoListAdapter extends RecyclerView.Adapter<RecyclerViewCryptoListAdapter.ViewHolderCryptoList> implements Filterable, Serializable {

    private ArrayList<Post> cryptoList;
    private ArrayList<Post> cryptoListFull;
    private Context context;

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
        holder.assignData(cryptoList.get(position));



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
            tvCryptoSymbol = itemView.findViewById(R.id.tvCryptoSymbol);
            tvCryptoName = itemView.findViewById(R.id.tvCryptoName);
            tvCryptoPrice = itemView.findViewById(R.id.tvCryptoPrice);
            tvCryptoPriceChange = itemView.findViewById(R.id.tvCryptoPriceChange);
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
            if(post.getPriceChangePercentage24h() >= 0) {
                tvCryptoPriceChange.setTextColor(ContextCompat.getColor(context, R.color.green_high));
            } else {
                tvCryptoPriceChange.setTextColor(ContextCompat.getColor(context, R.color.red_low));
            }
        }
    }
}

package com.cryptofication.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Crypto implements Parcelable {

    private String id;
    private String name;
    private String symbol;
    private String image;
    private double current_price;
    private int market_cap_rank;
    private float high_24h;
    private float low_24h;
    private float price_change_percentage_24h;

    public Crypto() {

    }

    public Crypto(String id, String name, String symbol, String image, double current_price,
                  int market_cap_rank, float high_24h, float low_24h, float price_change_percentage_24h) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.image = image;
        this.current_price = current_price;
        this.market_cap_rank = market_cap_rank;
        this.high_24h = high_24h;
        this.low_24h = low_24h;
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getCurrentPrice() {
        return current_price;
    }

    public void setCurrentPrice(double current_price) {
        this.current_price = current_price;
    }

    public int getMarketCapRank() {
        return market_cap_rank;
    }

    public void setMarketCapRank(int market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public float getHigh24h() {
        return high_24h;
    }

    public void setHigh24h(float high_24h) {
        this.high_24h = high_24h;
    }

    public float getLow24h() {
        return low_24h;
    }

    public void setLow24h(float low_24h) {
        this.low_24h = low_24h;
    }

    public float getPriceChangePercentage24h() {
        return price_change_percentage_24h;
    }

    public void setPriceChangePercentage24h(float price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----COIN----").append("\n");
        sb.append("Id: ").append(getId()).append("\n");
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Symbol: ").append(getSymbol()).append("\n");
        sb.append("Image: ").append(getImage()).append("\n");
        sb.append("Current price: ").append(getCurrentPrice()).append("\n");
        sb.append("Market Cap Rank: ").append(getMarketCapRank()).append("\n");
        sb.append("High 24h: ").append(getHigh24h()).append("\n");
        sb.append("Low 24h: ").append(getLow24h()).append("\n");
        sb.append("Price change percentage 24h: ").append(getPriceChangePercentage24h())
                .append("\n");
        return sb.toString();
    }

    public Crypto(Parcel in) {
        String[] data = new String[9];
        in.readStringArray(data);

        this.id = data[0];
        this.name = data[1];
        this.symbol = data[2];
        this.image = data[3];
        this.current_price = Double.parseDouble(data[4]);
        this.market_cap_rank = Integer.parseInt(data[5]);
        this.high_24h = Float.parseFloat(data[6]);
        this.low_24h = Float.parseFloat(data[7]);
        this.price_change_percentage_24h = Float.parseFloat(data[8]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id, this.name, this.symbol, this.image, String.valueOf(this.current_price),
                String.valueOf(this.market_cap_rank), String.valueOf(this.high_24h), String.valueOf(this.low_24h),
                String.valueOf(this.price_change_percentage_24h)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Crypto createFromParcel(Parcel in) {
            return new Crypto(in);
        }

        public Crypto[] newArray(int size) {
            return new Crypto[size];
        }
    };
}

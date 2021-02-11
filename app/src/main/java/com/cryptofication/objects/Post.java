package com.cryptofication.objects;

public class Post {

    private String id;
    private String name;
    private String symbol;
    private String image;
    private double current_price;
    private int market_cap_rank;
    private float high_24h;
    private float low_24h;
    private float price_change_percentage_24h;

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
}

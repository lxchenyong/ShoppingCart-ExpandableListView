package com.chenyong.jeff.myshoppingcart.model.bean;

/**
 * 商品信息
 * Created by chenyong on 2016/8/25.
 */
public class GoodsInfo {
    protected String Id;
    protected String name;
    protected boolean isChoosed;
    private String imageUrl;
//    private String desc;
    private double price;
    private int count;
    private int position;// 绝对位置，只在ListView构造的购物车中，在删除时有效
//    private String color;
//    private String size;
    private int goodsImg;
//    private double discountPrice;


    public GoodsInfo(String id, String name, double price, int count, int goodsImg) {
        Id = id;
        this.name = name;

        this.imageUrl = imageUrl;
        this.price = price;
        this.count = count;
        this.position = position;
        this.goodsImg = goodsImg;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(int goodsImg) {
        this.goodsImg = goodsImg;
    }
}

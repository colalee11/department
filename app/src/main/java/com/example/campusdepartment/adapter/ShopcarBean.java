package com.example.campusdepartment.adapter;

import android.graphics.Bitmap;

public class ShopcarBean {
    String content;
    Bitmap picture;
    String price;
    String number;

    public ShopcarBean() {
    }

    public ShopcarBean(String content, Bitmap picture, String price, String number) {
        this.content = content;
        this.picture = picture;
        this.price = price;
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ShopcarBean{" +
                "content='" + content + '\'' +
                ", picture=" + picture +
                ", price='" + price + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}

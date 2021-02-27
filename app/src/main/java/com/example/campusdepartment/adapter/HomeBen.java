package com.example.campusdepartment.adapter;

import android.graphics.Bitmap;

/**
 * Created by 林嘉煌 on 2021/1/5.
 */

public class HomeBen {
    private Bitmap picture;
    private String content;
    private String price;
    private String number;

    public HomeBen() {

    }

    public HomeBen(Bitmap picture, String content, String price, String number) {

        this.picture = picture;
        this.content = content;
        this.price = price;
        this.number = number;
    }

    @Override
    public String toString() {
        return "HomeBen{" +
                "picture=" + picture +
                ", content='" + content + '\'' +
                ", price='" + price + '\'' +
                ", number=" + number +
                '}';
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}

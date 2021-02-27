package com.example.campusdepartment.adapter;

import android.graphics.Bitmap;
import android.widget.LinearLayout;

public class ShopcarBean {
    String content;
    Bitmap picture;
    String price;
    String number;
    public String title;
    public LinearLayout rootView;

    public ShopcarBean() {
    }

    public ShopcarBean(String content, Bitmap picture, String price, String number, String title, LinearLayout rootView) {
        this.content = content;
        this.picture = picture;
        this.price = price;
        this.number = number;
        this.title = title;
        this.rootView = rootView;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LinearLayout getRootView() {
        return rootView;
    }

    public void setRootView(LinearLayout rootView) {
        this.rootView = rootView;
    }

    @Override
    public String toString() {
        return "ShopcarBean{" +
                "content='" + content + '\'' +
                ", picture=" + picture +
                ", price='" + price + '\'' +
                ", number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", rootView=" + rootView +
                '}';
    }
}

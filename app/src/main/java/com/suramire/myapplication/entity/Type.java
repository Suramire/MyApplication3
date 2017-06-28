package com.suramire.myapplication.entity;

/**
 * Created by Suramire on 2017/6/28.
 */

public class Type {
    int image;
    String name;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type(int image, String name) {
        this.image = image;
        this.name = name;
    }
}

package com.gridviewimagepicker.model;

public class Image {
    private String imageUrl;
    private String key;

    public Image(){}

    public Image(String imageUrl, String key) {
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

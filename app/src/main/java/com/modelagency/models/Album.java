package com.modelagency.models;

import java.util.List;

public class Album {

    private String header;
    private List<Object> imageList;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Object> getImageList() {
        return imageList;
    }

    public void setImageList(List<Object> imageList) {
        this.imageList = imageList;
    }
}

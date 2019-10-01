package com.modelagency.models;

import java.io.Serializable;
import java.util.List;

public class CourseSection implements Serializable {

    private String title;
    private int id,type;

    private List<SectionVideo> sectionVideoList;

    public List<SectionVideo> getSectionVideoList() {
        return sectionVideoList;
    }

    public void setSectionVideoList(List<SectionVideo> sectionVideoList) {
        this.sectionVideoList = sectionVideoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

package com.modelagency.models;

public class InfoItem {
    private String label,showLabel,value;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShowLabel() {
        return showLabel;
    }

    public void setShowLabel(String showLabel) {
        this.showLabel = showLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

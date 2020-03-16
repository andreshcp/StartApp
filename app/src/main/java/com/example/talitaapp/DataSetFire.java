package com.example.talitaapp;

public class DataSetFire {
    String titleName;
    String descriptionName;

    public DataSetFire(String titleName, String descriptionName) {
        this.titleName = titleName;
        this.descriptionName = descriptionName;
    }

    public DataSetFire() {
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDescriptionName() {
        return descriptionName;
    }

    public void setDescriptionName(String descriptionName) {
        this.descriptionName = descriptionName;
    }
}

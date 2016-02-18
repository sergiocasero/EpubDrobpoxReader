package com.sergiocasero.epubdrobpoxreader.model;

import io.realm.RealmObject;

/**
 * Created by sergiocasero on 18/2/16.
 */
public class DescriptionModel extends RealmObject {
    private String name;

    public DescriptionModel() {
    }

    public DescriptionModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

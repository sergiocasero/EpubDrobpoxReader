package com.sergiocasero.epubdrobpoxreader.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sergiocasero on 17/2/16.
 */
public class BookModel extends RealmObject {

    public static final String PRIMARY_KEY_FIELD = "name";

    @PrimaryKey
    private String name;
    private String cover;
    private RealmList<AuthorModel> authors;
    private RealmList<AuthorModel> contributors;
    private String languaje;
    private RealmList<DescriptionModel> descriptions;

    public BookModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<AuthorModel> getAuthors() {
        return authors;
    }

    public void setAuthors(RealmList<AuthorModel> authors) {
        this.authors = authors;
    }

    public RealmList<AuthorModel> getContributors() {
        return contributors;
    }

    public void setContributors(RealmList<AuthorModel> contributors) {
        this.contributors = contributors;
    }

    public String getLanguaje() {
        return languaje;
    }

    public void setLanguaje(String languaje) {
        this.languaje = languaje;
    }

    public RealmList<DescriptionModel> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(RealmList<DescriptionModel> descriptions) {
        this.descriptions = descriptions;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


}

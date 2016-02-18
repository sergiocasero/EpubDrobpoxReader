package com.sergiocasero.epubdrobpoxreader.model;

import io.realm.RealmObject;

/**
 * Created by sergiocasero on 18/2/16.
 */
public class AuthorModel extends RealmObject {
    private String firstname;

    private String lastname;

    public AuthorModel() {
    }

    public AuthorModel(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}

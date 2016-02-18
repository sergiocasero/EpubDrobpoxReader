package com.sergiocasero.epubdrobpoxreader.mapper;

import com.sergiocasero.epubdrobpoxreader.model.AuthorModel;

import java.io.IOException;

import nl.siegmann.epublib.domain.Author;

/**
 * Created by sergiocasero on 18/2/16.
 */
public class AuthorModelMapper implements Mapper<AuthorModel, Author> {
    @Override
    public AuthorModel dataToModel(Author data) throws IOException {
        AuthorModel authorModel = null;
        if (data != null) {
            authorModel = new AuthorModel();
            authorModel.setFirstname(data.getFirstname());
            authorModel.setLastname(data.getLastname());
        }
        return authorModel;
    }
}

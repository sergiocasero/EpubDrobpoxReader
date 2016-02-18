package com.sergiocasero.epubdrobpoxreader.mapper;

import java.io.IOException;

/**
 * Created by sergiocasero on 18/2/16.
 */
public interface Mapper<M, D> {
    M dataToModel(D data) throws IOException;
}

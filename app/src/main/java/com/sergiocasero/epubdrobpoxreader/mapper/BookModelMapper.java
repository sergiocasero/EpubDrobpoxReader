package com.sergiocasero.epubdrobpoxreader.mapper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.sergiocasero.epubdrobpoxreader.model.AuthorModel;
import com.sergiocasero.epubdrobpoxreader.model.BookModel;
import com.sergiocasero.epubdrobpoxreader.model.DescriptionModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import io.realm.RealmList;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;

/**
 * Created by sergiocasero on 18/2/16.
 */
public class BookModelMapper implements Mapper<BookModel, Book> {

    private AuthorModelMapper authorModelMapper;

    public BookModelMapper() {
        authorModelMapper = new AuthorModelMapper();
    }

    @Override
    public BookModel dataToModel(Book data) throws IOException {
        BookModel bookModel = null;
        if (data != null) {
            bookModel = new BookModel();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.decodeStream(data.getCoverImage().getInputStream()).compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String cover = Base64.encodeToString(b, Base64.DEFAULT);

            bookModel.setCover(cover);
            bookModel.setLanguaje(data.getMetadata().getLanguage());
            bookModel.setTitle(data.getTitle());

            RealmList<DescriptionModel> descriptionModels = new RealmList<>();

            List<String> descriptions = data.getMetadata().getDescriptions();
            if (descriptions != null) {
                for (String description : descriptions) {
                    descriptionModels.add(new DescriptionModel(description));
                }
            }
            bookModel.setDescriptions(descriptionModels);

            RealmList<AuthorModel> authorModels = new RealmList<>();
            List<Author> authors = data.getMetadata().getAuthors();
            if (authors != null) {
                for (Author author : authors) {
                    authorModels.add(authorModelMapper.dataToModel(author));
                }
            }
            bookModel.setAuthors(authorModels);

            RealmList<AuthorModel> contributorsModel = new RealmList<>();
            List<Author> contributors = data.getMetadata().getContributors();
            if (contributors != null) {
                for (Author contributor : contributors) {
                    contributorsModel.add(authorModelMapper.dataToModel(contributor));
                }
            }
            bookModel.setContributors(contributorsModel);


        }
        return bookModel;
    }
}

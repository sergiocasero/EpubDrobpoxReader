package com.sergiocasero.epubdrobpoxreader.mapper;

import com.sergiocasero.epubdrobpoxreader.model.AuthorModel;
import com.sergiocasero.epubdrobpoxreader.model.BookModel;
import com.sergiocasero.epubdrobpoxreader.model.DescriptionModel;
import com.sergiocasero.epubdrobpoxreader.util.Util;

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


            String cover = Util.resourceToString(data.getCoverImage().getInputStream());

            bookModel.setCover(cover);
            bookModel.setLanguaje(data.getMetadata().getLanguage());
            bookModel.setTitle(data.getTitle());

            if (data.getMetadata().getDates().size() > 0) {
                bookModel.setDate(data.getMetadata().getDates().get(0).toString());
            } else {
                bookModel.setDate("2010");
            }

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

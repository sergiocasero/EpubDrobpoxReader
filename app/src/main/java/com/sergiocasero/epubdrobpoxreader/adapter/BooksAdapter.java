package com.sergiocasero.epubdrobpoxreader.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.model.BookModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sergiocasero on 17/2/16.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    List<BookModel> items;

    public BooksAdapter(List<BookModel> items) {
        this.items = items;
    }

    public void add(BookModel bookModel) {
        items.add(bookModel);
        notifyItemInserted(items.indexOf(bookModel));
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookModel bookModel = items.get(position);
        holder.bind(bookModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cover)
        ImageView cover;

        @Bind(R.id.title)
        TextView title;

        public BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(BookModel model) {
            byte[] encodeByte = Base64.decode(model.getCover(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            cover.setImageBitmap(bitmap);
            title.setText(model.getName());
        }
    }
}

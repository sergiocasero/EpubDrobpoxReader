package com.sergiocasero.epubdrobpoxreader.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private OnItemClickListener onItemClickListener;
    private int lastPosition = 0;

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
        setAnimation(holder.getContainerView(), position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

        View itemView;

        public BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = BookViewHolder.super.getAdapterPosition();
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }

        public void bind(BookModel model) {
            byte[] encodeByte = Base64.decode(model.getCover(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            cover.setImageBitmap(bitmap);
            title.setText(model.getTitle());
        }

        public View getContainerView() {
            return itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}

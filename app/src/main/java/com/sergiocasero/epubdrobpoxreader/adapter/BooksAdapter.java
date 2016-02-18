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
import com.sergiocasero.epubdrobpoxreader.util.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sergiocasero on 17/2/16.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    public static final int LINEAR_TYPE = 0;
    public static final int GRID_TYPE = 1;
    public static final int ITEM_GRID_WIDTH_DP = 140;


    List<BookModel> items;

    private OnItemClickListener onItemClickListener;
    private int lastPosition = 0;
    private int itemViewType;

    public BooksAdapter(List<BookModel> items) {
        this.items = items;
    }

    public void add(BookModel bookModel) {
        items.add(bookModel);
        notifyItemInserted(items.indexOf(bookModel));
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResourceId = viewType == 0 ? R.layout.list_item : R.layout.grid_item;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutResourceId, parent, false);

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

    @Override
    public int getItemViewType(int position) {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public void orderByName() {
        Collections.sort(items, new Comparator<BookModel>() {
            @Override
            public int compare(final BookModel object1, final BookModel object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });
        notifyDataSetChanged();
    }

    public void orderByDate() {
        Collections.sort(items, new Comparator<BookModel>() {
            @Override
            public int compare(final BookModel object1, final BookModel object2) {
                return object1.getDate().compareTo(object2.getDate());
            }
        });
        notifyDataSetChanged();
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
            Bitmap bitmap = Util.stringToBitMap(model.getCover());
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

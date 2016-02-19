package com.sergiocasero.epubdrobpoxreader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.model.AuthorModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sergiocasero on 17/2/16.
 */
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {


    List<AuthorModel> items;
    private int lastPosition = 0;

    public AuthorAdapter(List<AuthorModel> items) {
        this.items = items;
    }


    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResourceId = R.layout.author_item;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutResourceId, parent, false);

        return new AuthorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder, int position) {
        AuthorModel authorModel = items.get(position);
        holder.bind(authorModel);
        setAnimation(holder.getContainerView(), position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.surname)
        TextView surName;

        View itemView;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }

        public void bind(AuthorModel authorModel) {
            name.setText(authorModel.getFirstname());
            surName.setText(authorModel.getLastname());
        }

        public View getContainerView() {
            return itemView;
        }
    }
}

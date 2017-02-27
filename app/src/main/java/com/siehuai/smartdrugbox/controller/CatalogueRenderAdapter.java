package com.siehuai.smartdrugbox.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siehuai.smartdrugbox.R;


public class CatalogueRenderAdapter extends RecyclerView.Adapter<CatalogueRenderAdapter.ViewHolder> {

    public CatalogueRenderAdapter() {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            mTextView = (TextView) itemView.findViewById(R.id.item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        public void setImageSource(int resId) {
            mImage.setImageResource(resId);
        }

        public void setTextView(String text) {
            mTextView.setText(text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_grid_view, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setImageSource(R.drawable.medicine_box_icon);
        holder.setTextView("Testing");
    }

    @Override
    public int getItemCount() {
        return 8;
    }

}

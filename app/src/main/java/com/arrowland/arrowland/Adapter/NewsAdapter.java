package com.arrowland.arrowland.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arrowland.arrowland.NotificationHomeDetail;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 8/11/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    ArrayList<News> dataList;
    private Context context;

    public NewsAdapter(ArrayList<News> dataList,Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, final int position) {


        holder.txTitle.setText(dataList.get(position).getTitle());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/monaco.ttf");
        holder.txTitle.setTypeface(typeface);

        Picasso.with(context)
                .load(dataList.get(position).getImage_url())
//                .placeholder(R.drawable.logo)
                .fit()
                .into(holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NotificationHomeDetail.class);
                intent.putExtra("ID",dataList.get(position).getId());
                intent.putExtra("fbIntent","News");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txTitle;

        CardView cardView;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);

            txTitle = itemView.findViewById(R.id.tvTitle);
            cardView = itemView.findViewById(R.id.card_view_row);
            img = itemView.findViewById(R.id.img_row);
        }
    }
}

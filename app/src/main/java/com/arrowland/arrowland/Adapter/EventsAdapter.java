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
import com.arrowland.arrowland.REST.Events;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 8/12/2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    ArrayList<Events> dataList;
    private Context context;

    public EventsAdapter(ArrayList<Events> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, final int position) {

        holder.txtTitle.setText(dataList.get(position).getTitle());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/monaco.ttf");
        holder.txtTitle.setTypeface(typeface);

        Picasso.with(context)
                .load(dataList.get(position).getImage_url())
                .placeholder(R.drawable.logo)
                .fit()
                .into(holder.img);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NotificationHomeDetail.class);
                intent.putExtra("ID",dataList.get(position).getId());
                intent.putExtra("fbIntent","Events");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        CardView cardView;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.tvTitle);
            cardView = itemView.findViewById(R.id.card_view_row);
            img = itemView.findViewById(R.id.img_row);
        }
    }
}

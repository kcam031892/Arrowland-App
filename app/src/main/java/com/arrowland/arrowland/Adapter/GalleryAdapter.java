package com.arrowland.arrowland.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.ClickImageGallery;
import com.arrowland.arrowland.Login;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.Gallery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 7/23/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    ArrayList<Gallery> dataList;
    private Context context;

    public GalleryAdapter(ArrayList<Gallery> dataList,Context context) {
        this.context = context;
        this.dataList = dataList;
    }
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_gallery,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, final int position) {

        // PICASSO SET IMAGE URL TO IMAGEVIEW


        Picasso.with(context)
                .load(dataList.get(position).getImg_path())
                .placeholder(R.drawable.logo)
                .fit()
                .into(holder.img);


        // SET CLICK IN A CARD VIEW
        // GO TO CLICKIMAGE

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ClickImageGallery.class);
                intent.putExtra("url",dataList.get(position).getImg_path());
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // INITIALIZE VIEW HOLDER

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.card_recycler_view);

        }

//        @Override
//        public void onClick(View view) {
//            clickListener.onItemClick(getAdapterPosition(),view);
//
//        }
//
//        @Override
//        public boolean onLongClick(View view) {
//            clickListener.onItemLongClick(getAdapterPosition(),view);
//            return false;
//        }
//
//    }
//
//
//    public void setOnItemClickListener(ClickListener clickListener) {
//        GalleryAdapter.clickListener = clickListener;
//    }
//
//    public interface ClickListener {
//        void onItemClick(int position, View v);
//        void onItemLongClick(int position, View v);
//
//    }
    }

}

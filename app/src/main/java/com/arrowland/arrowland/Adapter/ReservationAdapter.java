package com.arrowland.arrowland.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.Reservation;
import com.arrowland.arrowland.ReservationDetail;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 8/7/2018.
 */

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    ArrayList<Reservation> dataList;
    private Context context;


    public ReservationAdapter(ArrayList<Reservation> dataList,Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if(getItemCount() > 0) {


            view = layoutInflater.inflate(R.layout.card_reservation_list,parent,false);
        }else {
            view  = layoutInflater.inflate(R.layout.card_empty_reservation,parent,false);

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationAdapter.ViewHolder holder, final int position) {


        holder.txtDate.setText(dataList.get(position).getDate());
        holder.txtYear.setText(dataList.get(position).getYear());
        holder.txtTime.setText(dataList.get(position).getTime());



            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReservationDetail.class);
                    intent.putExtra("ID", dataList.get(position).getId());
                    intent.putExtra("Status",dataList.get(position).getStatus());
                    context.startActivity(intent);


                }
            });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate,txtYear,txtTime;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtYear = itemView.findViewById(R.id.txtYear);
            txtTime = itemView.findViewById(R.id.txtTime);
            cardView = itemView.findViewById(R.id.card_reserve_list_view);
        }
    }
}



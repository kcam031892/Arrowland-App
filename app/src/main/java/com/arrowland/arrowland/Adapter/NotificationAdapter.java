package com.arrowland.arrowland.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arrowland.arrowland.PaymentDetail;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.Notifications;
import com.arrowland.arrowland.ReservationDetail;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 9/21/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    ArrayList<Notifications> dataList;
    private Context context;

    public NotificationAdapter(ArrayList<Notifications> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(getItemCount() > 0) {
            view = layoutInflater.inflate(R.layout.card_notifications,parent,false);
        }else {
            view = layoutInflater.inflate(R.layout.card_empty_reservation,parent,false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtContent.setText(dataList.get(position).getMessage());
        holder.txtTime.setText(dataList.get(position).getTime());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataList.get(position).getNotificationable_type().equals("Reservation")) {
                    Intent intent = new Intent(context, ReservationDetail.class);
                    intent.putExtra("ID", dataList.get(position).getId());
                    intent.putExtra("Status",dataList.get(position).getStatus());
                    context.startActivity(intent);
                }else if(dataList.get(position).getNotificationable_type().equals("Payment")) {
                    Intent intent = new Intent(context, PaymentDetail.class);
                    intent.putExtra("ID", dataList.get(position).getId());
                    context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent,txtTime;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtTime = itemView.findViewById(R.id.txtTime);
            cardView = itemView.findViewById(R.id.card_recycler_view);
        }
    }
}

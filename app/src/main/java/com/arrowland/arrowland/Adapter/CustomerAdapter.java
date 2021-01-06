package com.arrowland.arrowland.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.User;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 7/23/2018.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    ArrayList<User> dataList;


    public CustomerAdapter(ArrayList<User> dataList){
        this.dataList = dataList;
    }


    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CustomerAdapter.ViewHolder holder, int i) {
//        holder.txtFirstName.setText(dataList.get(i).getUsername());
//        holder.txtLastName.setText(dataList.get(i).getUsername());
//        holder.txtFirstName.setText(dataList.get(i).getFirst_name());
//        holder.txtUsername.setText(dataList.get(i).getUsername());
//        holder.txtLastName.setText(dataList.get(i).getLast_name());

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         TextView txtFirstName,txtLastName,txtUsername;

        public ViewHolder(View view) {
            super(view);




        }
    }
}

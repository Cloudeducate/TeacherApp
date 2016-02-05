package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloudeducate.redtick.Activity.Assignment;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 04-02-2016.
 */
public class ViewConversationAdapter extends RecyclerView.Adapter<ViewConversationAdapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter;
    Bundle bundle;
    List<String[]> list = new ArrayList<String[]>();

    public ViewConversationAdapter(Context context, List<String[]> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardspecifc_conversation, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.convname.setText(list.get(position)[2]);
        holder.message.setText(list.get(position)[0]);
        holder.time.setText(list.get(position)[1]);
        Log.v("MyApp", list.get(position)[0]);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView convname, time,message;
        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            message=(TextView) itemView.findViewById(R.id.message);
            convname = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

}
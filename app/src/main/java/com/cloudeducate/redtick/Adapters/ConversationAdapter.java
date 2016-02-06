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
import com.cloudeducate.redtick.Activity.Performance;
import com.cloudeducate.redtick.Activity.View_Conversation;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 04-02-2016.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter;
    Bundle bundle;
    List<String[]> list = new ArrayList<String[]>();

    public ConversationAdapter(Context context, List<String[]> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_messages, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String conversation_id;
        holder.convname.setText(list.get(position)[0]);
        Log.v("MyApp",list.get(position)[0]);
        conversation_id=list.get(position)[1];
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle=new Bundle();
                bundle.putString("conversation_id",conversation_id);
                Intent intent=new Intent(context, View_Conversation.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        Log.v("MyApp", list.get(position)[0]);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView convname,view;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            convname = (TextView) itemView.findViewById(R.id.conversation);
            view = (TextView) itemView.findViewById(R.id.view);
        }
    }
}
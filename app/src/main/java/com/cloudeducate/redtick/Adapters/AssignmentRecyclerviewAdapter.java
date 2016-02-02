package com.cloudeducate.redtick.Adapters;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cloudeducate.redtick.Activity.Assignment;
import com.cloudeducate.redtick.Activity.Grade_Assignment;
import com.cloudeducate.redtick.Model.Assignment_model;
import com.cloudeducate.redtick.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogesh on 22/1/16.
 */
public class AssignmentRecyclerviewAdapter extends RecyclerView.Adapter<AssignmentRecyclerviewAdapter.ViewHolder> {

    View assignmentView;
    CardView cards;
    Context context;
    List<Assignment_model> list = new ArrayList<Assignment_model>();

    ProgressDialog progressDialog;
    int id = 1;

    public AssignmentRecyclerviewAdapter(Context context, List<Assignment_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_assignment, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.deadline.setText("Deadline : " + list.get(position).getDeadline());
        holder.course.setText("Subject: " + list.get(position).getCourse());
        holder.status.setText("Class : " + list.get(position).getClassroom());
        final Bundle bundle=new Bundle();
        bundle.putString("assignment_id",list.get(position).getId());

        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Grade_Assignment.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, course, status, deadline;

        public ViewHolder(View itemView) {
            super(itemView);
            assignmentView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            deadline = (TextView) itemView.findViewById(R.id.deadline);
            course = (TextView) itemView.findViewById(R.id.course);
            status = (TextView) itemView.findViewById(R.id.status);
            cards=(CardView) itemView.findViewById(R.id.cardview_assignment);
        }
    }

}

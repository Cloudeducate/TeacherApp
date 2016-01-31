package com.cloudeducate.redtick.Adapters;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cloudeducate.redtick.Model.Assignment_model;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogesh on 22/1/16.
 */
public class AssignmentRecyclerviewAdapter extends RecyclerView.Adapter<AssignmentRecyclerviewAdapter.ViewHolder> {

    View assignmentView;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.desc.setText(list.get(position).getDescription());
        holder.deadline.setText("Deadline : " + list.get(position).getDeadline());
        holder.course.setText(list.get(position).getCourse());
        //holder.filename.setText(list.get(position).getFilename());
//


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, filename, course, status, deadline;

        public ViewHolder(View itemView) {
            super(itemView);
            assignmentView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.description);
            deadline = (TextView) itemView.findViewById(R.id.deadline);
            filename = (TextView) itemView.findViewById(R.id.filename);
            course = (TextView) itemView.findViewById(R.id.course);
            status = (TextView) itemView.findViewById(R.id.status);

        }
    }

}

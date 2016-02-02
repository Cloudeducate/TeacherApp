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
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 01-02-2016.
 */
public class Courses_Adapter extends RecyclerView.Adapter<Courses_Adapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter;
    Bundle bundle;
    List<String[]> list = new ArrayList<String[]>();

    public Courses_Adapter(Context context, List<String[]> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_card_course, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String course_id,classroom_id;
        holder.subjectname.setText(list.get(position)[0]);
        holder.classroom.setText("Class " + list.get(position)[1]);
        classroom_id=list.get(position)[2];
        course_id=list.get(position)[3];
        bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("class_id",classroom_id);
        holder.assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(context, Assignment.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.Performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Performance.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        public TextView subjectname, classroom;
        public TextView assignment, Performance;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            subjectname = (TextView) itemView.findViewById(R.id.subject_name);
            classroom = (TextView) itemView.findViewById(R.id.classroom);
            assignment = (TextView) itemView.findViewById(R.id.assignment);
            Performance = (TextView) itemView.findViewById(R.id.performance);
        }
    }
}
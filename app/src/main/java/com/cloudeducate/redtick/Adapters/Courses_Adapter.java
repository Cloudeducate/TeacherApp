package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudeducate.redtick.Model.Attendance_model;
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
    List<String[]> list = new ArrayList<String[]>();

    public Courses_Adapter(Context context, List<String[]> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_courses, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String course_id,classroom_id;
        holder.subjectname.setText(list.get(position)[0]);
        holder.classroom.setText("Rollno." + list.get(position)[1]);
        classroom_id=list.get(position)[3];
        course_id=list.get(position)[2];
        holder.assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.Performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public Button assignment, Performance;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            subjectname = (TextView) itemView.findViewById(R.id.subject_name);
            classroom = (TextView) itemView.findViewById(R.id.classroom);
            assignment = (Button) itemView.findViewById(R.id.assignment);
            Performance = (Button) itemView.findViewById(R.id.performance);
        }
    }
}
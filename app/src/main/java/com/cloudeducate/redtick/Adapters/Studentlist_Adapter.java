package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 01-02-2016.
 */
public class Studentlist_Adapter extends RecyclerView.Adapter<Studentlist_Adapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter,adapter1;
    List<Attendance_model> list = new ArrayList<Attendance_model>();

    public Studentlist_Adapter(Context context, List<Attendance_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_studentlist, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.studentname.setText(list.get(position).getstudentname());
        holder.rollnoview.setText("Rollno." + list.get(position).getrollno());
        Log.v("MyApp", Integer.toString(list.get(position).getAttendancevalue()));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studentname,rollnoview,user_idview;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            studentname = (TextView) itemView.findViewById(R.id.student_name);
            rollnoview=(TextView) itemView.findViewById(R.id.rollno);
        }
    }
}
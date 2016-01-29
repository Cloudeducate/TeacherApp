package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudeducate.redtick.Activity.Manage_Attendance;
import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by abhishek on 29-01-2016.
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter,adapter1;
    List<Attendance_model> list = new ArrayList<Attendance_model>();

    public AttendanceAdapter(Context context, List<Attendance_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_attendance, null);
        Set<String> val = new HashSet<String>();
        val.add("Present");
        val.add("Absent");
       adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, val.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.studentname.setText(list.get(position).getstudentname());
        holder.rollnoview.setText("Rollno." + list.get(position).getrollno());
        if(list.get(position).getAttendancevalue()==1)
        {
            Set<String> val = new HashSet<String>();
            val.add("Absent");
            val.add("Present");
            adapter1 = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, val.toArray());
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter1);
        }
           else {
            holder.spinner.setAdapter(adapter);
        }
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Attendance_model attendance= list.get(position);
                if(pos==0)
                    attendance.setAttendancevalue(1);
                else
                attendance.setAttendancevalue(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studentname,rollnoview,user_idview;
        public Spinner spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            studentname = (TextView) itemView.findViewById(R.id.student_name);
            rollnoview=(TextView) itemView.findViewById(R.id.rollno);
            spinner = (Spinner) itemView.findViewById(R.id.spinner_attendance);

        }
    }
}

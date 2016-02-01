package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudeducate.redtick.Activity.Performance;
import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by abhishek on 29-01-2016.
 */
public class Performance_Adapter extends RecyclerView.Adapter<Performance_Adapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter,adapter1;
    List<Attendance_model> list = new ArrayList<Attendance_model>();

    public Performance_Adapter(Context context, List<Attendance_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_attendance, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.studentname.setText(list.get(position).getstudentname());
        holder.rollnoview.setText("Rollno." + list.get(position).getrollno());
        Log.v("MyApp", Integer.toString(list.get(position).getAttendancevalue()));
        String value[]=new String[]{"1","2","3","4","5","6","7","8","9","10"};
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(list.get(position).getGradevalue()-1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Attendance_model attendance= list.get(position);
                attendance.setGradevalue(pos+1);
               holder.spinner.setSelection(pos);
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

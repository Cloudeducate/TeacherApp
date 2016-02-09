package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudeducate.redtick.Activity.Grade_Assignment;
import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 02-02-2016.
 */
public class GradeAssignmentAdapter extends RecyclerView.Adapter<GradeAssignmentAdapter.ViewHolder> {

    View courseView;
    Context context;
    ArrayAdapter adapter,adapter1;
    List<Attendance_model> list = new ArrayList<Attendance_model>();

    public GradeAssignmentAdapter(Context context, List<Attendance_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_assign_item_layout, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.studentname.setText(list.get(position).getstudentname());
        holder.rollnoview.setText("Rollno." + list.get(position).getrollno());
        holder.remark.setText(list.get(position).getstudentremark());
        Log.v("MyApp", Integer.toString(list.get(position).getAttendancevalue()));
        String value[]=new String[]{"0","1","2","3","4","5"};
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(list.get(position).getGradevalue()-1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Attendance_model attendance= list.get(position);
                attendance.setGradevalue(pos);
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
        public TextView studentname,rollnoview;
        public Spinner spinner;
        public EditText remark;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            studentname = (TextView) itemView.findViewById(R.id.student_name);
            rollnoview=(TextView) itemView.findViewById(R.id.rollno);
            spinner = (Spinner) itemView.findViewById(R.id.grade_spinner);
            remark=(EditText)itemView.findViewById(R.id.remark);

        }
    }
}

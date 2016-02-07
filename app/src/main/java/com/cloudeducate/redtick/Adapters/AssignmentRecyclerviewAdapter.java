package com.cloudeducate.redtick.Adapters;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudeducate.redtick.Activity.Assignment;
import com.cloudeducate.redtick.Activity.Grade_Assignment;
import com.cloudeducate.redtick.Activity.Send_Notification;
import com.cloudeducate.redtick.Model.Assignment_model;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.URL1;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yogesh on 22/1/16.
 */
public class AssignmentRecyclerviewAdapter extends RecyclerView.Adapter<AssignmentRecyclerviewAdapter.ViewHolder> {

    View assignmentView;
    CardView cards;
    Context context;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private SharedPreferences sharedpref;
    private String assignment_id="1";
    private String metadata;
    private AssignmentRecyclerviewAdapter assignmentRecyclerviewAdapter;
    public static final String TAG = "MyApp";
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
        if(list.get(position).getNotify()=="true")
            holder.notify.setText("Notified");
        else
        holder.notify.setText("Send Notification");
        final Bundle bundle=new Bundle();
        bundle.putString("assignment_id",list.get(position).getId());

        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Grade_Assignment.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bool=list.get(position).getNotify();
                assignment_id=list.get(position).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Send Notification For Assignment");
                if(bool=="true") {
                    builder.setMessage("Want to Notify Again");
                }
                else
                {
                    builder.setMessage("Are you Sure to Send Notification");
                }
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fetchData();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, course, status, deadline,notify;

        public ViewHolder(View itemView) {
            super(itemView);
            assignmentView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            deadline = (TextView) itemView.findViewById(R.id.deadline);
            course = (TextView) itemView.findViewById(R.id.course);
            status = (TextView) itemView.findViewById(R.id.status);
            cards=(CardView) itemView.findViewById(R.id.cardview_assignment);
            notify=(TextView)itemView.findViewById(R.id.notify);
        }
    }
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending Notification..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public void fetchData() {
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        if (assignment_id == null) {
            assignment_id = "1";
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL1.getAssignNotifyURL(assignment_id), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString() == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response.toString());
                Toast.makeText(context,"Notification Send",Toast.LENGTH_LONG);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.v(TAG, "Response = " + "timeOut");
                } else if (error instanceof AuthFailureError) {
                    Log.v(TAG, "Response = " + "AuthFail");
                } else if (error instanceof ServerError) {
                    Log.v(TAG, "Response = " + "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.v(TAG, "Response = " + "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.v(TAG, "Response = " + "ParseError");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-App", "teacher");
                params.put("X-Access-Token", metadata);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}

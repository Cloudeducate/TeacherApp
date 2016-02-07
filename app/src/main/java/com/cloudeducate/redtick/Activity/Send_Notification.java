package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.cloudeducate.redtick.Adapters.Performance_Adapter;
import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL1;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Send_Notification extends AppCompatActivity {

    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Attendance_model> list = new ArrayList<Attendance_model>();
    private RecyclerView mRecyclerView;
    private Performance_Adapter studentAdapter;
    private final String TAG = "MyApp";
    String message;
    Button submitnotify;
    Spinner spinnersubject,spinnerclass;
    String course_id="1",classroom_id="1";
    Bundle bundle;
    EditText messageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__notification);

        sharedpref= this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        metadata = sharedpref.getString(Constants.METAKEY, "null");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bundle=getIntent().getExtras();
        if(bundle!=null) {
            course_id = bundle.getString("course_id");
            classroom_id = bundle.getString("class_id");
        }
        spinnertasks();
        submitnotify=(Button)findViewById(R.id.sendnotify);
        messageview=(EditText)findViewById(R.id.message);
        submitnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submittask();
            }
        });

    }

    void spinnertasks() {
        spinnersubject = (Spinner) findViewById(R.id.course_spinner);
        Set<String> defaultval = new HashSet<String>();
        defaultval.add("English");
        defaultval.add("Mathmatics");
        Set<String> values = sharedpref.getStringSet(Constants.COURSES, defaultval);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, values.toArray());
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnersubject.setAdapter(adapter);
        Log.v(TAG, "course_id will contain" + course_id);
        spinnersubject.setSelection(Integer.parseInt(course_id) - 1);
        spinnersubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_id = Integer.toString(position + 1);
                spinnertask2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    void spinnertask2()
    {
        spinnerclass=(Spinner)findViewById(R.id.classroom_spinner);
        Set<String> defaultvalclass = new HashSet<String>();
        defaultvalclass.add("I A");
        defaultvalclass.add("I C");
        Set<String> values1 = sharedpref.getStringSet(Constants.CLASS, defaultvalclass);

        ArrayAdapter adapter1= new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, values1.toArray());
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerclass.setAdapter(adapter1);
        Log.v(TAG,"classroom_id will contain "+classroom_id);
        spinnerclass.setSelection(Integer.parseInt(classroom_id) - 1);
        spinnerclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classroom_id = Integer.toString(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    void submittask()
    {
        message=messageview.getText().toString();
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        if(course_id==null)course_id="1";
        if(classroom_id==null)classroom_id="1";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL1.getnotificationURL(course_id, classroom_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                Toast.makeText(Send_Notification.this,"Notification send to all Students",Toast.LENGTH_LONG).show();
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
            @Override
            public Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                params.put("action", "notifyStudents");
                return params;
            }

            ;
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Notification..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }
}

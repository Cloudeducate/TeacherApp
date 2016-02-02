package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.cloudeducate.redtick.Adapters.AssignmentRecyclerviewAdapter;
import com.cloudeducate.redtick.Model.Assignment_model;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL1;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Assignment extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Assignment_model> list = new ArrayList<Assignment_model>();
    private RecyclerView mRecyclerView;
    private SharedPreferences sharedpref;
    private String course_id="1";
    private String metadata;
    private AssignmentRecyclerviewAdapter assignmentRecyclerviewAdapter;
    public static final String TAG = "MyApp";
    private TextView create;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedpref = this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        metadata = sharedpref.getString(Constants.METAKEY, "null");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Assignment.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);


        bundle=getIntent().getExtras();
        if(bundle!=null)
        course_id=bundle.getString("course_id");

        create=(TextView)findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create=new Intent(Assignment.this,Create_Assignment.class);
                startActivity(create);
            }
        });
        subjectspinnertask();
        //fetchData();

    }
    public void subjectspinnertask() {
        Set<String> defaultval = new HashSet<String>();
        defaultval.add("English");
        defaultval.add("Mathmatics");
        Set<String> values = sharedpref.getStringSet(Constants.COURSES, defaultval);
        Spinner spinner = (Spinner) findViewById(R.id.course_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, values.toArray());
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(Integer.parseInt(course_id));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_id = Integer.toString(position+1);
                fetchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fetchData();
            }
        });
    }

    public void fetchData() {
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        if (course_id == null) {
            course_id = "1";
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL1.getAssignmentURL(course_id), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString() == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response.toString());
                list = parseJson(response.toString());
                assignmentRecyclerviewAdapter = new AssignmentRecyclerviewAdapter(Assignment.this, list);
                mRecyclerView.setAdapter(assignmentRecyclerviewAdapter);
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

            ;
        };

        requestQueue.add(jsonObjectRequest);
    }

    public List<Assignment_model> parseJson(String jsonString) {
        List<Assignment_model> assignmentList = new ArrayList<Assignment_model>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.ASSIGNMENTS);


                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {

                    Assignment_model assignment = new Assignment_model();

                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    assignment.setTitle(jsonObject.getString(Constants.ASSIGNMENT_TITLE));
                    assignment.setCourse(jsonObject.getString(Constants.ASSIGNMENT_COURSE));
                    assignment.setDeadline(jsonObject.getString(Constants.ASSIGNMENT_DEADLINE));
                    assignment.setClassroom(jsonObject.getString(Constants.COURSE_ID) + " " + jsonObject.getString(Constants.CLASSROOM_ID));
                    assignment.setClassroomid(jsonObject.getString(Constants.CLASSROOM_ID));
                    assignment.setCourseid(jsonObject.getString(Constants.COURSE_ID));
                    assignment.setId(jsonObject.getString(Constants.ASSIGNMENT_ID));
                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.ASSIGNMENT_TITLE)));

                    assignmentList.add(assignment);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " + String.valueOf(assignmentList));

        return assignmentList;

    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Assignments");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }


}

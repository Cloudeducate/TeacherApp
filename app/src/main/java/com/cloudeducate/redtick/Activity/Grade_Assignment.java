package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

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
import com.cloudeducate.redtick.Adapters.GradeAssignmentAdapter;
import com.cloudeducate.redtick.Adapters.Studentlist_Adapter;
import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL1;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grade_Assignment extends AppCompatActivity {

    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Attendance_model> list = new ArrayList<Attendance_model>();
    private RecyclerView mRecyclerView;
    private GradeAssignmentAdapter studentAdapter;
    private final String TAG = "MyApp";
    String assignment_id;
    Button submit;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade__assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref= this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        metadata = sharedpref.getString(Constants.METAKEY, "null");
        Log.v(TAG, metadata + "meta value for teacher");

        bundle=getIntent().getExtras();
        assignment_id=bundle.getString("assignment_id");
        mRecyclerView = (RecyclerView) findViewById(R.id.rvgradelist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Grade_Assignment.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        gradetask();
    }

    void gradetask()
    {
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        if(assignment_id==null)assignment_id="1";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL1.getsubmitgradeURL(assignment_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJson(response);
                studentAdapter = new GradeAssignmentAdapter(Grade_Assignment.this, list);
                mRecyclerView.setAdapter(studentAdapter);
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

    public List<Attendance_model> parseJson(String jsonString) {
        List<Attendance_model> resultList = new ArrayList<Attendance_model>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                String message=jsonObjMain.getString(Constants.MESSAGE);
                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.STUDENTS);


                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {

                    Attendance_model attendance_model=new Attendance_model();

                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    attendance_model.setstudentname(jsonObject.getString(Constants.NAME));
                    attendance_model.setrollno(jsonObject.getString(Constants.ROLLNO));
                    attendance_model.setuserid(jsonObject.getString(Constants.USER_ID));
                    attendance_model.setstudentremark(jsonObject.getString(Constants.REMARKS));
                    if(jsonObject.getString(Constants.GRADE)==null)
                        attendance_model.setGradevalue(10);
                    else
                    attendance_model.setGradevalue(Integer.parseInt(jsonObject.getString(Constants.GRADE)));
                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.NAME)));

                    resultList.add(attendance_model);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " + String.valueOf(resultList));

        return resultList;

    }
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Results");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }
}

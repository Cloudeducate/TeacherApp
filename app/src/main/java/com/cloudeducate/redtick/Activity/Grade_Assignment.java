package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    String assignment_id,message;
    String[] userid_array,grade_array,remark_array;
    Button submit;
    Bundle bundle;
    CardView submitcard;


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
        submitcard=(CardView)findViewById(R.id.submitlayout);
        submitcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submittask();
            }
        });
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

    public void submittask() {

        int size=list.size();
        userid_array = new String[size];
        grade_array = new String[size];
        remark_array=new String[size];
        for (int i = 0; i < size; i++) {

            Attendance_model attendance_model = new Attendance_model();
            // Creating JSONObject from JSONArray
             attendance_model = list.get(i);
            String userid = attendance_model.getuserid();
            int valueofpresence = attendance_model.getGradevalue();
            String presence = Integer.toString(valueofpresence);
            String remark = attendance_model.getstudentremark();
            Log.v(TAG, userid + " " + presence);
            userid_array[i] = userid;
            grade_array[i] = presence;
            remark_array[i] = remark;
            Log.v(TAG, userid_array.toString() + " " + grade_array.toString() + " " + remark_array);
        }
            Toast.makeText(this, "Submiting Assignment Grade..", Toast.LENGTH_LONG).show();
            PerformanceTask performancesubmit=new PerformanceTask();
            performancesubmit.execute();
            if(message!=null)
                Toast.makeText(Grade_Assignment.this,message,Toast.LENGTH_LONG).show();

    }

    public class PerformanceTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        PerformanceTask()
        {
           assignment_id="1";
            mlink = "http://cloudeducate.com//assignments/gradeIt/"+assignment_id+".json";

        }
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                URL url=new URL(mlink);
                StringBuilder postData=new StringBuilder();
                for(int i=0;i<userid_array.length;i++) {

                    if (postData.length() != 0)
                        postData.append('&');
                    postData.append(URLEncoder.encode("grade[]", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(grade_array[i], "UTF-8"));

                    if (postData.length() != 0)
                        postData.append('&');
                    postData.append(URLEncoder.encode("remarks[]", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(remark_array[i], "UTF-8"));

                    if (postData.length() != 0)
                        postData.append('&');
                    postData.append(URLEncoder.encode("user_id[]", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(userid_array[i], "UTF-8"));
                }
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("action", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode("saveMarks","UTF-8"));
                //Log.v(TAG,param.toString());

                Log.v(TAG,"post url "+postData.toString());
                byte[] postDataBytes=postData.toString().getBytes("UTF-8");
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-Access-Token", metadata);
                conn.setRequestProperty("X-App", "teacher");
                conn.getOutputStream().write(postDataBytes);
                InputStream inputStream = conn.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    return "null_inputstream";
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line ;

                while ( (line=bufferedReader.readLine())!=null ){
                    buffer.append(line + '\n');
                }

                if (buffer.length() == 0) {
                    return "null_inputstream";
                }

                String stringJSON = buffer.toString();
                Log.v("MyApp", "JSON retured in Attendance" + stringJSON);
                return stringJSON;

            } catch (UnknownHostException | ConnectException e) {
                error = "null_internet" ;
                e.printStackTrace();
            } catch (IOException e) {
                error= "null_file";
                e.printStackTrace();
            } finally {
                if ( conn!= null) {
                    conn.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
//                        Log.e(LOG_CAT, "ErrorClosingStream", e);
                    }
                }
            }

            return error;
        }
        @Override
        protected void onPostExecute(final String success) {
            try {
                JSONObject jsonobject=new JSONObject(success);
                message=jsonobject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

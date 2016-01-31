package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.cloudeducate.redtick.Adapters.AttendanceAdapter;
import com.cloudeducate.redtick.Model.Attendance_model;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;

import com.cloudeducate.redtick.Utils.URL1;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Manage_Attendance extends AppCompatActivity {

    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Attendance_model> list = new ArrayList<Attendance_model>();
    private RecyclerView mRecyclerView;
    private AttendanceAdapter attendanceAdapter;
    private final String TAG = "MyApp";
    Button submit;
    JSONObject user_json,presence_json;
    String[] userid_array=null;
    String[] presence_array=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref= this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        metadata = sharedpref.getString(Constants.METAKEY, "null");
        Log.v(TAG,metadata+"meta value for teacher");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvattendance);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Manage_Attendance.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submittask();
            }
        });

        attendancetask();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    void attendancetask()
    {
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL1.getAttendanceURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJson(response);
                attendanceAdapter = new AttendanceAdapter(Manage_Attendance.this, list);
                mRecyclerView.setAdapter(attendanceAdapter);
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
                    if(jsonObject.getString(Constants.PRESENCE)==null)
                        attendance_model.setAttendancevalue(1);
                    else
                        attendance_model.setAttendancevalue(jsonObject.getInt(Constants.PRESENCE));


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


        userid_array = new String[list.size()];
        presence_array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {

            Attendance_model attendance_model = new Attendance_model();

            // Creating JSONObject from JSONArray
            attendance_model = list.get(i);
            String userid = attendance_model.getuserid();
            int valueofpresence = attendance_model.getAttendancevalue();
            String presence = Integer.toString(valueofpresence);

            Log.v(TAG, userid + " " + presence);
            userid_array[i] = userid;
            presence_array[i] = presence;
            Log.v(TAG, userid_array.toString() + " " + presence_array.toString());
            Toast.makeText(this,"Submiting Attendance..",Toast.LENGTH_LONG);
            AttendanceTask attendancesubmit=new AttendanceTask();
            attendancesubmit.execute();
        }
    }

    public class AttendanceTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        AttendanceTask()
        {
            mlink = "http://cloudeducate.com/teacher/manageAttendance.json";
            userid_array=new String[list.size()];
            presence_array=new String[list.size()];
            for (int i = 0; i < list.size(); i++) {

                Attendance_model attendance_model=new Attendance_model();

                // Creating JSONObject from JSONArray
                attendance_model=list.get(i);
                String userid=attendance_model.getuserid();
                int valueofpresence=attendance_model.getAttendancevalue();
                String presence=Integer.toString(valueofpresence);

                Log.v(TAG,userid+" "+presence);
                userid_array[i]=userid;
                presence_array[i]=presence;
                Log.v(TAG,userid_array.toString()+" "+presence_array.toString());
            }
        }
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                URL url=new URL(mlink);
                Map<String,Object> param=new LinkedHashMap<String, Object>();
                StringBuilder postData=new StringBuilder();
                //Iterator<String> paramiterator=param.keySet().iterator();
                for(int i=0;i<userid_array.length;i++) {

                    if (postData.length() != 0)
                        postData.append('&');
                    postData.append(URLEncoder.encode("presence[]", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(presence_array[i], "UTF-8"));

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
                postData.append(URLEncoder.encode("saveAttendance","UTF-8"));
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
                String message=jsonobject.getString("message");
                String classsupdated=jsonobject.getString("class");
                Toast.makeText(Manage_Attendance.this,message+" For Class "+classsupdated,Toast.LENGTH_LONG);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }






    }






}

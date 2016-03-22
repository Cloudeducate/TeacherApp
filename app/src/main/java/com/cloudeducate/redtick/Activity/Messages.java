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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.android.volley.toolbox.StringRequest;
import com.cloudeducate.redtick.Adapters.ConversationAdapter;
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

public class Messages extends AppCompatActivity {
    String jsonData;
    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog,progressDialog2,progressDialog1;
    private List<String[]> list = new ArrayList<String[]>();
    private RecyclerView mRecyclerView;
    private ConversationAdapter conversationAdapter;
    private final String TAG = "MyApp";
    private TextView createview;
    private AutoCompleteTextView actv;
    private TextView create;
    private int size=4;
    private String username,useridselected,displayselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref= this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        metadata = sharedpref.getString(Constants.METAKEY, "null");
        Log.v(TAG, metadata + "meta value for teacher");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvallconv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Messages.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        actv=(AutoCompleteTextView)findViewById(R.id.studentlist);
        conversationtask();
        newconversationtask();
        create=(TextView)findViewById(R.id.createnewconv);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=actv.getText().toString();
                String[] studentlist=list.get(0);
                String[] display_list = list.get(1);
                String[] username_list = list.get(2);
                String[] userid_list = list.get(3);
                for(int i=0;i<studentlist.length;i++) {
                    if (name.equals(studentlist[i])) {
                        useridselected = userid_list[i];
                        displayselect = display_list[i];
                        username = username_list[i];
                        createnewtask();
                    }
                }
            }

        });

    }
    void conversationtask()
    {
        Log.v(TAG, "fetchData conv is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL1.getconversation(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJson(response);
                conversationAdapter = new ConversationAdapter(Messages.this, list);
                mRecyclerView.setAdapter(conversationAdapter);
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
            };
        };
        requestQueue.add(jsonObjectRequest);
    }

    public List<String[]> parseJson(String jsonString) {
        List<String[]> resultList = new ArrayList<String[]>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.CONVERSATION);

                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String[] string=new String[2];
                    string[0]=jsonObject.getString(Constants.CONVERSATION_DISPLAY);
                    string[1]=jsonObject.getString(Constants.CONVERSATION_ID);
                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.CONVERSATION_DISPLAY)+String.valueOf(jsonObject.getString(Constants.CONVERSATION_ID))));
                    resultList.add(string);
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
        progressDialog.setMessage("Getting Old Conversation");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    void newconversationtask()
    {
        Log.v(TAG, "fetchData conv is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        shownameDialog();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL1.getcreateconversation(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog2.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJsonConv(response);
                String[] studentlist=new String[size];
                studentlist=list.get(0);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Messages.this,android.R.layout.simple_list_item_1,studentlist);
                actv.setAdapter(adapter);
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
            };
        };
        requestQueue.add(jsonObjectRequest);
    }

    public List<String[]> parseJsonConv(String jsonString) {
        List<String[]> resultList = new ArrayList<String[]>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.STUDENTS);
                size=jsonArray.length();
                String[] string=new String[size];
                String[] string2=new String[size];
                String[] string3=new String[size];
                String[] string4=new String[size];
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    string[i]=jsonObject.getString(Constants.NAME);
                    string2[i]=jsonObject.getString(Constants.DISPLAY);
                    string3[i]=jsonObject.getString(Constants.USERNNAME);
                    string4[i]=jsonObject.getString(Constants.USER_ID);
                }
                resultList.add(string);
                resultList.add(string2);
                resultList.add(string3);
                resultList.add(string4);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.v(TAG, "List = " + String.valueOf(resultList));
        return resultList;
    }
    public void shownameDialog() {

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Getting Student list");
        progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog2.setIndeterminate(true);
        progressDialog2.show();
    }
    void createnewtask()
    {
        Log.v(TAG, "Creating Conversation is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showprogressofcreation();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL1.getcreateconversation(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog1.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "responseofcreation = " + response);
                list = parseJsoncreate(response);
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
            };
            @Override
            public Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.v(TAG,displayselect+useridselected+username);
                params.put("display", displayselect);
                params.put("identifier",username);
                params.put("user_id", useridselected);
                params.put("action", "newConv");
                return params;
            };
        };
        requestQueue.add(jsonObjectRequest);
    }

    public List<String[]> parseJsoncreate(String jsonString) {
        List<String[]> resultList = new ArrayList<String[]>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                JSONObject jsonname=jsonObjMain.getJSONObject("conversation");
                String Conversation_id=jsonname.getString("id");
                Bundle bundle=new Bundle();
                bundle.putString("conversation_id",Conversation_id);
                Intent intent=new Intent(Messages.this,View_Conversation.class).putExtras(bundle);
                startActivity(intent);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " + String.valueOf(resultList));

        return resultList;

    }
    public void showprogressofcreation() {

        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Creating new conversation");
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog1.setIndeterminate(true);
        progressDialog1.show();
    }

}

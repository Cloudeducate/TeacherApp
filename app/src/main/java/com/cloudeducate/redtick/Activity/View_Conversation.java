package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.cloudeducate.redtick.Adapters.ConversationAdapter;
import com.cloudeducate.redtick.Adapters.ViewConversationAdapter;
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

public class View_Conversation extends AppCompatActivity {

    String jsonData;
    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<String[]> list = new ArrayList<String[]>();
    private RecyclerView mRecyclerView;
    private ViewConversationAdapter conversationAdapter;
    private final String TAG = "MyApp";
    private String convname;
    private TextView conview,send;
    private EditText messagesend;
    private Boolean success;
    private String conversation_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref= this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        metadata = sharedpref.getString(Constants.METAKEY, "null");
        Log.v(TAG, metadata + "meta value for teacher");

        Bundle bundle=getIntent().getExtras();
        conversation_id=bundle.getString("conversation_id");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvmessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(View_Conversation.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        messagetask();
        conview=(TextView)findViewById(R.id.Convname);
        if(convname!="null")
            conview.setText(convname);
        send=(TextView) findViewById(R.id.sendnewmessage);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtask();
            }
        });
    }

    void messagetask()
    {
        Log.v(TAG, "fetchData conv is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();

        if(conversation_id=="null")conversation_id="";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL1.getmessages(conversation_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJson(response);
                conversationAdapter = new ViewConversationAdapter(View_Conversation.this, list);
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

                JSONObject jsonname=jsonObjMain.getJSONObject("conversation");
                convname=jsonname.getString("title");
                JSONObject jsonuser=jsonObjMain.getJSONObject("user");
                String useradmin=jsonuser.getString("_id");
                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.MESSAGES);

                // JSONArray has x JSONObject
                for (int i = jsonArray.length()-1; i>=0; i--) {
                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String[] string=new String[4];
                    string[0]=jsonObject.getString(Constants.CONTENT);
                    string[1]=jsonObject.getString(Constants.CREATED);
                    if(jsonObject.getString(Constants.USER_ID).equals(useradmin))
                        string[2]="By You";
                    else string[2]="By Student";
                    string[3]=jsonObject.getString(Constants.LIVE);

                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.CONTENT)+String.valueOf(jsonObject.getString(Constants.CREATED))));
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
    public void showProgressDialog1() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Message");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }
    void sendtask()
    {
        Log.v(TAG, "fetchData conv is called");
        messagesend=(EditText)findViewById(R.id.textmessage);
        final String messagesending=messagesend.getText().toString();
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog1();

        if(conversation_id=="null")conversation_id="";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL1.sendmessages(conversation_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                success=parsemessage(response);
                Log.v(TAG,success.toString());
                    messagetask();
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
                params.put("action", "sendMessage");
                params.put("message", messagesending);
                return params;
            };
        };
        requestQueue.add(jsonObjectRequest);
    }
    public Boolean parsemessage(String jsonString) {
        Boolean success1=false;
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.MESSAGES);
                for (int i = jsonArray.length()-1; i>=0; i--) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String[] string=new String[1];
                    string[0]=jsonObject.getString(Constants.CONTENT);
                    Toast.makeText(this,"Message Successfully Send",Toast.LENGTH_LONG).show();
                    success1=true;
                }
            } catch (JSONException e) {
               success1=false;
                e.printStackTrace();
            }

        }
        else
        success1=false;
        return success1;
    }
}

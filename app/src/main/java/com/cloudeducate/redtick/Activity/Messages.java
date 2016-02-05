package com.cloudeducate.redtick.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.cloudeducate.redtick.Adapters.Courses_Adapter;
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

public class Messages extends AppCompatActivity {
    String jsonData;
    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<String[]> list = new ArrayList<String[]>();
    private RecyclerView mRecyclerView;
    private ConversationAdapter conversationAdapter;
    private final String TAG = "MyApp";
    private TextView createview;

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
        conversationtask();
        createview=(TextView)findViewById(R.id.create);
        createview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Messages.this,Create_Conversation.class);
                startActivity(intent);
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

}

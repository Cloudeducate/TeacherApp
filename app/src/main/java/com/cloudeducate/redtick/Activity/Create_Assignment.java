package com.cloudeducate.redtick.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudeducate.redtick.Dialog.FileChooserDialog;
import com.cloudeducate.redtick.R;
//import com.afollestad.materialdialogs.folderselector.FileChooserDialog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Create_Assignment extends AppCompatActivity implements FileChooserDialog.FileCallback{

    EditText title,description,deadline,path;
    Button browse,post;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private final static int STORAGE_PERMISSION_RC = 69;
    String upLoadServerUri="http://cloudeducate.com/assignments/create/1/1.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        deadline = (EditText) findViewById(R.id.deadline);
        path = (EditText) findViewById(R.id.path);
        browse = (Button) findViewById(R.id.browse);
        post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(Create_Assignment.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.v("MyApp", "uploading started.....");
                            }
                        });

                        uploadFile(path.getText().toString());

                    }
                }).start();
            }
        });
    }

    public void browse(View view) {
        //browse = R.id.browse;
        if (ActivityCompat.checkSelfPermission(Create_Assignment.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Create_Assignment.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , STORAGE_PERMISSION_RC);
            return;
        }
        new FileChooserDialog.Builder(this)
                .show();
    }

    @Override
    public void onFileSelection(@NonNull File file) {
        path.setText(file.getAbsolutePath());
    }


    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"+
                    path.getText().toString());

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Source File not exist :"
                      //      +uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                StringBuilder postData=new StringBuilder();

                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("title", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( title.getText().toString(),"UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("description", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( description.getText().toString(),"UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("deadline", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(deadline.getText().toString(), "UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("action", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode("assignment","UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("file", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(fileName,"UTF-8"));
                //Log.v(TAG,param.toString());

                Log.v("MyApp","post url "+postData.toString());
                byte[] postDataBytes=postData.toString().getBytes("UTF-8");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.getOutputStream().write(postDataBytes);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"attachment\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +"http://cloudeducate.com/assignments/create/{$course_id}/{$classroom_id}.json"
                                    +path.toString();

                            //messageText.setText(msg);
                            Toast.makeText(Create_Assignment.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(Create_Assignment.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(Create_Assignment.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        if(requestCode==100){
            if(resultCode==RESULT_OK){
                Uri uri=intent.getData();
                if(uri.getScheme().toString().compareTo("content")==0){
                    Cursor cursor =getContentResolver().query(uri, null, null, null, null);
                    if (cursor.moveToFirst())
                    {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                        Uri filePathUri = Uri.parse(cursor.getString(column_index));
                        String file_name = filePathUri.getLastPathSegment().toString();
                        String file_path=filePathUri.getPath();
                        path.setText(file_path +" "+file_name);
                        Toast.makeText(this, "File Name & PATH are:" + file_name + "\n" + file_path, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


}

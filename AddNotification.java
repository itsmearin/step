package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddNotification extends Activity {

    EditText nsEdit, ndEdit;
    Button nsubmitButton;
    String subjectString, descriptionString;

    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String addUrl = "http://www.wikihands.com/ebridge/addnotification.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        nsEdit = (EditText) findViewById(R.id.notificationsubject);
        ndEdit = (EditText) findViewById(R.id.notificationdesc);

        nsubmitButton = (Button) findViewById(R.id.notificationsubmit);
        nsubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subjectString = nsEdit.getText().toString();
                descriptionString = ndEdit.getText().toString();

                new Notificationadding().execute();
            }
        });
    }

    public class Notificationadding extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddNotification.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences share= PreferenceManager.getDefaultSharedPreferences(AddNotification.this);
            String userString=share.getString("FUSERNAME",null);
            try {
                List<NameValuePair> args = new ArrayList<>();

                args.add(new BasicNameValuePair("subject", subjectString));
                args.add(new BasicNameValuePair("description", descriptionString));
                args.add(new BasicNameValuePair("username", userString));

                JSONObject object = parser.makeHttpRequest(addUrl, "POST", args);


            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddNotification.this, "Notification added successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}

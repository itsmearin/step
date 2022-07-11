package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class DoubtSending extends Activity {

    EditText dsubEdit,ddescEdit;
    Button dsubmitButton;

    String dsubString,ddescString;

    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String adddoubtUrl = "http://www.wikihands.com/ebridge/adddoubt.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_sending);

        dsubEdit= (EditText) findViewById(R.id.doubtsubject);
        ddescEdit= (EditText) findViewById(R.id.doubtdescription);
        dsubmitButton= (Button) findViewById(R.id.doubtsending);

        dsubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dsubString=dsubEdit.getText().toString();
                ddescString=ddescEdit.getText().toString();

                new Senddoubt().execute();
            }
        });
    }
    public class Senddoubt extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DoubtSending.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> args = new ArrayList<>();

                args.add(new BasicNameValuePair("subject", dsubString));
                args.add(new BasicNameValuePair("description", ddescString));

                JSONObject object = parser.makeHttpRequest(adddoubtUrl, "POST", args);


            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(DoubtSending.this, "Notification added successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

}

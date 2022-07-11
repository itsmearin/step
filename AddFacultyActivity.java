package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddFacultyActivity extends Activity {

    EditText nameEdit, mobileEdit, emailEdit, departEdit, usernameEdit, passwordEdit;
    Button addButton;
    String nameString,mobileString,emailString,departString,usernameString,passwordString;

    ProgressDialog progressDialog;
    JSONParser parser=new JSONParser();
    public String addUrl="http://www.wikihands.com/ebridge/facultyregister.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);
        nameEdit = (EditText) findViewById(R.id.facultyname);
        mobileEdit = (EditText) findViewById(R.id.facultymobile);
        emailEdit = (EditText) findViewById(R.id.facultyemail);
        departEdit = (EditText) findViewById(R.id.facultydepartment);
        usernameEdit = (EditText) findViewById(R.id.facultyusername);
        passwordEdit = (EditText) findViewById(R.id.facultypassword);

        addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameString=nameEdit.getText().toString();
                mobileString=mobileEdit.getText().toString();
                emailString=emailEdit.getText().toString();
                departString=departEdit.getText().toString();
                usernameString=usernameEdit.getText().toString();
                passwordString=passwordEdit.getText().toString();
                ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    new Addfaculty().execute();
                }else{
                    Toast.makeText(AddFacultyActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public class Addfaculty extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(AddFacultyActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> args = new ArrayList<>();

            args.add(new BasicNameValuePair("name",nameString));
            args.add(new BasicNameValuePair("phone",mobileString));
            args.add(new BasicNameValuePair("mail",emailString));
            args.add(new BasicNameValuePair("department",departString));
            args.add(new BasicNameValuePair("username",usernameString));
            args.add(new BasicNameValuePair("password",passwordString));

            JSONObject object=parser.makeHttpRequest(addUrl,"POST",args);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent in = new Intent(AddFacultyActivity.this, AdminHomeActivity.class);
            startActivity(in);

//            SmsManager manager=SmsManager.getDefault();
//            manager.sendTextMessage(mobileString,null,usernameString+""+passwordString,null,null);

            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_faculty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

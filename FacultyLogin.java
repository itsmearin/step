package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
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


public class FacultyLogin extends Activity {

    EditText fusrEdit,fpswEdit;
    Button floginButton;
    String userString,passwordString;

    ProgressDialog progressDialog;
    JSONParser parser=new JSONParser();
    JSONArray response;
    public String addUrl="http://www.wikihands.com/ebridge/facultylogin.php";
    String responseUser,responsePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faculty_login);

        fusrEdit= (EditText) findViewById(R.id.fusername);
        fpswEdit= (EditText) findViewById(R.id.fpassword);

        floginButton= (Button) findViewById(R.id.flogin);
        floginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userString=fusrEdit.getText().toString();
                passwordString=fpswEdit.getText().toString();
                ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()==true ) {
                    new Login().execute();
                }else{
                    Toast.makeText(FacultyLogin.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public class Login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(FacultyLogin.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> args = new ArrayList<>();

                args.add(new BasicNameValuePair("username", userString));

                JSONObject object = parser.makeHttpRequest(addUrl, "POST", args);

                response=object.getJSONArray("facultylogin");

                for (int i=0;i<response.length();i++)
                {
                    JSONObject c=response.getJSONObject(i);
                    responseUser=c.getString("username");
                    responsePass=c.getString("password");

                }

            }catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(responseUser.equals(userString)&&responsePass.equals(passwordString)) {
                Intent in = new Intent(FacultyLogin.this, FacultyHomeActivity.class);
                startActivity(in);

                SharedPreferences share= PreferenceManager.getDefaultSharedPreferences(FacultyLogin.this);
                SharedPreferences.Editor editor=share.edit();
                editor.putString("FUSERNAME",responseUser);
                editor.commit();
            }
            else
            {
                Toast.makeText(FacultyLogin.this,"Invalid username or password!",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faculty_login, menu);
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

package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentRegisterActivity extends Activity {

    EditText nameEdit,mobileEdit,mailEdit,departEdit,usernameEdit,passwordEdit;
    Button submitButton;

    ProgressDialog progressDialog;
    JSONParser parser=new JSONParser();
    public static String register_url="http://www.wikihands.com/ebridge/studentregister.php";

    String nameString,mobileString,mailString,departString,usernameString,passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        nameEdit= (EditText) findViewById(R.id.name);
        mobileEdit= (EditText) findViewById(R.id.mobile);
        mailEdit= (EditText) findViewById(R.id.mail);
        departEdit= (EditText) findViewById(R.id.depart);
        usernameEdit= (EditText) findViewById(R.id.username);
        passwordEdit= (EditText) findViewById(R.id.password);

        submitButton= (Button) findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameString=nameEdit.getText().toString();
                mobileString=mobileEdit.getText().toString();
                mailString=mailEdit.getText().toString();
                departString=departEdit.getText().toString();
                usernameString=usernameEdit.getText().toString();
                passwordString=passwordEdit.getText().toString();


                if (nameString.length()>0&&mobileString.length()>0&&mailString.length()>0&&departString.length()>0&&usernameString.length()>0&&passwordString.length()>0)
                {
                    ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                    if(networkInfo != null && networkInfo.isConnected()==true ) {
                        new Register().execute();
                    }else{
                        Toast.makeText(StudentRegisterActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(StudentRegisterActivity.this,"Enter all the fields ",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public class Register extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try
            {
                List<NameValuePair> args=new ArrayList<>();
                args.add(new BasicNameValuePair("name",nameString));
                args.add(new BasicNameValuePair("phone",mobileString));
                args.add(new BasicNameValuePair("mail",mailString));
                args.add(new BasicNameValuePair("department",departString));
                args.add(new BasicNameValuePair("username",usernameString));
                args.add(new BasicNameValuePair("password",passwordString));

                JSONObject object=parser.makeHttpRequest(register_url,"POST",args);


            }catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            Intent in=new Intent(StudentRegisterActivity.this,StudentLoginActivity.class);
            startActivity(in);
        }
    }
}

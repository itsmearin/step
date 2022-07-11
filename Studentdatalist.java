package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Studentdatalist extends Activity {

    ListView dataListView;

    public static ArrayList<String> subjectArray = new ArrayList<>();
    public static ArrayList<String> descritionArray = new ArrayList<>();
    public static ArrayList<String> usernameArray = new ArrayList<>();
    public static ArrayList<String> timeArray = new ArrayList<>();
    public static ArrayList<String> imageArray = new ArrayList<>();



    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String gettimetableUrl = "http://www.wikihands.com/ebridge/gettimetable.php";
    public String getquestionsUrl = "http://www.wikihands.com/ebridge/getquestionpapers.php";
    public String getnotificationUrl = "http://www.wikihands.com/ebridge/getnotification.php";

    int getInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentdatalist);

        dataListView = (ListView) findViewById(R.id.datalist);

        getInt = getIntent().getIntExtra("selection", 0);


        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent(Studentdatalist.this, StudentdataViewing.class);
                in.putExtra("viewdata", getInt);
                in.putExtra("position", position);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        subjectArray.clear();
        descritionArray.clear();

        imageArray.clear();

        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            if (getInt == 1) {
                new Gettimetable().execute();
            } else if (getInt == 2) {

                new Getquestions().execute();
            } else if (getInt == 3) {
                new GetNotifications().execute();
            }
        }else{
            Toast.makeText(Studentdatalist.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    //Get time table

    public class Gettimetable extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Studentdatalist.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> args = new ArrayList<>();

                JSONObject object = parser.makeHttpRequest(gettimetableUrl, "GET", args);

                response = object.getJSONArray("timetabledetails");

                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);
                    String subject = c.getString("subject");
                    String desription = c.getString("description");
                    String image = c.getString("image");

                    subjectArray.add(subject);
                    descritionArray.add(desription);
                    imageArray.add(image);
                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayAdapter<String> facultyAdapter = new ArrayAdapter<String>(Studentdatalist.this, android.R.layout.simple_list_item_1, subjectArray);
            dataListView.setAdapter(facultyAdapter);
            progressDialog.dismiss();
        }
    }

    //Get questions

    public class Getquestions extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Studentdatalist.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> args = new ArrayList<>();

                JSONObject object = parser.makeHttpRequest(getquestionsUrl, "GET", args);

                response = object.getJSONArray("questiondetails");

                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);
                    String subject = c.getString("subject");
                    String desription = c.getString("description");
                    String image = c.getString("image");

                    subjectArray.add(subject);
                    descritionArray.add(desription);
                    imageArray.add(image);
                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayAdapter<String> facultyAdapter = new ArrayAdapter<String>(Studentdatalist.this, android.R.layout.simple_list_item_1, subjectArray);
            dataListView.setAdapter(facultyAdapter);
            progressDialog.dismiss();
        }
    }

    //Get notifications

    public class GetNotifications extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Studentdatalist.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> args = new ArrayList<>();

                JSONObject object = parser.makeHttpRequest(getnotificationUrl, "GET", args);

                response = object.getJSONArray("notifications");

                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);
                    String subject = c.getString("subject");
                    String desription = c.getString("description");
                    String usernmae = c.getString("username");
                    String time = c.getString("time");


                    subjectArray.add(subject);
                    descritionArray.add(desription);
                    usernameArray.add(usernmae);
                    timeArray.add(time);

                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

         //   ArrayAdapter<String> facultyAdapter = new ArrayAdapter<String>(Studentdatalist.this, android.R.layout.simple_list_item_1, viewArray);
            dataListView.setAdapter(new MyNotifications());
            progressDialog.dismiss();
        }
    }

    public class MyNotifications extends BaseAdapter
    {
        @Override
        public int getCount() {
            return subjectArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.notificaton,null);
            TextView sub= (TextView) convertView.findViewById(R.id.notsub);
            TextView time= (TextView) convertView.findViewById(R.id.nottime);

            sub.setText(subjectArray.get(position).toString());
            time.setText(timeArray.get(position).toString());

            return convertView;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_studentdatalist, menu);
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

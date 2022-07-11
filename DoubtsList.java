package com.tracking.ebridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DoubtsList extends Activity {

    ListView fdoubtListView;

    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String getdoubtUrl = "http://www.wikihands.com/ebridge/getdoubts.php";

    ArrayList<String> subjectArray = new ArrayList<>();
    ArrayList<String> descriptionArray = new ArrayList<>();
    ArrayList<String> timeArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubts_list);

        fdoubtListView = (ListView) findViewById(R.id.fdoubtslist);

        new Getdoubts().execute();

        fdoubtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(DoubtsList.this, Viewdoubt.class);
                in.putExtra("description", descriptionArray.get(position).toString());
                startActivity(in);
            }
        });
    }

    public class Getdoubts extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DoubtsList.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> args = new ArrayList<>();


                JSONObject object = parser.makeHttpRequest(getdoubtUrl, "GET", args);

                response = object.getJSONArray("doubts");

                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);
                    String subject = c.getString("subject");
                    String description = c.getString("description");
                    String time = c.getString("time");


                    subjectArray.add(subject);
                    descriptionArray.add(description);
                    timeArray.add(time);

                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

          //  ArrayAdapter<String> facultyAdapter = new ArrayAdapter<String>(DoubtsList.this, android.R.layout.simple_list_item_1, subjectArray);
            fdoubtListView.setAdapter(new MyDoubts());
            progressDialog.dismiss();
        }
    }
    public class MyDoubts extends BaseAdapter
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
        getMenuInflater().inflate(R.menu.menu_doubts_list, menu);
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

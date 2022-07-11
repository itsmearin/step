package com.tracking.ebridge;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class AddTimeTable extends Activity implements View.OnClickListener {

    EditText tsEdit, tdEdit;
    Button tcButton, tsubmitButton;
    ImageView tImageView;

    private static final int CAMERA_REQUEST = 1888,RESULT_LOAD_IMAGE=100;
    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;

    public String timetableUrl = "http://www.wikihands.com/ebridge/addtimetable.php";

    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);

        tsEdit = (EditText) findViewById(R.id.timesubject);
        tdEdit = (EditText) findViewById(R.id.timedescription);
        tcButton = (Button) findViewById(R.id.timechooseimage);
        tImageView = (ImageView) findViewById(R.id.timeimageView);
        tsubmitButton = (Button) findViewById(R.id.timesubmit);

        tcButton.setOnClickListener(this);
        tsubmitButton.setOnClickListener(this);
    }

    public class Timetableadding extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddTimeTable.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String subjectString = tsEdit.getText().toString();
            String descString = tdEdit.getText().toString();
            String imageString = getStringImage(photo);

            try {
                List<NameValuePair> args = new ArrayList<>();
                args.add(new BasicNameValuePair("subject", subjectString));
                args.add(new BasicNameValuePair("description", descString));
                args.add(new BasicNameValuePair("image", imageString));

                JSONObject object = parser.makeHttpRequest(timetableUrl, "POST", args);

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddTimeTable.this, "Uploading successfully!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.timechooseimage) {
           final Dialog d = new Dialog(AddTimeTable.this);

            d.setContentView(R.layout.dialog);
            d.setTitle("Choose your option");
            Button camera = (Button) d.findViewById(R.id.camera);
            Button gallery = (Button) d.findViewById(R.id.gallery);

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    d.dismiss();
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            100);

                    d.dismiss();
                }
            });
            d.show();

        } else if (id == R.id.timesubmit) {

            ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()==true ) {
                new Timetableadding().execute();
            }else{
                Toast.makeText(AddTimeTable.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            tImageView.setImageBitmap(photo);
        }
        else if(requestCode==RESULT_LOAD_IMAGE&&resultCode==RESULT_OK)
        {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null,
                null);
        Cursor cursor =cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        photo = BitmapFactory.decodeFile(selectedImagePath, options);
            tImageView.setImageBitmap(photo);
        }
    }
}

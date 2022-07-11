package com.tracking.ebridge;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class AddQuestionpapers extends Activity implements View.OnClickListener{

    EditText qsEdit,qdEdit;
    Button qcButton,qsubmitButton;
    ImageView qImageView;
    private static final int CAMERA_REQUEST = 1888;

    ProgressDialog progressDialog;
    JSONParser parser=new JSONParser();
    JSONArray response;

    public String questionUrl="http://www.wikihands.com/ebridge/addquestionspapers.php";

    Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questionpapers);

        qsEdit= (EditText) findViewById(R.id.questionsubject);
        qdEdit= (EditText) findViewById(R.id.questiondescription);
        qcButton= (Button) findViewById(R.id.questionchooseimage);
        qImageView= (ImageView) findViewById(R.id.questionimageView);
        qsubmitButton= (Button) findViewById(R.id.questionsubmit);

        qcButton.setOnClickListener(this);
        qsubmitButton.setOnClickListener(this);
    }
    public class questionpapersadding extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(AddQuestionpapers.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String subjectString=qsEdit.getText().toString();
            String descString=qdEdit.getText().toString();
            String imageString=getStringImage(photo);

            try
            {
                List<NameValuePair> args=new ArrayList<>();
                args.add(new BasicNameValuePair("subject",subjectString));
                args.add(new BasicNameValuePair("description",descString));
                args.add(new BasicNameValuePair("image",imageString));

                JSONObject object=parser.makeHttpRequest(questionUrl,"POST",args);


            }catch (Exception e)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddQuestionpapers.this, "Uploading successfully!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.questionchooseimage)
        {
            final Dialog d = new Dialog(AddQuestionpapers.this);

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
        }
        else if(id==R.id.questionsubmit)
        {

            new questionpapersadding().execute();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            qImageView.setImageBitmap(photo);
        }
    }
}

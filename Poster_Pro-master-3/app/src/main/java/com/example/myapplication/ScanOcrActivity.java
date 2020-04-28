package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
//import com.google.firebase.ml.vision.text.RecognizedLanguage;

public class ScanOcrActivity extends AppCompatActivity implements View.OnClickListener {
private Uri uri;
private Bitmap photo;
private int choice=0;
private ImageButton ocrback,ocrstart;
private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ocr);
        iv=findViewById(R.id.ocr_iv);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("uri")) {
            uri= Uri.parse(extras.getString("uri"));
            iv.setImageURI(uri);
            choice=1;
        }else if(extras != null && extras.containsKey("BitmapImage")){
            photo=(Bitmap) extras.getParcelable("BitmapImage");
            choice=2;
            iv.setImageBitmap(photo);
        }
        ocrback=findViewById(R.id.ocr_back);
        ocrback.setOnClickListener(this);
        ocrstart=findViewById(R.id.ocr_start);
        ocrstart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ocr_back:
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.ocr_start:
                break;
            default:
                break;
        }
    }



}

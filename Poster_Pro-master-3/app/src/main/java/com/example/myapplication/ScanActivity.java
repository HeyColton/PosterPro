package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.ocr.TextRecognitionActivity;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ScanActivity";
    private ImageButton scancam, scanupload, scanback;
    private Uri pic;
    public static final int GET_FROM_GALLERY = 3;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scancam=findViewById(R.id.scan_cam);
        scanupload=findViewById(R.id.scanupload);
        scancam.setOnClickListener(this);
        scanupload.setOnClickListener(this);
        scanback=findViewById(R.id.scan_back);
        scanback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_back:
                Intent intent = new Intent(this.getApplication(), Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.scan_cam:
                dispatchTakePictureIntent();
                break;
            case R.id.scanupload:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                break;
            default:
                break;
        }
    }
    private void dispatchTakePictureIntent() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(this, TextRecognitionActivity.class);
            intent.putExtra("BitmapImage", photo);
            startActivity(intent);
        }

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Log.e("IMAGETT", String.valueOf(imageUri));

//            Bitmap bitmap = null;
//            try {
//                bitmap = ImageDecoder.decodeBitmap( ImageDecoder.createSource(this.getContentResolver(), imageUri));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Intent intent = new Intent(this, TextRecognitionActivity.class);
            intent.putExtra("uri", imageUri);
            startActivity(intent);


        }
    }

}

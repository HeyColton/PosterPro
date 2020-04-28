package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.graphics.ImageDecoder.decodeBitmap;

public class PostActivity extends AppCompatActivity implements OnClickListener {

    private EditText etposttitle;
    private EditText etpostlocation;
    private EditText etposttime;
    private EditText etpostdetails;
    private ImageButton btpostposter, btpostit, btpostback;
    private Bitmap bitmap;
    private ImageView ivposter;
    private SQLiteDatabase db;
    private String pic;
    public static final int GET_FROM_GALLERY = 3;
    private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        pic="";
        DatabaseHelper dbHelper = new DatabaseHelper(PostActivity.this, "test_db", null, 1);
        db = dbHelper.getWritableDatabase();

        initView();
    }

    private void initView() {

        etposttitle = findViewById(R.id.post_title);
        etpostlocation = findViewById(R.id.post_location);
        etposttime = findViewById(R.id.post_time);
        etpostdetails = findViewById(R.id.post_details);
        btpostposter = findViewById(R.id.post_addposter);
        ivposter=findViewById(R.id.post_poster);
        btpostposter.setOnClickListener(this);
        btpostit=findViewById(R.id.post_it);
        btpostit.setOnClickListener(this);
        btpostback=findViewById(R.id.post_back);
        btpostback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String title = etposttitle.getText().toString();
        String location = etpostlocation.getText().toString();
        String time = etposttime.getText().toString();
        String details = etpostdetails.getText().toString();
        switch (v.getId()) {

            case R.id.post_back:
                intent = new Intent(this.getApplication(), Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.post_addposter:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                break;
            case R.id.post_it:
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("location",location);
                values.put("time",time);
                values.put("content",details);
                if(title.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter title",Toast.LENGTH_SHORT).show();
                }else if(location.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter location",Toast.LENGTH_SHORT).show();
                }else if(time.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter time",Toast.LENGTH_SHORT).show();
                }else if(time.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter details",Toast.LENGTH_SHORT).show();
                }else {
                    if(bitmap!=null) {
                        pic = saveToInternalStorage(bitmap);
                    }
                    values.put("poster",pic);
                    long auid=db.insert("activity", null, values);
                    SharedPreferences preference = getSharedPreferences("CURRENT_UID",MODE_PRIVATE);
                    int uid = preference.getInt("uid",0);
                    if(uid!=0){
                        ContentValues postvalues=new ContentValues();
                        postvalues.put("puid",uid);
                        postvalues.put("auid",(int)auid);
                        db.insert("p_a_post", null, postvalues);
                        Log.e("test",String.valueOf(uid));
                        Log.e("test",String.valueOf(auid));
                    }
                    intent = new Intent(this.getApplication(), Main2Activity.class);
                    startActivity(intent);

                }
                break;
            default:
                break;
        }
    }
    @RequiresApi(api = 29)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

//            ivposter.setImageURI(selectedImage);
            try {
                bitmap = decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), selectedImage));
                ivposter.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String filename=UUID.randomUUID()+".jpg";
        File mypath=new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath()+"/"+filename;
    }
}

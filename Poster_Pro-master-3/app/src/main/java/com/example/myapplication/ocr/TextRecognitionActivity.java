package com.example.myapplication.ocr;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.Main2Activity;
import com.example.myapplication.R;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class TextRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri uri;
    private Bitmap photo;
    private int choice=0;
    private ImageButton ocrback,ocrstart;
    private ImageView iv;
    private TextView tv;
    private ArrayList<String> months=new ArrayList<>();
    String textall;
    private static String TAG = "IMAGETT";
    private static final String lang = "eng";
    private TessBaseAPI tessBaseApi;
    String result = "empty";
    private String DATA_PATH = "";
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        months.addAll(Arrays.asList("january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"));
        try {
            initview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void initview() throws IOException {
        setContentView(R.layout.activity_scan_ocr);
        getStorageAccessPermission();
        DATA_PATH=String.valueOf(getExternalFilesDir(null));
        copyAssets();
        textall="";
        iv=findViewById(R.id.ocr_iv);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("uri")) {
            Uri uri=(Uri) extras.getParcelable("uri");
            photo = ImageDecoder.decodeBitmap( ImageDecoder.createSource(this.getContentResolver(), uri));
            iv.setImageBitmap(photo);
            photo=photo.copy(Bitmap.Config.ARGB_8888,true);
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
                Log.e(TAG,DATA_PATH);
                  startOCR(photo);
                  parsecalendar(result);
                break;
            default:
                break;
        }
    }

    private void startOCR(Bitmap bitmap) {
        try {

            result = extractText(bitmap);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private String extractText(Bitmap bitmap) {
//        Log.e(TAG, bitmap.toString());
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }
//File file=new File(DATA_PATH+ "eng.traineddata");
//        Log.e(TAG,String.valueOf(file.exists()));
        tessBaseApi.init(DATA_PATH, lang);

//       //EXTRA SETTINGS
//        //For example if we only want to detect numbers
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
//
//        //blackList Example
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
//                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

        Log.d(TAG, "Training file loaded");
        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, "Error in recognizing text.");
        }
        tessBaseApi.end();
        Log.e(TAG, extractedText);
        return extractedText;
    }
    private void getStorageAccessPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(TextRecognitionActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(TextRecognitionActivity.this, "Request Permission！", Toast.LENGTH_LONG).show();
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(TextRecognitionActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            return;
        }


    }
    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            if(!filename.contains("eng.traineddata")){
                break;
            }
            Log.e(TAG,filename);
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                final File newFile = new File(getExternalFilesDir(null),"tessdata");
                newFile.mkdir();
                File outFile = new File(getExternalFilesDir(null), "tessdata/"+filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    public void parsecalendar(String text){
        String[] lines = text.split(System.getProperty("line.separator"));
        String title="";
        String location="";
        Calendar starttime=Calendar.getInstance();
        String description="";
        for (String line : lines){
            line=line.trim();
            if(title.isEmpty()&isUpperCase(line)){
                title=line;
            }else if(months.contains(line.toLowerCase().split(" ")[0])){

                try {
                    line = line.toLowerCase();
                    int month = months.indexOf(line.split(" ")[0]) + 1;
                    int day = new Scanner(line.split(" ")[1]).useDelimiter("\\D+").nextInt();
                    starttime.set(Calendar.MONTH, month);
                    starttime.set(Calendar.DATE, day);
                    if (line.split(" ").length > 2) {
                        int year = Integer.valueOf(line.split(" ")[2]);
                        starttime.set(Calendar.YEAR, year);
                    }
                }catch (Exception e){
                    starttime=Calendar.getInstance();
                }
            }else if(line.startsWith("20")){
                try {
                    int year = new Scanner(line.split(" ")[0]).useDelimiter("\\D+").nextInt();
                    starttime.set(Calendar.YEAR, year);
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }else if(line.toLowerCase().contains("center")|line.toLowerCase().contains("theater")|line.toLowerCase().contains("stadium")){
                location=line;
            }else{
                description+=line;
                description+="/n";
            }
        }
        calendar(title,description,location,starttime);
    }


    public static boolean isUpperCase(String s)
    {
        for (int i=0; i<s.length(); i++)
        {
            if ((!Character.isUpperCase(s.charAt(i)))&(' '!=(s.charAt(i))))
            {
                return false;
            }
        }
        return true;
    }

    public void calendar(String title, String description, String location, Calendar beginTime){
        Intent i = new Intent();

        // mimeType will popup the chooser any  for any implementing application (e.g. the built in calendar or applications such as "Business calendar"
        i.setType("vnd.android.cursor.item/event");

        // the time the event should start in millis. This example uses now as the start time and ends in 1 hour
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginTime.getTimeInMillis());
        i.putExtra(CalendarContract.Events.TITLE,title);
        i.putExtra(CalendarContract.Events.DESCRIPTION,description);
        i.putExtra(CalendarContract.Events.EVENT_LOCATION,location);

        // the action
        i.setAction(Intent.ACTION_EDIT);
        startActivity(i);
    }

}
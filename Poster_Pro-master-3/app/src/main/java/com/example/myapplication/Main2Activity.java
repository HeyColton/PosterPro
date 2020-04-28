package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.loga.LoginActivity;
import com.example.myapplication.loga.MainActivity;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<Event> eventArrayList;
    private ImageButton feedscan,feedpost,feedlog;
    private int uid;
    private Intent intent;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preference = getSharedPreferences("CURRENT_UID",MODE_PRIVATE);
        uid = preference.getInt("uid",0);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        feedscan=findViewById(R.id.feedscan);
        feedscan.setOnClickListener(this);
        feedlog=findViewById(R.id.feedlog);
        if(uid==0){
            feedlog.setImageResource(R.mipmap.ic_signin_foreground);
        }else{
            feedlog.setImageResource(R.mipmap.ic_profileimg);
        }
        feedlog.setOnClickListener(this);
        feedpost=findViewById(R.id.feedpost);
        feedpost.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        planetArrayList = new ArrayList<>();
        eventArrayList = new ArrayList<>();
        adapter = new CardAdapter(this, eventArrayList,uid);
        recyclerView.setAdapter(adapter);
        createListData();
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }

    private void createListData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this, "test_db", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from activity",null);
        int count=0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
            Event event=new Event(cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("location")),cursor.getString(cursor.getColumnIndex("time")),cursor.getString(cursor.getColumnIndex("content")), cursor.getString(cursor.getColumnIndex("poster")),cursor.getInt(cursor.getColumnIndex("auid")));
//            if(cursor.getString(cursor.getColumnIndex("title")).equals("")){
//                break;
//            }
            eventArrayList.add(event);
            count+=1;
            if(count==5){
                break;
            }
            cursor.moveToNext();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedlog:
                if(uid==0) {
                    intent = new Intent(this.getApplication(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(this.getApplication(), MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.feedpost:
                intent = new Intent(this.getApplication(), PostActivity.class);
                startActivity(intent);
                break;
            case R.id.feedscan:
                intent = new Intent(this.getApplication(), ScanActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
package com.example.myapplication.loga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Event;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ParticipatedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<Event> eventArrayList;
    TextView noItemText;
    private ImageButton paback;

    public Integer uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participated);

        paback = findViewById(R.id.paback);
        paback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences preference = getSharedPreferences("CURRENT_UID", MODE_PRIVATE);
        uid = preference.getInt("uid",0);

        recyclerView = findViewById(R.id.erv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventArrayList = new ArrayList<>();
        adapter = new MyAdapter(this, eventArrayList);
        recyclerView.setAdapter(adapter);
        createListData();

        //SharedPreferences sharedPref = getSharedPreferences("uid", Context.MODE_PRIVATE);
        //uid = sharedPref.getInt("uid",0);
    }



    private void createListData() {

        DatabaseHelper dbHelper = new DatabaseHelper(this, "test_db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "select * from p_a " + "where puid = " + uid;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Integer> list = new ArrayList<>();
        int auid = -1;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                auid = cursor.getInt(cursor.getColumnIndex("auid"));
                Log.e("test", "auid:" + String.valueOf(auid));
                list.add(auid);
                cursor.moveToNext();
            }
        }
        if(auid==-1){
            return;
        }
        eventArrayList = new ArrayList<>();
        for (int i : list) {
            Log.e("test",String.valueOf(i));
            String query1 = "select * from activity " + "where auid = " + i;
            Cursor cursor1 = db.rawQuery(query1, null);
            if (cursor1.moveToFirst()) {
                while (!cursor1.isAfterLast()) {
                    Event event = new Event(cursor1.getString(cursor1.getColumnIndex("title")), cursor1.getString(cursor1.getColumnIndex("location")), cursor1.getString(cursor1.getColumnIndex("time")), cursor1.getString(cursor1.getColumnIndex("content")), cursor1.getString(cursor1.getColumnIndex("poster")),cursor1.getInt(cursor1.getColumnIndex("poster")));

                    eventArrayList.add(event);
                    Log.e("test", String.valueOf(cursor1.getColumnIndex("puid")));
                    cursor1.moveToNext();
                }
            }
        }

        noItemText = findViewById(R.id.noItemText);
        if (eventArrayList.isEmpty()) {
            noItemText.setVisibility(View.VISIBLE);
        } else {
            noItemText.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyAdapter(this, eventArrayList);
            recyclerView.setAdapter(adapter);


        }


    }




}





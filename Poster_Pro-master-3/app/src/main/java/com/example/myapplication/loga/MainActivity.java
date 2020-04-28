package com.example.myapplication.loga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Main2Activity;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton backhome, modpi, posted, participated,log_out;

    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backhome = findViewById(R.id.back_home);
        modpi = findViewById(R.id.mod_pi);
        posted = findViewById(R.id.posted);
        participated = findViewById(R.id.participated);
        log_out=findViewById(R.id.log_out);
        final SharedPreferences preference = getSharedPreferences("CURRENT_UID",MODE_PRIVATE);
        uid = preference.getInt("uid",0);
        log_out.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                preference.edit().remove("uid").commit();
                Intent intent = new Intent(getApplication(), Main2Activity.class);
                startActivity(intent);
            }
        });

        backhome.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), Main2Activity.class);
                startActivity(intent);
            }
        });

        modpi.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), EditActivity.class);
                startActivity(intent);
            }
        });

        posted.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), PostedActivity.class);
                startActivity(intent);
            }
        });

        participated.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), ParticipatedActivity.class);
                startActivity(intent);
            }
        });
    }
}

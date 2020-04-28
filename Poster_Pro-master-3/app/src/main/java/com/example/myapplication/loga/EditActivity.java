package com.example.myapplication.loga;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;

public class EditActivity extends AppCompatActivity {
    public EditText n_name;
    public EditText n_email;
    public EditText n_password;
    public EditText n_organization;
    public ImageButton meditback;
    public ImageButton edit;
    public Integer uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        SharedPreferences preference = getSharedPreferences("CURRENT_UID", MODE_PRIVATE);
        uid = preference.getInt("uid",0);
        showData();
        ImageButton btn = (ImageButton)findViewById(R.id.edit);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        meditback=findViewById(R.id.editback);
        meditback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Info");
        builder.setMessage("Sure to modify?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int response=update();
                        if(response==1) {
                            Intent in = new Intent(EditActivity.this, MainActivity.class);
                            // in.putExtra("new_email",n_email.getText().toString());
                            startActivity(in);
                            finish();
                        }
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    private void showData(){
        n_name=(EditText)findViewById(R.id.editname);
        n_email=(EditText)findViewById(R.id.editemail);
        n_organization=(EditText)findViewById(R.id.editorganization);
        n_password=(EditText)findViewById(R.id.editpassword);

        DatabaseHelper helper = new DatabaseHelper(this,"test_db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from personal_info where puid=?" , new String[]{String.valueOf(uid)});
        if (cursor==null) {
        }else{
            while (cursor.moveToNext()) {
                n_name.setHint(cursor.getString(cursor.getColumnIndex("name")));
                n_email.setText(cursor.getString(cursor.getColumnIndex("email_address")));
                n_email.setEnabled(false);
                n_organization.setHint(cursor.getString(cursor.getColumnIndex("organization")));
                n_password.setHint(cursor.getString(cursor.getColumnIndex("password")));

            }
        }


    }

    private int update() {
        n_name=(EditText)findViewById(R.id.editname);
        n_organization=(EditText)findViewById(R.id.editorganization);
        n_password=(EditText)findViewById(R.id.editpassword);

        DatabaseHelper helper = new DatabaseHelper(EditActivity.this, "test_db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(!TextUtils.isEmpty(n_name.getText())){
        contentValues.put("name",n_name.getText().toString());}else{
            Toast.makeText(getApplicationContext(),"name cannot be empty!",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(!TextUtils.isEmpty(n_organization.getText())){
        contentValues.put("organization",n_organization.getText().toString());}else{
            Toast.makeText(getApplicationContext(),"organization cannot be empty!",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(!TextUtils.isEmpty(n_password.getText())){
        contentValues.put("password",n_password.getText().toString());}else{
            Toast.makeText(getApplicationContext(),"password cannot be empty!",Toast.LENGTH_SHORT).show();
            return 0;
        }
        db.update("personal_info", contentValues, "puid=?", new String[]{String.valueOf(uid)});
        db.close();
        return 1;

    }

}

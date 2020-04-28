package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists personal_info(puid integer primary key autoincrement,email_address text not null" +
                ",password text not null,name text not null,organization text not null,posting_permission text not null)");
        db.execSQL("create table if not exists activity(auid integer primary key autoincrement,title text not null" +
                ",content text not null,location text not null,time text not null,poster text not null)");
        db.execSQL("create table if not exists p_a(uid integer primary key autoincrement,puid integer not null" +
                ",auid integer not null)");
        db.execSQL("create table if not exists p_a_post(pid integer primary key autoincrement,puid integer not null" +
                ",auid integer not null)");
        db.execSQL("insert into personal_info values('1','233@233.com','password','name','org','per')");
        db.execSQL("insert into p_a values('1','1','1')");
        db.execSQL("insert into p_a_post values('1','1','1')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
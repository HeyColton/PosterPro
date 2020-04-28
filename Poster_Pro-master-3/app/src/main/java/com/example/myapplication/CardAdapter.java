package com.example.myapplication;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.EventHolder> {

    private Context context;
    private ArrayList<Event> events;
    private int uid;
    private SQLiteDatabase db;
    public CardAdapter(Context context, ArrayList<Event> events, int uid) {
        this.context = context;
        this.events = events;
        this.uid=uid;
        DatabaseHelper dbHelper = new DatabaseHelper(context, "test_db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event event = events.get(position);
        holder.setDetails(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventHolder extends RecyclerView.ViewHolder {
        private ImageView cardimg;
        private TextView cardtime, cardlocation;
        private ImageButton cardjoin;

        EventHolder(View itemView) {
            super(itemView);
            cardimg = itemView.findViewById(R.id.cardimg);
            cardtime = itemView.findViewById(R.id.cardtime);
            cardlocation = itemView.findViewById(R.id.cardlocation);
            cardjoin = itemView.findViewById(R.id.cardjoin);
        }

        void setDetails(final Event event) {
            if(!event.getPoster().equals("")) {
               loadImageFromStorage(event.getPoster());
            }
            cardtime.setText(String.format(Locale.US, " %s ", event.getTime()));
            cardlocation.setText(String.format(Locale.US, " %s ", event.getLocation()));
            cardjoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(uid==0){
                        Toast.makeText(context,"Please Log in",Toast.LENGTH_SHORT).show();
                    }else {
                        db.execSQL("insert into p_a(puid,auid)" +
                                "values('" + uid + "'," +
                                "'" + event.getAuid() + "')");
                        Log.e("test2",String.valueOf(uid));
                        Log.e("test2",String.valueOf(event.getAuid()));
                        Toast.makeText(context, "Join Sucess!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        private void loadImageFromStorage(String path)
        {

            try {
                File f=new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                cardimg.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }
    }
}
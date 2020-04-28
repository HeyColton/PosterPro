package com.example.myapplication.loga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Event;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Event> events;



    MyAdapter(Context context, List<Event> events){
        this.inflater = LayoutInflater.from(context);
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.event_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String title = String.format(Locale.US, "Title: %s ",events.get(i).getTitle());
        String content = String.format(Locale.US, "Description: %s ",events.get(i).getContent());
        String time = String.format(Locale.US, "Time: %s ",events.get(i).getTime());
        String location = String.format(Locale.US, "Location: %s ",events.get(i).getLocation());
        if(!events.get(i).getPoster().equals("")) {
            viewHolder.poster.setImageBitmap(loadImageFromStorage(events.get(i).getPoster()));
        }

        viewHolder.title.setText(title);
        viewHolder.content.setText(content);
        viewHolder.location.setText(location);
        viewHolder.time.setText(time);


    }
    private Bitmap loadImageFromStorage(String path)
    {

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,time,location,content;
        ImageView poster;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            content = itemView.findViewById(R.id.content);
           poster=itemView.findViewById(R.id.mainposter);


        }
    }
}
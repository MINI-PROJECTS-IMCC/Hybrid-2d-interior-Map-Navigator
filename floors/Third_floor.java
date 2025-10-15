package com.example.proto2.floors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proto2.Main_Building;
import com.example.proto2.maplogic.MapOverlayView;
import com.example.proto2.maplogic.MapPoint;
import com.example.proto2.R;

import java.util.ArrayList;
import java.util.List;

public class Third_floor extends AppCompatActivity {
    MapOverlayView overlayView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.thirdfloor);
        Button back=findViewById(R.id.mg3);
        overlayView = findViewById(R.id.overlayView);
        List<MapPoint> points = new ArrayList<>();
        points.add(new MapPoint("A", 0.12f, 0.49f));
        points.add(new MapPoint("B", 0.53f, 0.49f));
        points.add(new MapPoint("C", 0.53f, 0.31f));
        points.add(new MapPoint("D", 0.3f, 0.31f));
        points.add(new MapPoint("E", 0.08f, 0.31f));
        points.add(new MapPoint("F", 0.66f, 0.33f));
        points.add(new MapPoint("G", 0.66f, 0.37f));
        points.add(new MapPoint("H", 0.53f, 0.69f));
        points.add(new MapPoint("I", 0.45f, 0.69f));
        points.add(new MapPoint("J", 0.45f, 0.73f));
        points.add(new MapPoint("K", 0.53f, 0.77f));
        points.add(new MapPoint("L", 0.45f, 0.86f));
        points.add(new MapPoint("M", 0.53f, 0.73f));
        overlayView.setPoints(points);
        back.setOnClickListener(v->{
            Intent intent=new Intent(Third_floor.this, Main_Building.class);
            startActivity(intent);
        });
    }
}

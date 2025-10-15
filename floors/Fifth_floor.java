package com.example.proto2.floors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proto2.Main_Building;
import com.example.proto2.maplogic.MapOverlayView;
import com.example.proto2.maplogic.MapPoint;
import com.example.proto2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fifth_floor extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        MapOverlayView overlayView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fifthfloor);
        overlayView = findViewById(R.id.overlayView1);
        List<MapPoint> points = new ArrayList<>();
        points.add(new MapPoint("A", 0.12f, 0.49f));

        points.add(new MapPoint("C", 0.53f, 0.49f));
        points.add(new MapPoint("D", 0.53f, 0.32f));
        points.add(new MapPoint("E", 0.3f, 0.31f));
        points.add(new MapPoint("F", 0.1f, 0.31f));
        points.add(new MapPoint("G", 0.53f, 0.25f));
        points.add(new MapPoint("H", 0.53f, 0.69f));
        points.add(new MapPoint("J", 0.45f, 0.69f));
        points.add(new MapPoint("K", 0.45f, 0.73f));
        points.add(new MapPoint("L", 0.53f, 0.77f));
        points.add(new MapPoint("M", 0.45f, 0.86f));
        points.add(new MapPoint("N", 0.53f, 0.73f));


        Map<String, List<String>> paths = new HashMap<>();
        paths.put("A", List.of("C"));
        paths.put("C", List.of("D","A","H"));
        paths.put("D", List.of( "C","E","F","G"));
        paths.put("E", List.of("D"));
        paths.put("F",List.of("E","D"));
        paths.put("G",List.of("D"));
        paths.put("H",List.of("C","J","N"));
        paths.put("J",List.of("H","N"));
        paths.put("K",List.of("H","J","N"));
        paths.put("N",List.of("H","J","K","L","M"));
        paths.put("L",List.of("N","M"));
        paths.put("M",List.of("L","N"));
        overlayView.setPoints(points);
        overlayView.setPaths(paths);
        Button back=findViewById(R.id.mg5);
        back.setOnClickListener(v->{
            Intent intent=new Intent(Fifth_floor.this, Main_Building.class);
            startActivity(intent);
        });
        Button reset=findViewById(R.id.reset5);
        reset.setOnClickListener(v->{
            overlayView.resetPath();
        });
    }
}

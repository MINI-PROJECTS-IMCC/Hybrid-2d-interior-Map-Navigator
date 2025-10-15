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

public class fourth_floor extends AppCompatActivity {
    MapOverlayView overlayView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourthfloor);
        overlayView = findViewById(R.id.overlayView);

        // Define points (ratios instead of pixels)
        List<MapPoint> points = new ArrayList<>();
        points.add(new MapPoint("A", 0.1f, 0.48f));

        points.add(new MapPoint("C", 0.5f, 0.48f));
        points.add(new MapPoint("D", 0.5f, 0.31f));
        points.add(new MapPoint("E", 0.29f, 0.31f));
        points.add(new MapPoint("F", 0.1f, 0.31f));
        points.add(new MapPoint("G", 0.5f, 0.255f));
        points.add(new MapPoint("H", 0.5f, 0.58f));
        points.add(new MapPoint("I", 0.42f, 0.614f));
        points.add(new MapPoint("J", 0.42f, 0.682f));
        points.add(new MapPoint("K", 0.38f, 0.73f));
        points.add(new MapPoint("L", 0.5f, 0.86f));
        points.add(new MapPoint("M", 0.5f, 0.91f));
        points.add(new MapPoint("N", 0.5f, 0.73f));

        // Define paths (graph connections)
        Map<String, List<String>> paths = new HashMap<>();
        paths.put("A", List.of("C"));
        paths.put("C", List.of("D","A","H"));
        paths.put("D", List.of( "C","E","F","G"));
        paths.put("E", List.of("D"));
        paths.put("F",List.of("E","D"));
        paths.put("G",List.of("D"));
        paths.put("H",List.of("C","I","J","K","N"));
        paths.put("I",List.of("H","N"));
        paths.put("J",List.of("I","H","N"));
        paths.put("K",List.of("I","H","J","N"));
        paths.put("N",List.of("I","H","J","K","L","M"));
        paths.put("L",List.of("N","M"));
        paths.put("M",List.of("L","N"));

        overlayView.setPoints(points);
        overlayView.setPaths(paths);
        Button reset=findViewById(R.id.reset);
        reset.setOnClickListener(v->{
            overlayView.resetPath();
        });
        Button back=findViewById(R.id.mg4);
        back.setOnClickListener(v->{
            Intent intent=new Intent(fourth_floor.this, Main_Building.class);
            startActivity(intent);
        });
    }
}

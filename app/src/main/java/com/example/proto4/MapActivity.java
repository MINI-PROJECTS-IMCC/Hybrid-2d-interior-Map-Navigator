package com.example.proto4;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proto4.maplogic.MapOverlayView;

import java.io.IOException;
import java.io.InputStream;

public class MapActivity extends AppCompatActivity {
    private ImageView floorMap;
    private MapOverlayView mapOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        floorMap =findViewById(R.id.imageview);
        mapOverlay=findViewById(R.id.overlayView1);
        String floorName = getIntent().getStringExtra("FLOOR_NAME");
        if (floorName != null) {
            loadFloor(floorName);
        }
        Button reset=findViewById(R.id.resetbutton);
        reset.setOnClickListener(v->mapOverlay.resetPath());
        Button bck=findViewById(R.id.back);
        bck.setOnClickListener(v->{
            Intent intent=new Intent(MapActivity.this, building.class);
            startActivity(intent);
        });
        Button btnSelectFloor = findViewById(R.id.floor_select);
        String[] floors = { "1st Floor", "2nd Floor","3rd floor","4th floor","5th floor"};
        btnSelectFloor.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Floor");

            builder.setItems(floors, (dialog, which) -> {
                String selectedFloor = floors[which];
                btnSelectFloor.setText(selectedFloor);

                switch (which) {
                    case 0:
                        loadFloor("floor_1");

                        break;
                    case 1:
                        loadFloor("floor_2");

                        break;
                    case 2:
                        loadFloor("floor_3");

                        break;
                    case 3:
                        loadFloor("floor_4");

                        break;
                    case 4:
                        loadFloor("IMCC_M_floor5");

                        break;
                }
            });
            builder.show();
        });
    }
    public void loadFloor(String folderName) {
        try {
            // Load map image
            AssetManager am = getAssets();
            InputStream is = am.open(folderName + "/map.png");
            Drawable d = Drawable.createFromStream(is, null);
            floorMap.setImageDrawable(d);

            mapOverlay.setPoints(folderName + "/datapoints.xml");
            mapOverlay.loadGraphFromXml(folderName+"/datapoints.xml");
            mapOverlay.setTextPoints(folderName+"/datapoints.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

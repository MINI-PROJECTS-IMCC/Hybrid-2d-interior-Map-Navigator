package com.example.proto4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class building extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building);
        Button bt1=findViewById(R.id.floor_1);
        bt1.setOnClickListener(v->openMap("floor_1"));
        Button bt2=findViewById(R.id.floor_2);
        bt2.setOnClickListener(v->openMap("floor_2"));
        Button bt3=findViewById(R.id.floor_3);
        bt3.setOnClickListener(v->openMap("floor_3"));
        Button bt4=findViewById(R.id.floor_4);
        bt4.setOnClickListener(v->openMap("floor_4"));
        Button bt5=findViewById(R.id.floor_5);
        bt5.setOnClickListener(v->openMap("IMCC_M_floor5"));

    }
    protected void openMap(String floorName) {
        Intent intent = new Intent(building.this, MapActivity.class);
        intent.putExtra("FLOOR_NAME", floorName);  // pass which floor to load
        startActivity(intent);
    }


}

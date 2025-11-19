package com.example.proto4.qr;

import android.content.Context;
import android.content.Intent;
import com.example.proto4.MapActivity;

public class Verify {
    String qr;
    Context context;
    public Verify(Context context)
    {
        this.context=context;
    }
    public void checkQr(String qrValue){
        if (qrValue.equals("IMCC_M_floor5")) {
            Intent i = new Intent(context, MapActivity.class);
            i.putExtra("FLOOR_NAME", qrValue);
            context.startActivity(i);
        }
    }


}

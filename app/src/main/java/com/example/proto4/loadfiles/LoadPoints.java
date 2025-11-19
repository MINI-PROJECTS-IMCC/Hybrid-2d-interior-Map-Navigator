package com.example.proto4.loadfiles;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.content.Context;

import android.view.View;
import com.example.proto4.maplogic.MapPoint;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class LoadPoints extends View {
    public LoadPoints(Context context, AttributeSet attrs){
        super(context,attrs);

    }
    InputStream is;
    private ArrayList<MapPoint> points =new ArrayList<>();
    public ArrayList<MapPoint> loadPointsFromXml(String assetPath) {
        try {
            is = getContext().getAssets().open(assetPath);
            XmlPullParser parser= Xml.newPullParser();
            parser.setInput(is,null);

            int eventType=parser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_TAG && parser.getName().equals("point"))
                {
                    String id = parser.getAttributeValue(null, "id");
                    float x = Float.parseFloat(parser.getAttributeValue(null, "x"));
                    float y = Float.parseFloat(parser.getAttributeValue(null, "y"));
                    boolean clickable = Boolean.parseBoolean(parser.getAttributeValue(null, "clickable"));
                    points.add(new MapPoint(id,x,y,clickable));
                }
                eventType=parser.next();
            }
            is.close();
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("loadpoints error","not able to load file");
        }
        return points;
    }
}

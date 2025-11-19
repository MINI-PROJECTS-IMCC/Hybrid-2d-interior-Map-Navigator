package com.example.proto4.loadfiles;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import com.example.proto4.maplogic.MapPoint;

import org.xmlpull.v1.XmlPullParser;

import java.net.ConnectException;
import java.io.InputStream;
import java.util.ArrayList;
public class LoadTextPoints extends View{
    InputStream is;
    private ArrayList<MapPoint> textPoints=new ArrayList<>();
    public LoadTextPoints(Context context, AttributeSet attrs)
    {
        super(context,attrs);

    }
    public ArrayList<MapPoint> LoadTextpointsXml(String path){
        try{
            is=getContext().getAssets().open(path);
            XmlPullParser parser= Xml.newPullParser();
            parser.setInput(is,null);

            int eventType=parser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT)
            {
                if(eventType==XmlPullParser.START_TAG && parser.getName().equals("textpoint")){
                    String id= parser.getAttributeValue(null,"id");
                    float x= Float.parseFloat(parser.getAttributeValue(null,"x"));
                    float y=Float.parseFloat(parser.getAttributeValue(null,"y"));
                    boolean clickable=Boolean.parseBoolean(parser.getAttributeValue(null,"clickable"));
                    textPoints.add(new MapPoint(id,x,y,clickable));
                }
                eventType=parser.next();

            }
            is.close();
            invalidate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return textPoints;
    }


}

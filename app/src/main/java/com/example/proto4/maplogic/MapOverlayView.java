package com.example.proto4.maplogic;
import com.example.proto4.loadfiles.LoadPoints;
import com.example.proto4.loadfiles.LoadTextPoints;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import org.xmlpull.v1.XmlPullParser;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
public class MapOverlayView extends View{
    private List<MapPoint> points = new ArrayList<>();
    private Map<String, List<String>> paths = new HashMap<>();
    private List<MapPoint> selectedPoints = new ArrayList<>();
    private List<MapPoint> selectedPoints2 = new ArrayList<>();
    private List<MapPoint> tempPoints= new ArrayList<>();
    private List<MapPoint> textPoints = new ArrayList<>();
    private List<MapPoint> pathPoints = new ArrayList<>();
    private Paint paintPoint,paintPoint2;
    private Paint paintPath;
    InputStream is;
    private LoadPoints ld;
    private LoadTextPoints ld2;
    public MapOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ld=new LoadPoints(context,attrs);
        ld2=new LoadTextPoints(context,attrs);
        paintPoint = new Paint();
        paintPoint.setColor(Color.BLUE);
        paintPoint.setStyle(Paint.Style.FILL);

        paintPoint2=new Paint();
        paintPoint2.setColor(Color.RED);
        paintPoint2.setStyle(Paint.Style.FILL);

        paintPath = new Paint();
        paintPath.setColor(Color.BLUE);
        paintPath.setStrokeWidth(8f);
        paintPath.setStyle(Paint.Style.STROKE);

    }
    public Map<String, List<String>> loadGraphFromXml(String xmlFilePath) {
        try {
            is = getContext().getAssets().open(xmlFilePath);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, null);

            String currentNodeId = null;
            List<String> connections = null;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();

                    if (name.equals("node")) {
                        currentNodeId = parser.getAttributeValue(null, "id");
                        connections = new ArrayList<>();

                    } else if (name.equals("connectsTo") && currentNodeId != null) {
                        connections.add(parser.nextText());
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("node") && currentNodeId != null) {
                        paths.put(currentNodeId, connections);
                        currentNodeId = null;
                    }
                }

                eventType = parser.next();
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paths;
    }
    public void setPoints(String path){
        points=ld.loadPointsFromXml(path);
    }
    public void setTextPoints(String path){
        textPoints=ld2.LoadTextpointsXml(path);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw all points
        for (MapPoint p : points) {
            float x = p.getxRatio() * getWidth();
            float y = p.getyRatio() * getHeight();
            canvas.drawCircle(x,y,15f,paintPoint);
        }
        for (MapPoint p : textPoints) {
            float x = p.getxRatio() * getWidth();
            float y = p.getyRatio() * getHeight();
            canvas.drawCircle(x,y,15f,paintPoint2);
        }
        // Draw current path
        for (int i = 0; i < pathPoints.size() - 1; i++) {
            MapPoint p1 = pathPoints.get(i);
            MapPoint p2 = pathPoints.get(i + 1);
            canvas.drawLine(
                    p1.getxRatio() * getWidth(),
                    p1.getyRatio() * getHeight(),
                    p2.getxRatio() * getWidth(),
                    p2.getyRatio() * getHeight(),
                    paintPath
            );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xRatio = event.getX() / getWidth();
            float yRatio = event.getY() / getHeight();

            MapPoint nearest = null;
            MapPoint nearest2 = null;
            double minDist = Double.MAX_VALUE;
            double minDist2 = Double.MAX_VALUE;

            for (MapPoint p : points) {
                if(p.clickable==true)
                {
                    double dx = p.getxRatio() - xRatio;
                    double dy = p.getyRatio() - yRatio;
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = p;
                    }
                }

            }
            for (MapPoint p : textPoints) {
                double dx2 = p.getxRatio() - xRatio;
                double dy2 = p.getyRatio() - yRatio;
                double dist = Math.sqrt(dx2 * dx2 + dy2 * dy2);
                if (dist < minDist2) {
                    minDist2 = dist;
                    nearest2 = p;
                }
            }
            // If tap is close enough to a point, select it
            if(nearest2!=null&&minDist2<0.12){
                selectedPoints2.add(nearest);
                invalidate();
            }
            else if (nearest != null && minDist < 0.07) { // Adjust sensitivity
                selectedPoints.add(nearest);
                invalidate();
            }
            if (selectedPoints2.size() == 2) {
                int n=0;
                while(n<2)
                {
                    String a=selectedPoints2.get(n).getId();
                    for(MapPoint p:points)
                    {
                        if(p.getId()==a)
                        {
                            tempPoints.add(p);
                            break;
                        }
                    }
                    n++;
                }
                pathPoints=findPath(tempPoints.get(0),tempPoints.get(1));
            }
            if(selectedPoints2.size()==3)
            {
                selectedPoints2.clear();
                tempPoints.clear();
            }
            if (selectedPoints.size() == 2) {
                pathPoints = findPath(selectedPoints.get(0), selectedPoints.get(1));
            }
            if(selectedPoints.size()==3)
            {
                selectedPoints.clear();
            }
            if(selectedPoints.size()==1&&selectedPoints2.size()==1)
            {
                String a=selectedPoints2.get(0).getId();
                for(MapPoint p:points)
                {
                    if(p.getId()==a)
                    {
                        tempPoints.add(p);
                        break;
                    }
                }
                pathPoints = findPath(selectedPoints.get(0), tempPoints.get(0));
            }
        }
        return true;
    }
    private List<MapPoint> findPath(MapPoint start, MapPoint end) {
        // Simple BFS pathfinding through the predefined graph
        Map<String, String> parent = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start.getId());
        parent.put(start.getId(), null);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end.getId())) break;
            List<String> neighbors = paths.get(current);
            if (neighbors == null) continue;
            for (String n : neighbors) {
                if (!parent.containsKey(n)) {
                    parent.put(n, current);
                    queue.add(n);
                }
            }
        }
        // Reconstruct path
        List<MapPoint> result = new ArrayList<>();
        String curr = end.getId();
        while (curr != null) {
            MapPoint p = findPointById(curr);
            if (p != null) result.add(0, p);
            curr = parent.get(curr);
        }
        return result;
    }
    private MapPoint findPointById(String id) {
        for (MapPoint p : points) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
    public void resetPath() {
        pathPoints.clear();      // clear the drawn path
        selectedPoints.clear();
        selectedPoints2.clear();
        tempPoints.clear();// clear selected points too (optional)
        invalidate();            // redraw without path
    }

}

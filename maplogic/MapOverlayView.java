package com.example.proto2.maplogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.proto2.maplogic.MapPoint;

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
    private List<MapPoint> pathPoints = new ArrayList<>();

    private Paint paintPoint,paintSelected;
    private Paint paintPath;

    public MapOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintPoint = new Paint();
        paintPoint.setColor(Color.BLUE);
        paintPoint.setStyle(Paint.Style.FILL);

        paintPath = new Paint();
        paintPath.setColor(Color.BLUE);
        paintPath.setStrokeWidth(8f);
        paintPath.setStyle(Paint.Style.STROKE);

    }

    public void setPoints(List<MapPoint> points) {
        this.points = points;
        invalidate();
    }

    public void setPaths(Map<String, List<String>> paths) {
        this.paths = paths;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw all points
        for (MapPoint p : selectedPoints) {
            float x = p.getxRatio() * getWidth();
            float y = p.getyRatio() * getHeight();
            canvas.drawCircle(x,y,15f,paintPoint);
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
            double minDist = Double.MAX_VALUE;

            for (MapPoint p : points) {
                double dx = p.getxRatio() - xRatio;
                double dy = p.getyRatio() - yRatio;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = p;
                }
            }

            // If tap is close enough to a point, select it
            if (nearest != null && minDist < 0.05) { // Adjust sensitivity
                selectedPoints.add(nearest);
                if (selectedPoints.size() == 2) {
                    pathPoints = findPath(selectedPoints.get(0), selectedPoints.get(1));
                    //selectedPoints.clear();
                }
                invalidate();
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
        selectedPoints.clear();  // clear selected points too (optional)
        invalidate();            // redraw without path
    }


}

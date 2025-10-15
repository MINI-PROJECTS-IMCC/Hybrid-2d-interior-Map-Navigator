package com.example.proto2.maplogic;

public class MapPoint {
    private String id;
    private float xRatio;
    private float yRatio;

    public MapPoint(String id, float xRatio, float yRatio) {
        this.id = id;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public String getId() { return id; }
    public float getxRatio() { return xRatio; }
    public float getyRatio() { return yRatio; }
}

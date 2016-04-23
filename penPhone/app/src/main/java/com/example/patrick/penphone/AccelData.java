package com.example.patrick.penphone;

/**
 * Created by Patrick on 4/21/16.
 */
public class AccelData {
    private long timestamp;
    private double x;
    private double y;
    private double z;

    public AccelData(long timestamp, double x, double y, double z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public AccelData() {
        this.timestamp = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public String toString()
    {
        return timestamp+", "+x+", "+y+", "+z;
    }
}
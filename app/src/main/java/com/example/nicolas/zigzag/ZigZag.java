package com.example.nicolas.zigzag;

import android.content.Context;
import android.util.Log;
import android.view.View;

/**
 * Created by Nicolas on 25/10/2016.
 */

public class ZigZag {
    private int height;
    private int y;
    private int x;
    private int sens;

    public ZigZag(int x, int y, int height, int sens) {
        this.height = height;
        this.sens = sens;
        this.x = x;
        this.y = y;
    }



    public int getSens() {
        return sens;
    }

    public int getHeight() {
        return height;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "ZigZag{" +
                "height=" + height +
                ", y=" + y +
                ", x=" + x +
                ", sens=" + sens +
                '}';
    }
}

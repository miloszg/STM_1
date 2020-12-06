package pl.milosz.pong.gameObjects;

import android.graphics.Paint;

public class Ball {


    public float cx;
    public float cy;
    public float dx;
    public float dy;
    public int radius;
    public Paint paint;

    public Ball(int radius, Paint paint) {
        this.radius = radius;
        this.paint = paint;
    }

}

package pl.milosz.pong.gameObjects;

import android.graphics.Paint;

public class Ball {
    public float cx;
    public float cy;
    public float dx;
    public float dy;
    public int r;
    public Paint paint;

    public Ball(int radius, Paint paint) {
        this.r = radius;
        this.paint = paint;
    }

}

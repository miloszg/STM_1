package pl.milosz.pong.gameObjects;

import android.graphics.Paint;
import android.graphics.RectF;

public class Pad {

    public int paddleWidth;
    public int paddleHeight;
    public RectF size;
    public Paint paint;
    public int score;
    public int collision;

    public Pad(int paddleWidth, int paddleHeight, Paint paint) {
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.size = new RectF(0, 0, paddleWidth, paddleHeight);
        this.paint = paint;
        this.score = 0;
        this.collision = 0;
    }

}
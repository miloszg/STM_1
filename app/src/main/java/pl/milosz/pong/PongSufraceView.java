package pl.milosz.pong;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static pl.milosz.pong.PongLogic.canvasHeight;
import static pl.milosz.pong.PongLogic.canvasWidth;

public class PongSufraceView extends SurfaceView implements SurfaceHolder.Callback {
    private PongLogic pongLogic;
    private float lastPlayer1 = 0;
    private float lastPlayer2 = 0;

    private int player1Pointer;
    private int player2Pointer;

    public PongSufraceView(Context context, AttributeSet attr) {
        super(context, attr);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        pongLogic = new PongLogic(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        pongLogic.canvasWidth = width;
        pongLogic.canvasHeight = height;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        pongLogic.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int activePointer = event.getActionIndex();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!pongLogic.runBoolean) {
                    pongLogic.runBoolean = true;
                } else {
                    if (event.getX(activePointer) < canvasHeight && event.getY(activePointer) < canvasWidth / 2) {
                        player1Pointer = event.getPointerId(activePointer);
                        lastPlayer1 = event.getY(activePointer);
                    } else {
                        player2Pointer = event.getPointerId(activePointer);
                        lastPlayer2 = event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pongLogic.runBoolean) {
                    if (event.getX(player1Pointer) < canvasHeight && event.getY(player1Pointer) < canvasWidth / 2) {
                        pongLogic.movePlayer1(event.getY(player1Pointer) - lastPlayer1);
                        lastPlayer1 = event.getY();
                    } else {
                        pongLogic.movePlayer2(event.getY(player2Pointer) - lastPlayer2);
                        lastPlayer2 = event.getY();
                    }
                }
                break;
        }
        return true;
    }
}
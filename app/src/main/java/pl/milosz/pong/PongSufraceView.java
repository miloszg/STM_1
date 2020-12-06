package pl.milosz.pong;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class PongSufraceView extends SurfaceView implements SurfaceHolder.Callback {
    private PongLogic pongLogic;
    private TextView scoreView;
    private boolean moving;
    private float lastPlayer1;
    private float lastPlayer2;

    public PongSufraceView(Context context, AttributeSet attr) {
        super(context, attr);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        pongLogic = new PongLogic(holder,
                new Handler() {
                    @Override
                    public void handleMessage(Message m) {
                        scoreView.setText(m.getData().getString("score"));
                    }
                }
        );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        pongLogic.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        pongLogic.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!pongLogic.runBoolean) {
                    pongLogic.runBoolean = true;
                } else {
                    if (pongLogic.Player1Touch(event)) {
                        moving = true;
                        lastPlayer1 = event.getY();
                    } else if (pongLogic.Player2Touch(event)) {
                        moving = true;
                        lastPlayer2 = event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (moving) {
                    if (pongLogic.Player1Touch(event)) {
                        pongLogic.movePlayer1(event.getY() - lastPlayer1);
                        lastPlayer1=event.getY();
                    }

                    if (pongLogic.Player2Touch(event)) {
                        pongLogic.movePlayer2(event.getY() - lastPlayer2);
                        lastPlayer2=event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                moving = false;
                break;
        }
        return true;
    }

    public void setScoreView(TextView textView) {
        scoreView = textView;
    }
}
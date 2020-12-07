package pl.milosz.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import pl.milosz.pong.gameObjects.Ball;
import pl.milosz.pong.gameObjects.Pad;

public class PongLogic extends Thread {
    public static boolean runBoolean = true;
    private Pad player1;
    private Pad player2;
    private Ball ball;
    private Paint layoutPaint;
    public static int canvasHeight;
    public static int canvasWidth;
    private final SurfaceHolder surface;
    private final Handler scoreHandler;

    public PongLogic(final SurfaceHolder surfaceHolder,
                     final Handler scoreHandler) {
        surface = surfaceHolder;
        this.scoreHandler = scoreHandler;

        int paddleHeight = 350;
        int paddleWidth = 125;
        int ballRadius = 25;

        // player 1
        Paint player1Paint = new Paint();
        player1Paint.setAntiAlias(true);
        player1Paint.setColor(Color.WHITE);
        player1 = new Pad(paddleWidth, paddleHeight, player1Paint);

        // player 2
        Paint player2Paint = new Paint();
        player2Paint.setAntiAlias(true);
        player2Paint.setColor(Color.WHITE);
        player2 = new Pad(paddleWidth, paddleHeight, player2Paint);

        // ball
        Paint ballPaint = new Paint();
        ballPaint.setAntiAlias(true);
        ballPaint.setColor(Color.WHITE);
        ball = new Ball(ballRadius, ballPaint);

        // layout
        layoutPaint = new Paint();
        layoutPaint.setAntiAlias(true);
        layoutPaint.setColor(Color.WHITE);
        layoutPaint.setStyle(Paint.Style.STROKE);
        layoutPaint.setStrokeWidth(2.0f);
    }

    private void drawLayout(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, layoutPaint);
        canvas.drawLine(canvasWidth / 2, 1, canvasWidth / 2, canvasHeight, layoutPaint);
        setScoreText(player1.score + "    " + player2.score);

        canvas.drawRoundRect(player1.size, 5, 5, player1.paint);
        canvas.drawRoundRect(player2.size, 5, 5, player2.paint);
        canvas.drawCircle(ball.cx, ball.cy, ball.radius, ball.paint);
    }

    private void reset() {
        ball.cx = canvasWidth / 2;
        ball.cy = canvasHeight / 2;
        ball.dx = -10;
        ball.dy = 0;
        movePlayer(player1, 2, (canvasHeight - player1.paddleHeight) / 2);
        movePlayer(player2, canvasWidth - player2.paddleWidth - 2, (canvasHeight - player2.paddleHeight) / 2);
    }

    @Override
    public void run() {
        reset();
        while (runBoolean) {
            Canvas canvas = null;
            try {
                canvas = surface.lockCanvas(null);
                if (canvas != null) {
                    updatePlayers();
                    drawLayout(canvas);
                }
            } catch (Exception e) {
                Log.i("Error", e.getMessage());
            } finally {
                if (canvas != null) {
                    surface.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void movePlayer(Pad player, float left, float top) {
        if (left < 2) {
            left = 2;
        } else if (left + player.paddleWidth >= canvasWidth - 2) {
            left = canvasWidth - player.paddleWidth - 2;
        }
        if (top < 0) {
            top = 0;
        } else if (top + player.paddleHeight >= canvasHeight) {
            top = canvasHeight - player.paddleHeight - 1;
        }
        player.size.offsetTo(left, top);
    }

    public void movePlayer1(float dy) {
        movePlayer(player1, player1.size.left, player1.size.top + dy);
    }

    public void movePlayer2(float dy) {
        movePlayer(player2, player2.size.left, player2.size.top + dy);
    }

    private void updatePlayers() {
        if (player1.collision > 0) {
            player1.collision--;
        }
        if (player2.collision > 0) {
            player2.collision--;
        }

        // check collision
        if (collision(player1, ball)) {
            handleCollision(player1, ball);
            player1.collision = 5;
        } else if (collision(player2, ball)) {
            handleCollision(player2, ball);
            player2.collision = 5;
        } else if (ball.cy <= ball.radius
                || ball.cy + ball.radius >= canvasHeight - 1) {
            ball.dy = -ball.dy;
        } else if (ball.cx + ball.radius >= canvasWidth - 1) {
            player1.score++;
            reset();
            return;
        } else if (ball.cx <= ball.radius) {
            player2.score++;
            reset();
            return;
        }

        // ball
        ball.cx += ball.dx;
        ball.cy += ball.dy;

        if (ball.cy < ball.radius) {
            ball.cy = ball.radius;
        } else if (ball.cy + ball.radius >= canvasHeight) {
            ball.cy = canvasHeight - ball.radius - 1;
        }
    }

    private boolean collision(Pad player, Ball ball) {
        return player.size.intersects(
                ball.cx - this.ball.radius,
                ball.cy - this.ball.radius,
                ball.cx + this.ball.radius,
                ball.cy + this.ball.radius);
    }

    private void handleCollision(Pad player, Ball ball) {
        float relative = player.size.top + player.paddleHeight / 2 - ball.cy;
        float normalized = relative / (player.paddleHeight / 2);
        double bounceAngle = normalized * 5 * Math.PI / 12;

        ball.dx = (float) (-Math.signum(ball.dx) * 10 * Math.cos(bounceAngle));
        ball.dy = (float) (10 * -Math.sin(bounceAngle));

        if (player == player1) {
            this.ball.cx = player1.size.right + this.ball.radius;
        } else {
            this.ball.cx = player2.size.left - this.ball.radius;
        }
    }

    private void setScoreText(String text) {
        Message msg = scoreHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("score", text);
        msg.setData(b);
        scoreHandler.sendMessage(msg);
    }
}
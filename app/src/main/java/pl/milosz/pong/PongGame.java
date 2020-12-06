package pl.milosz.pong;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PongGame extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        PongSufraceView view =  findViewById(R.id.PongSurface);
        view.setScoreView((TextView) findViewById(R.id.scoreText));
    }
}

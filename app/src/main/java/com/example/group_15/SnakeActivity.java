package com.example.group_15;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class SnakeActivity extends Activity {
    private SnakeGame mSnakeGame;
    private Button mPauseButton, upButton, downButton, leftButton, rightButton;;
    private boolean isGameStarted = false;
    private boolean isGamePaused = false; // Flag to track if the game is paused
    private PauseButton pauseButton;
    private ArrowButtons arrowButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // Create a FrameLayout to hold the SnakeGame view and buttons
        FrameLayout layout = new FrameLayout(this);
        // Create an instance of SnakeGame class
        mSnakeGame = new SnakeGame(this, size);
        pauseButton = new PauseButton();
        mPauseButton = pauseButton.createPauseButton(this);
        layout.addView(mSnakeGame);
        layout.addView(mPauseButton);
        mPauseButton.setBackgroundColor(getResources().getColor(R.color.light_gray));
        mPauseButton.setAlpha(0.5f);
        arrowButtons = new ArrowButtons(this, mSnakeGame);
        // Add arrow buttons to the layout
        layout.addView(arrowButtons.getUpButton());
        layout.addView(arrowButtons.getDownButton());
        layout.addView(arrowButtons.getLeftButton());
        layout.addView(arrowButtons.getRightButton());
        setContentView(layout);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.togglePause(mSnakeGame);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSnakeGame.resume();
        mSnakeGame.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnakeGame.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP ||
                keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
                keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return mSnakeGame.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setPauseButtonVisibility(int visibility) {
        if (mPauseButton != null) {
            mPauseButton.setVisibility(visibility);
        }
    }
}

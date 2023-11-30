package com.example.group_15;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
public class SnakeActivity extends Activity {

    private SnakeGame mSnakeGame;
    private Button mPauseButton;
    private boolean isGameStarted = false;
    private boolean isGamePaused = false; // Flag to track if the game is paused
    private long lastClickTime = 0; // Variable to track last click time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Create a FrameLayout to hold the SnakeGame view and the pause button
        FrameLayout layout = new FrameLayout(this);

        // Create an instance of SnakeGame class
        mSnakeGame = new SnakeGame(this, size);

        // Create a pause button
        mPauseButton = new Button(this);
        mPauseButton.setText("||");

        // Set layout parameters for the pause button
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                100, // Set the width of the button (in pixels)
                100  // Set the height of the button (in pixels)
        );
        params.setMargins(20, 20, 20, 20); // Adjust margins as needed
        params.gravity = android.view.Gravity.TOP | android.view.Gravity.END; // Adjust gravity as needed
        mPauseButton.setLayoutParams(params);

        // Add the SnakeGame view and the pause button to the FrameLayout
        layout.addView(mSnakeGame);
        layout.addView(mPauseButton);

        setContentView(layout);

        // Set an OnClickListener for the pause button
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGamePaused) {
                    // If the game is paused, resume the game
                    mSnakeGame.resume();
                    mPauseButton.setText("||"); // Change button text/icon as needed for pause
                } else {
                    // If the game is running, pause the game
                    mSnakeGame.pause();
                    mPauseButton.setText("▶"); // Change button text/icon as needed for resume
                }

                isGamePaused = !isGamePaused; // Toggle the game pause state
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
}

package com.example.group_15;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
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
    private Button mPauseButton, upButton, downButton, leftButton, rightButton;;
    private boolean isGameStarted = false;
    private boolean isGamePaused = false; // Flag to track if the game is paused

    private PauseButton pauseButton;


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
        // Create arrow buttons and apply the specific layout for each button
        upButton = createArrowButton("↑", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 242, 230);
        downButton = createArrowButton("↓", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 242, 32);
        rightButton = createArrowButton("→", Gravity.CENTER | Gravity.END, 100,100, 0, 0, 142, 132);
        leftButton = createArrowButton("←", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 342, 132);



        // Add arrow buttons to the layout
        layout.addView(upButton);
        layout.addView(downButton);
        layout.addView(leftButton);
        layout.addView(rightButton);

        setContentView(layout);

        // Set listeners for arrow key buttons
        setArrowButtonListeners();
        // Set click listener for the pause button
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

    private void setArrowButtonListeners() {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeGame.setSnakeDirection(Snake.Heading.UP);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeGame.setSnakeDirection(Snake.Heading.DOWN);
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeGame.setSnakeDirection(Snake.Heading.LEFT);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeGame.setSnakeDirection(Snake.Heading.RIGHT);
            }
        });
    }

    private Button createArrowButton(String text, int gravityFlag, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        Button button = new Button(this);
        button.setText(text);

        // Set layout parameters for each arrow button
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = gravityFlag; // Set gravity based on direction
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin); // Set specific margins
        button.setLayoutParams(layoutParams);

        // Other button properties...
        button.setBackgroundColor(getResources().getColor(R.color.light_gray));
        button.setAlpha(0.5f); // Change the value to set the desired opacity (0.0f to 1.0f)

        return button;
    }
}

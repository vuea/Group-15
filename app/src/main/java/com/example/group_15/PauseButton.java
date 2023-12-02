package com.example.group_15;
import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

public class PauseButton extends AppCompatButton {
    private boolean isGamePaused = false;
    private SnakeGame mSnakeGame;

    public PauseButton(Context context, SnakeGame snakeGame) {
        super(context);
        mSnakeGame = snakeGame;
        initializeButton();
    }

    private void initializeButton() {
        setText("||"); // Set initial text/icon for pause
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGamePaused) {
                    // If the game is paused, resume the game
                    mSnakeGame.resume();
                    setText("||"); // Change button text/icon as needed for pause
                } else {
                    // If the game is running, pause the game
                    mSnakeGame.pause();
                    setText("▶"); // Change button text/icon as needed for resume
                }
                isGamePaused = !isGamePaused; // Toggle the game pause state
            }
        });
    }
}

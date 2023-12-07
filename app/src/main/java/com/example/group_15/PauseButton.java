package com.example.group_15;
import android.widget.Button;
import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;

public class PauseButton {
    private boolean isGamePaused = false;
    private Button mPauseButton;

    //Builds the half of the frontend/design
    //Pause Button
    public Button createPauseButton(Context context) {
        mPauseButton = new Button(context);
        mPauseButton.setText("||");

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                100, // Set the width of the button (in pixels)
                100  // Set the height of the button (in pixels)
        );
        params.setMargins(20, 20, 20, 20); // Adjust margins as needed
        params.gravity = Gravity.TOP | Gravity.END; // Adjust gravity as needed
        mPauseButton.setLayoutParams(params);
        return mPauseButton;
    }

    //Backend of the button
    //Calls the class SnakeGame for resume and pause when toggled
    public void togglePause(SnakeGame snakeGame) {
        if (isGamePaused) {
            snakeGame.resume();
            mPauseButton.setText("||"); // Change button text/icon as needed for pause
        } else {
            snakeGame.pause();
            mPauseButton.setText("▶"); // Change button text/icon as needed for resume
        }
        isGamePaused = !isGamePaused;
    }
}

package com.example.group_15;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class ArrowButtons {
    private Button upButton, downButton, leftButton, rightButton;
    private SnakeGame mSnakeGame;

    public ArrowButtons(Context context, SnakeGame snakeGame) {
        mSnakeGame = snakeGame; // Assign SnakeGame instance

        // Create arrow buttons
        upButton = createArrowButton(context, "↑", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 242, 230);
        downButton = createArrowButton(context, "↓", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 242, 32);
        rightButton = createArrowButton(context, "→", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 142, 132);
        leftButton = createArrowButton(context, "←", Gravity.CENTER | Gravity.END, 100, 100, 0, 0, 342, 132);

        // Set click listeners for arrow buttons
        setArrowButtonListeners();
    }

    // Method to create an arrow button
    private Button createArrowButton(Context context, String text, int gravityFlag, int width, int height,
                                     int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        Button button = new Button(context);
        button.setText(text);

        // Set layout parameters for each arrow button
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = gravityFlag; // Set gravity based on direction
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin); // Set specific margins
        button.setLayoutParams(layoutParams);

        // Other button properties...
        button.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        button.setAlpha(0.5f); // Change the value to set the desired opacity (0.0f to 1.0f)

        return button;
    }

    // Getter methods to access arrow buttons from outside
    public Button getUpButton() {
        return upButton;
    }

    public Button getDownButton() {
        return downButton;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public Button getRightButton() {
        return rightButton;
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
}

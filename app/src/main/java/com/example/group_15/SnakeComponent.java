package com.example.group_15;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface SnakeComponent {
    void move();
    boolean detectCollision();
    void draw(Canvas canvas, Paint paint);
    //Not using it since we are using the arrow key bindings
    //void switchHeading(MotionEvent motionEvent);
    boolean onKeyEvent(int keyCode, KeyEvent event);
}

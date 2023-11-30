package com.example.group_15;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

class SnakeSegment implements SnakeComponent {
    private Point location;
    private Bitmap bitmap;

    SnakeSegment(Point location, Bitmap bitmap) {
        this.location = location;
        this.bitmap = bitmap;
    }

    @Override
    public void move() {
        // Snake segment doesn't move on its own
    }

    @Override
    public boolean detectCollision() {
        // Snake segment doesn't collide on its own
        return false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, location.x, location.y, paint);
    }

    @Override
    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        // Snake segment doesn't handle key events
        return false;
    }
}

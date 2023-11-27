package com.example.group_15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

interface SnakeComponent {
    void move();

    boolean detectCollision();

    void draw(Canvas canvas, Paint paint);

    void switchHeading(MotionEvent motionEvent);

    boolean onKeyEvent(int keyCode, KeyEvent event);
}

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
    public void switchHeading(MotionEvent motionEvent) {
        // Snake segment doesn't change heading
    }

    @Override
    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        // Snake segment doesn't handle key events
        return false;
    }
}

class Snake implements SnakeComponent {
    private List<SnakeComponent> segments;
    private int mSegmentSize;
    private Point mMoveRange;

    // Other properties and methods...

    Snake(Context context, Point mr, int ss) {
        // Initialization...

        // Initialize segments as an empty list
        segments = new ArrayList<>();
    }

    // Other methods...

    @Override
    public void move() {
        for (SnakeComponent segment : segments) {
            segment.move();
        }
    }

    @Override
    public boolean detectCollision() {
        for (SnakeComponent segment : segments) {
            if (segment.detectCollision()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for (SnakeComponent segment : segments) {
            segment.draw(canvas, paint);
        }
    }

    @Override
    public void switchHeading(MotionEvent motionEvent) {
        // Only the head of the snake handles heading changes
        if (!segments.isEmpty()) {
            segments.get(0).switchHeading(motionEvent);
        }
    }

    @Override
    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        // Only the head of the snake handles key events
        if (!segments.isEmpty()) {
            return segments.get(0).onKeyEvent(keyCode, event);
        }
        return false;
    }

    // Additional methods for managing segments...
}

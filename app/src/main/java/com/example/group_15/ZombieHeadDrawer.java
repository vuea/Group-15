package com.example.group_15;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class ZombieHeadDrawer {
    private Point location;
    private int size;
    private Bitmap bitmapZombieHead;

    public ZombieHeadDrawer(Point location, int size, Bitmap bitmapZombieHead) {
        this.location = location;
        this.size = size;
        this.bitmapZombieHead = bitmapZombieHead;
    }

    public void drawZombieHead(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmapZombieHead, location.x * size, location.y * size, paint);
    }
}

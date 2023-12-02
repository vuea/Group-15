package com.example.group_15;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;


public class AppleDrawer {

    private Point location;
    private int size;


    private Bitmap bitmapApple;


    public AppleDrawer(Point location, int size, Bitmap bitmapApple) {
        this.location = location;
        this.size = size;
        this.bitmapApple = bitmapApple;
    }

    public void drawApple(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmapApple, location.x * size, location.y * size, paint);
    }

}

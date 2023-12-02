package com.example.group_15;


import android.graphics.Bitmap;

import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
public class SnakeDrawer {
    private ArrayList<Point> segmentLocations;
    private int mSegmentSize;
    private Bitmap mBitmapHeadRight, mBitmapHeadLeft, mBitmapHeadUp, mBitmapHeadDown;
    private Bitmap mBitmapBody;

    public SnakeDrawer(ArrayList<Point> segmentLocations, int segmentSize, Bitmap headRight, Bitmap headLeft, Bitmap headUp, Bitmap headDown, Bitmap body) {
        this.segmentLocations = segmentLocations;
        this.mSegmentSize = segmentSize;
        this.mBitmapHeadRight = headRight;
        this.mBitmapHeadLeft = headLeft;
        this.mBitmapHeadUp = headUp;
        this.mBitmapHeadDown = headDown;
        this.mBitmapBody = body;
    }

    public void drawSnake(Canvas canvas, Paint paint, Snake.Heading heading) {
        if (!segmentLocations.isEmpty()) {
            switch (heading) {
                case RIGHT:
                    canvas.drawBitmap(mBitmapHeadRight, segmentLocations.get(0).x * mSegmentSize, segmentLocations.get(0).y * mSegmentSize, paint);
                    break;
                case LEFT:
                    canvas.drawBitmap(mBitmapHeadLeft, segmentLocations.get(0).x * mSegmentSize, segmentLocations.get(0).y * mSegmentSize, paint);
                    break;
                case UP:
                    canvas.drawBitmap(mBitmapHeadUp, segmentLocations.get(0).x * mSegmentSize, segmentLocations.get(0).y * mSegmentSize, paint);
                    break;
                case DOWN:
                    canvas.drawBitmap(mBitmapHeadDown, segmentLocations.get(0).x * mSegmentSize, segmentLocations.get(0).y * mSegmentSize, paint);
                    break;
            }
            for (int i = 1; i < segmentLocations.size(); i++) {
                canvas.drawBitmap(mBitmapBody, segmentLocations.get(i).x * mSegmentSize, segmentLocations.get(i).y * mSegmentSize, paint);
            }
        }
    }
}

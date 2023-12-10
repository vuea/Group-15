package com.example.group_15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;

import java.util.Random;

public class ZombieHead {
    private Point location;
    private int size;
    private Bitmap mBitmapZombieHead;
    private ZombieHeadDrawer ZombieHeadDrawer;
    private Point mSpawnRange;
    private boolean disappeared;
    private long disappearedTime;
    private Handler disappearHandler;
    private boolean isDisappearDelayed;

    private static final int DISAPPEARANCE_DURATION = 5000; // 5 seconds disappearance duration
    private static final int REAPPEAR_DELAY = 5000; // 5 seconds delay before reappearance

    private Handler handler; // Reuse the Handler instance
    public ZombieHead(Context context, Point sr, int s) {
        mSpawnRange = sr;
        this.size = s;

        mBitmapZombieHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.zombie_head);
        mBitmapZombieHead = Bitmap.createScaledBitmap(mBitmapZombieHead, s, s, false);
        // Set initial location to (0, 0) or any other default value
        this.location = new Point(0, 0);
        //Draws zombie head
        this.ZombieHeadDrawer = new ZombieHeadDrawer(location, s, mBitmapZombieHead);

        handler = new Handler(); // Initialize the Handler
    }

    void spawnZombieHead() {
        // Calculate the center of the grid
        int centerX = mSpawnRange.x / 2;
        int centerY = mSpawnRange.y / 2;

        // Define a range around the center to spawn the apple
        int rangeX = mSpawnRange.x / 2; // Adjust the range as needed
        int rangeY = mSpawnRange.y / 2; // Adjust the range as needed

        Random random = new Random();
        boolean isOverlapping = true;

        // Keep generating random positions until it's not the same as the previous location
        while (isOverlapping) {
            int newLocationX = random.nextInt(rangeX) + (centerX - rangeX / 2);
            int newLocationY = random.nextInt(rangeY) + (centerY - rangeY / 2);

            // Check if the new location is different from the previous one
            isOverlapping = (location.x == newLocationX && location.y == newLocationY);

            // If it's not the same location, update the golden apple's location
            if (!isOverlapping) {
                location.x = newLocationX;
                location.y = newLocationY;
            }
        }
    }
    void drawZombieHead(Canvas canvas, Paint paint){
        ZombieHeadDrawer.drawZombieHead(canvas, paint);
    }
    Point getLocation() {
        return location;
    }

    public void disappearZombieHeadWithDelay(long delay) {
        final int DISAPPEARANCE_DELAY = 0; // Delay before disappearance (initially set to 0)

        if (!isDisappearDelayed) {
            isDisappearDelayed = true;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    disappeared = true;
                    disappearedTime = System.currentTimeMillis();
                    location.x = -10;
                    location.y = -10;
                    isDisappearDelayed = false;

                    scheduleZombieHeadReappearance(REAPPEAR_DELAY);
                }
            }, delay);
        }
    }

    public void scheduleZombieHeadReappearance(long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spawnZombieHead(); // Reappear the zombie head after delay milliseconds
            }
        }, delay);
    }
}

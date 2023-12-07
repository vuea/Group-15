package com.example.group_15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import java.util.Random;

public class GoldenApple {
    // The location of the apple on the grid
    // Not in pixels
    private Point location = new Point();
    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;
    // An image to represent the apple
    private Bitmap mBitmapGoldenApple;
    // Initialize appleDrawer class
    // logic of drawing the apple
    private GoldenAppleDrawer GoldenAppleDrawer;
    private Handler spawnHandler;
    private boolean isSpawnDelayed;
    private boolean eaten;
    private long eatenTime;
    private boolean ignoreRegularAppleConsumption = false;

    GoldenApple(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an apple
        mSize = s;
        // Hide the apple off-screen until the game starts
        location.x = -10;

        mBitmapGoldenApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.goldenapple);
        mBitmapGoldenApple = Bitmap.createScaledBitmap(mBitmapGoldenApple, s, s, false);
        //Draws apple
        this.GoldenAppleDrawer = new GoldenAppleDrawer(location, s, mBitmapGoldenApple);
        spawnHandler = new Handler();
        isSpawnDelayed = false;
    }

    void spawnGoldenApple() {
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

    Point getLocation(){
        return location;
    }

    void drawGoldenApple(Canvas canvas, Paint paint){
        GoldenAppleDrawer.drawGoldenApple(canvas, paint);
    }

    void spawnGoldenAppleWithDelay() {
        if (!isSpawnDelayed) {
            isSpawnDelayed = true;
            // Set the flag to ignore regular apple consumption during the delay
            ignoreRegularAppleConsumption = true;

            spawnHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spawnGoldenApple();
                    isSpawnDelayed = false; // Reset the delay flag after spawning
                    // Reset the flag to allow regular apple consumption after the delay
                    ignoreRegularAppleConsumption = false;
                }
            }, 2000); //How long the delay?
        }
    }
    public void setEaten() {
        eaten = true;
        eatenTime = System.currentTimeMillis();
        // Hide the apple when eaten
        location.x = -10;
        location.y = -10;
    }

    // Method to check if the golden apple is eaten and manage its reappearance after a certain duration
    public void manageDisappearance() {
        long currentTime = System.currentTimeMillis();
        if (eaten && (currentTime - eatenTime >= 0)) { // 10000 milliseconds = 10 seconds
            eaten = false; // Reset the eaten flag after the duration
            // Reappear the apple at a new location
            spawnGoldenApple();
        }
    }

    // Method to check if the golden apple is eaten
    public boolean isEaten() {
        return eaten;
    }
}

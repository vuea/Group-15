package com.example.group_15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Apple {
    // The location of the apple on the grid
    private Point location = new Point();
    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;
    // An image to represent the apple
    private Bitmap mBitmapApple;
    // Initialize appleDrawer class
    // logic of drawing the apple
    private AppleDrawer AppleDrawer;
    private Bitmap mBitmapGoldenApple;

    //Constructor Used In Apple Builder
    Apple(Point spawnRange, int size, Bitmap bitmapApple, AppleDrawer appleDrawer) {
        mSpawnRange = spawnRange;
        mSize = size;
        mBitmapApple = bitmapApple;
        AppleDrawer = appleDrawer;

    }

    /// Set up the apple in the constructor
    Apple(Context context, Point sr, int s){
        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an apple
        mSize = s;
        // Hide the apple off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s, s, false);

        mBitmapGoldenApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.goldenapple);
        mBitmapGoldenApple = Bitmap.createScaledBitmap(mBitmapGoldenApple, s, s, false);
        //Draws apple
        this.AppleDrawer = new AppleDrawer(location, s, mBitmapApple);
    }

    // This is called every time an apple is eaten
    void spawn(GoldenApple goldenApple) {
        // Calculate the center of the grid
        int centerX = mSpawnRange.x / 2;
        int centerY = mSpawnRange.y / 2;
        // Define a range around the center to spawn the apple
        int rangeX = mSpawnRange.x / 2; // Adjust the range as needed
        int rangeY = mSpawnRange.y / 2; // Adjust the range as needed
        Random random = new Random();
        boolean isOverlapping = true;
        while (isOverlapping) {
            // Generate random positions around the center of the grid
            location.x = random.nextInt(rangeX) + (centerX - rangeX / 2);
            location.y = random.nextInt(rangeY) + (centerY - rangeY / 2);
            // Check if the regular apple's location overlaps with the golden apple's location
            isOverlapping = (goldenApple.getLocation().x == location.x && goldenApple.getLocation().y == location.y);
        }
    }

    // Let SnakeGame know where the apple is
    Point getLocation(){
        return location;
    }

    // Draw the apple
    void drawApple(Canvas canvas, Paint paint){
        AppleDrawer.drawApple(canvas, paint);
    }
}


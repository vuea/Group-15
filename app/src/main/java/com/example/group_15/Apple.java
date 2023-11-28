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
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the apple
    private Bitmap mBitmapApple;
    // Initialize appleDrawer class
    // logic of drawing the apple
    private appleDrawer AppleDrawer;

    //private constructor
Apple(Point spawnRange, int size, Bitmap bitmapApple, appleDrawer appleDrawer) {
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

        //Draws apple
        this.AppleDrawer = new appleDrawer(location, s, mBitmapApple);
    }

    // This is called every time an apple is eaten
    void spawn(){
        // Calculate the center of the grid
        int centerX = mSpawnRange.x / 2;
        int centerY = mSpawnRange.y / 2;

        // Define a range around the center to spawn the apple
        int rangeX = mSpawnRange.x /2; // You can adjust the range as needed
        int rangeY = mSpawnRange.y /2; // You can adjust the range as needed

        // Choose random values within the defined range around the center
        Random random = new Random();
        location.x = random.nextInt(rangeX) + (centerX - rangeX / 2);
        location.y = random.nextInt(rangeY) + (centerY - rangeY / 2);
    }


    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the apple
    void draw(Canvas canvas, Paint paint){
        AppleDrawer.drawApple(canvas, paint);
    }
}


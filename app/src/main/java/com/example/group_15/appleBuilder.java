package com.example.group_15;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.content.Context;


public class appleBuilder {

    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapApple;

    private appleDrawer appleDrawer;
    //builder class for spawn
    public appleBuilder setSpawnRange(Point spawnRange) {

        this.mSpawnRange = spawnRange;
        return this;
    }

    public appleBuilder setSize(int size) {
        this.mSize = size;
        return this;
    }

    public appleBuilder setBitmap(Context context) {
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, mSize, mSize, false);
        return this;
    }


    public appleBuilder setAppleDrawer(appleDrawer appleDrawer) {
        this.appleDrawer = appleDrawer;
        return this;
    }
    public Apple build() {
        return new Apple(mSpawnRange, mSize, mBitmapApple, appleDrawer);
    }
}


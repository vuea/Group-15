package com.example.group_15;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.content.Context;

public class AppleBuilder {
    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapApple;
    private Bitmap mBitmapGoldenApple;
    private AppleDrawer appleDrawer;
    //builder class for spawn
    public AppleBuilder setSpawnRange(Point spawnRange) {

        this.mSpawnRange = spawnRange;
        return this;
    }

    public AppleBuilder setSize(int size) {
        this.mSize = size;
        return this;
    }

    public AppleBuilder setBitmap(Context context) {
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, mSize, mSize, false);
        mBitmapGoldenApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.goldenapple);
        mBitmapGoldenApple = Bitmap.createScaledBitmap(mBitmapGoldenApple, mSize, mSize, false);
        return this;
    }

    public AppleBuilder setAppleDrawer(AppleDrawer appleDrawer) {
        this.appleDrawer = appleDrawer;
        return this;
    }

    public Apple build() {
        return new Apple(mSpawnRange, mSize, mBitmapApple, appleDrawer);
    }
}


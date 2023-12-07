package com.example.group_15;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class Sound implements ISound {

    private SoundPool mSoundPool;
    private int mEatSoundID;
    private int mCrashSoundID;
    private int mSpeedSoundID;
    private int mBackgroundSoundID;

    public Sound(Context context) {
        initializeSoundPool(context);
    }

    private void initializeSoundPool(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("get_apple.ogg");
            mEatSoundID = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashSoundID = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("speed-effect.ogg");
            mSpeedSoundID = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("background-music.ogg");
            mBackgroundSoundID = mSoundPool.load(descriptor, 0);

        } catch (IOException e) {
            // Handle the error
            e.printStackTrace();
        }
    }

    @Override
    public void playEatSound() {
        mSoundPool.play(mEatSoundID, 1, 1, 0, 0, 1);
    }

    @Override
    public void playCrashSound() {
        mSoundPool.play(mCrashSoundID, 1, 1, 0, 0, 1);
    }

    public void playSpeedSound() {
        mSoundPool.play(mSpeedSoundID, 1, 1, 0, 0, 1);
    }

    @Override
    public void playBackgroundMusic() {
        mSoundPool.play(mBackgroundSoundID, 1, 1, 0, -1, 1);
    }

    @Override
    public void stopBackgroundMusic() {
        mSoundPool.stop(mBackgroundSoundID);
    }





    public int getEatSoundID() {
        return mEatSoundID;
    }

    public int getCrashSoundID() {
        return mCrashSoundID;
    }

    public int getSpeedSoundID() {
        return mSpeedSoundID;
    }

    public int getBackgroundMusic() {
        return mBackgroundSoundID;
    }

}

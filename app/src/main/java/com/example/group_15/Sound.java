package com.example.group_15;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import java.io.IOException;
import android.media.MediaPlayer;
import android.util.Log;


public class Sound implements ISound {
    private SoundPool mSoundPool;
    private int mEatSoundID;
    private int mCrashSoundID;
    private int mSpeedSoundID;

    private MediaPlayer backgroundMusicPlayer;

    public Sound(Context context) {
        initializeSoundPool(context);
    }

    private void initializeSoundPool(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("get_apple.ogg");
            mEatSoundID = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashSoundID = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("speed-effect.ogg");
            mSpeedSoundID = mSoundPool.load(descriptor, 0);


        } catch (IOException e) {
            // Handle the error
            e.printStackTrace();
        }
    }


    public void playBackgroundMusic(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("background-music.ogg");

            backgroundMusicPlayer = new MediaPlayer();
            backgroundMusicPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            backgroundMusicPlayer.setLooping(true);
            backgroundMusicPlayer.prepare();
            backgroundMusicPlayer.start();
        } catch (IOException e) {
            Log.e("BackgroundMusic", "Error playing background music: " + e.getMessage());
            // Handle error
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null && backgroundMusicPlayer.isPlaying()) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
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

}

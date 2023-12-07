package com.example.group_15;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer backgroundMusicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize MediaPlayer for background music
        backgroundMusicPlayer = new MediaPlayer();

        // Set the audio attributes (optional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            backgroundMusicPlayer.setAudioAttributes(audioAttributes);
        } else {
            backgroundMusicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        // Load and play the background music file
        playBackgroundMusic();
    }

    private void playBackgroundMusic() {
        try {
            // Access the music file from the assets folder
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("background_music.mp3");

            // Set the data source and prepare the MediaPlayer
            backgroundMusicPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            backgroundMusicPlayer.setLooping(true); // Enable looping
            backgroundMusicPlayer.prepare();
            backgroundMusicPlayer.start(); // Start playing the background music
        } catch (IOException e) {
            Log.e("BackgroundMusic", "Error playing background music: " + e.getMessage());
            Toast.makeText(this, "Error playing background music", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusicPlayer != null) {
            // Release the MediaPlayer when the activity is destroyed
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
    }
}

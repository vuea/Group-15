package com.example.group_15;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;


import java.io.IOException;

public class SnakeGame extends SurfaceView implements Runnable {

    private Thread mThread = null;
    private long mNextFrameTime;
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    private SoundPool mSoundPool;
    private int mEatSoundID;
    private int mCrashSoundID;
    private int mSpeedSoundID;
    private int mBackgroundSoundID;


    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    private int mScore;

    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    private Snake mSnake;
    private Apple mApple;
    private GoldenApple mGoldenApple;
    private Sound mSound;
    private Button mPauseButton;

    private boolean isGamePaused = false;
    private long goldenAppleTimer = 0;
    private boolean isGoldenAppleConsumed = false;
    private long goldenAppleSpawnInterval = 5000; // Time interval for golden apple spawn (in milliseconds)
    private int mBlockSize;
    private final int DESIRED_WIDTH = 1280;
    private final int DESIRED_HEIGHT = 720;
    private int highestScore = 0; // Variable to store the highest score
    private int mHighestScore = 0; // Variable to store the highest score


    private MediaPlayer backgroundMusicPlayer;

    public SnakeGame(Context context, Point size) {
        super(context);
        mSound = new Sound(context);
        mBlockSize = DESIRED_WIDTH / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = DESIRED_HEIGHT / mBlockSize;
        // Initialize game objects
        initializeGameObjects(context, size);

        // Create the pause button
        mPauseButton = new Button(context);

    }

    private void initializeGameObjects(Context context, Point size) {
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mGoldenApple = new GoldenApple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
    }

    public void newGame() {
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
        mApple.spawn(mGoldenApple);
        mScore = 0;
        mNextFrameTime = System.currentTimeMillis();

        // Check if the golden apple is not already spawned, then trigger the delayed spawn
        if (mGoldenApple.getLocation().x == -10) {
            startDelayedGoldenAppleSpawn();
        }

        // Check if background music player is not playing, then resume the music
        if (backgroundMusicPlayer == null || !backgroundMusicPlayer.isPlaying()) {
            playBackgroundMusic(getContext());
        }
    }



    @Override
    public void run() {

        while (mPlaying) {

            if (!mPaused) {
                if (updateRequired()) {
                    update();
                }
            }

            draw();
        }
    }

    public boolean updateRequired() {
        final long TARGET_FPS = 10;
        final long MILLIS_PER_SECOND = 1200;

        if (mNextFrameTime <= System.currentTimeMillis()) {
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS;
            return true;
        }

        return false;
    }

    public void update() {
        mSnake.move();
        final long SLOWER_DELAY_MS = 150;


        if (mSnake.checkDinner(mApple.getLocation(), mGoldenApple)) {
            mApple.spawn(mGoldenApple);
            mScore++;
            mSound.playEatSound();
        }

        if (mSnake.checkDinner(mGoldenApple.getLocation(), mGoldenApple)) {
            mGoldenApple.setEaten(); // Mark the golden apple as eate
        }

        if (mGoldenApple.isEaten()) {
            // If the golden apple is eaten, manage its reappearance after a certain duration
            mGoldenApple.manageDisappearance();
            mSound.playSpeedSound();
        }

        if (mSnake.detectDeath()) {


            mSound.stopBackgroundMusic();

            mSound.playCrashSound();
            mPaused = true;
            stopBackgroundMusic();

        }
    }


    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));
            drawScore();
            mApple.drawApple(mCanvas, mPaint);
            mGoldenApple.drawGoldenApple(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            drawPausedText();
            drawHighestScore(mCanvas, mPaint);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawScore() {
        mPaint.setColor(Color.argb(255, 255, 255, 255));
        mPaint.setTextSize(120);
        mCanvas.drawText(String.valueOf(mScore), 20, 120, mPaint);
    }

    private void drawPausedText() {
        if (mPaused) {
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(50);

            String customText = "Touch Screen";
            float textWidth = mPaint.measureText(customText);
            float x = (getWidth() - textWidth) / 2;
            float y = getHeight() / 2;

            mCanvas.drawText(customText, x, y, mPaint);

            // Hide the pause button when the game is not started
            setPauseButtonVisibility(View.INVISIBLE);
        } else {
            // Show the pause button when the game is started
            setPauseButtonVisibility(View.VISIBLE);
        }
    }

    //Click To Start Game Functionality
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // Check for key events
        int action = motionEvent.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
            // Start the game when a touch is detected, assuming game is paused
            if (mPaused) {
                mPaused = false;
                newGame();
                return true;
            }
        }
        return true;
    }

    //Click Functionality To Move The Snake
    /*
     @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    mPaused = false;
                    newGame();
                    return true;
                }
                mSnake.switchHeading(motionEvent);
                break;
            default:
                break;
        }
        return true;
    }
     */

    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPaused) {
            return super.onKeyDown(keyCode, event);
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                mSnake.setHeading(Snake.Heading.UP);
                return true;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                mSnake.setHeading(Snake.Heading.DOWN);
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                mSnake.setHeading(Snake.Heading.LEFT);
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mSnake.setHeading(Snake.Heading.RIGHT);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Modify the setPauseButtonVisibility() method to update the button visibility on the UI thread
    private void setPauseButtonVisibility(final int visibility) {
        post(new Runnable() {
            @Override
            public void run() {
                if (getContext() instanceof SnakeActivity) {
                    ((SnakeActivity) getContext()).setPauseButtonVisibility(visibility);
                }
            }
        });
    }

    public void startDelayedGoldenAppleSpawn() {
        mGoldenApple.spawnGoldenAppleWithDelay();
    }

    private void drawHighestScore(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(40); // Set the text size to 40 (you can adjust this value as needed)

        String highestScoreText = "Highest Score: " + mHighestScore;
        float textWidth = paint.measureText(highestScoreText);
        float x = (getWidth() - textWidth) / 2; // Center x-coordinate

        // Calculate y-coordinate for top center positioning
        float y = paint.getTextSize(); // Start at the text size (to add a buffer space, you can modify this value)

        // Draw the highest score text on the canvas
        canvas.drawText(highestScoreText, x, y, paint);
    }

    // Method to retrieve the highest score
    public int getHighestScore() {
        return highestScore;
    }

    // Method to update the highest score
    public void updateHighestScore(int currentScore) {
        if (currentScore > mHighestScore) {
            mHighestScore = currentScore;
        }
    }

    // Inside the SnakeGame class or where the game session ends (e.g., game-over conditions)

    public void gameOver() {
        // Assuming mScore holds the current game score
        if (mScore > mHighestScore) {
            mHighestScore = mScore; // Update the highest score if the current score is higher
        }
        updateHighestScore(mScore);
    }

    public void setSnakeDirection(Snake.Heading newHeading) {
        mSnake.setHeading(newHeading);
    }
    private void playBackgroundMusic(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("background-music.ogg");

            backgroundMusicPlayer = new MediaPlayer(); // Instantiate the MediaPlayer object
            backgroundMusicPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            backgroundMusicPlayer.setLooping(true); // Enable looping
            backgroundMusicPlayer.prepare();
            backgroundMusicPlayer.start(); // Start playing the background music
        } catch (IOException e) {
            Log.e("BackgroundMusic", "Error playing background music: " + e.getMessage());
            // Handle error
        }
    }


    private void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null && backgroundMusicPlayer.isPlaying()) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
    }

}
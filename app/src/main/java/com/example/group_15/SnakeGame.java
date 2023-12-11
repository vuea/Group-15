package com.example.group_15;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.media.MediaPlayer;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;


public class SnakeGame extends SurfaceView implements Runnable {
    private Thread mThread = null;
    private long mNextFrameTime;
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;
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
    private int mBlockSize;
    private final int DESIRED_WIDTH = 1280;
    private final int DESIRED_HEIGHT = 720;
    private int mHighestScore = 0; // Variable to store the highest score
    private MediaPlayer backgroundMusicPlayer;
    private ZombieHead mZombieHead;
    private static final long ZOMBIE_HEAD_DISAPPEAR_DURATION = 10000;
    private static final long ZOMBIE_HEAD_REAPPEAR_DELAY = 5000;
    private long mZombieHeadLastDisappearedTime;

    private boolean isGameOver = false;
    private boolean showTouchScreen = true;
    private Bitmap gameOverBitmap;

    public SnakeGame(Context context, Point size) {
        super(context);
        mSound = new Sound(context);
        mBlockSize = DESIRED_WIDTH / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = DESIRED_HEIGHT / mBlockSize;
        initializeGameObjects(context, size);
        mPauseButton = new Button(context);
        gameOverBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
    }

    private void initializeGameObjects(Context context, Point size) {
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mGoldenApple = new GoldenApple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mZombieHead = new ZombieHead(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
    }

    public void newGame() {
        isGameOver = false;
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
        mApple.spawn(mGoldenApple);
        mZombieHead.spawnZombieHead();
        mScore = 0;
        mNextFrameTime = System.currentTimeMillis();
        mGoldenApple.spawnGoldenApple();
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
            manageZombieHeadDisappearance();
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
        if (mSnake.checkDinner(mApple.getLocation(), mGoldenApple)) {
            mApple.spawn(mGoldenApple);
            mScore++;
            mSound.playCrunchSound();
        }
        if (mSnake.checkDinner(mGoldenApple.getLocation(), mGoldenApple)) {
            mGoldenApple.setEaten();
        }
        if (mGoldenApple.isEaten()) {
            mGoldenApple.manageDisappearance();
            mSound.playSpeedSound();
        }
        if (mSnake.detectDeath() || mZombieHeadCollision()) {
            mSound.stopBackgroundMusic();
            //mSound.stopGoldenAppleModeSound();
            mPaused = true;
            isGameOver = true; // Set the game over flag
            gameOver();
            mSound.playCrashSound();
        }
    }


    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                // Always clear the canvas at the beginning of each frame
                mCanvas.drawColor(Color.argb(255, 26, 128, 182));

                if (showTouchScreen) {
                    drawTouchScreen(); // Display "Touch Screen" text
                } else {
                    if (isGameOver) {
                        drawGameOverScreen(); // Display "Game Over" screen
                    } else {
                        drawGameObjects(); // Draw game objects during gameplay
                    }
                    drawHighestScore(mCanvas, mPaint); // Draw the highest score
                }

                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


    private void drawScore() {
        mPaint.setColor(Color.argb(255, 255, 255, 255));
        mPaint.setTextSize(120);
        mCanvas.drawText(String.valueOf(mScore), 20, 120, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
            if (mPaused) {
                mPaused = false;
                newGame();
                return true;
            }
            if (showTouchScreen) {
                showTouchScreen = false; // Hide "Touch Screen" text after the first touch event
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
        paint.setTextSize(57); //
        String highestScoreText = "Highest Score: " + mHighestScore;
        float textWidth = paint.measureText(highestScoreText);
        float x = (getWidth() - textWidth) / 2; // Center x-coordinate
        float y = paint.getTextSize();
        canvas.drawText(highestScoreText, x, y, paint);
    }

    // Method to update the highest score
    public void updateHighestScore(int currentScore) {
        if (currentScore > mHighestScore) {
            mHighestScore = currentScore;
        }
    }

    // Game ends it updates score
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

    public void playBackgroundMusic(Context context) {
        mSound.playBackgroundMusic(context);
    }

    public void stopBackgroundMusic() {
        mSound.stopBackgroundMusic();
    }

    private boolean mZombieHeadCollision() {
        Point snakeHeadLocation = mSnake.getSnakeHeadLocation(); // Get snake's head location
        return snakeHeadLocation.equals(mZombieHead.getLocation());
    }

    private void manageZombieHeadDisappearance() {
        long currentTime = System.currentTimeMillis();
        if (!mPaused && currentTime - mZombieHeadLastDisappearedTime >= ZOMBIE_HEAD_DISAPPEAR_DURATION) {
            // If the time for disappearance has passed, make the zombie head disappear
            mZombieHeadLastDisappearedTime = currentTime;
            mZombieHead.disappearZombieHeadWithDelay(ZOMBIE_HEAD_REAPPEAR_DELAY);
        }
    }

    public void drawGameOverScreen() {
        mCanvas.drawColor(Color.argb(255, 26, 128, 182)); // Clear the canvas
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(200);
        String gameOverText = "Game Over";
        float textWidth = mPaint.measureText(gameOverText);
        float x = (getWidth() - textWidth) / 2;
        float y = getHeight() / 2;
        mCanvas.drawText(gameOverText, x, y, mPaint);
        drawGameObjects(); // Draw game objects after displaying "Game Over"
    }

    private void drawGameObjects() {
        drawScore();
        mApple.drawApple(mCanvas, mPaint);
        mGoldenApple.drawGoldenApple(mCanvas, mPaint);
        mSnake.draw(mCanvas, mPaint);
        mZombieHead.drawZombieHead(mCanvas, mPaint);
    }

    public void drawTouchScreen() {
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(100);
        String customText = "Touch Screen";
        float textWidth = mPaint.measureText(customText);
        float x = (getWidth() - textWidth) / 2;
        float y = getHeight() / 2;
        mCanvas.drawText(customText, x, y, mPaint);
    }

}



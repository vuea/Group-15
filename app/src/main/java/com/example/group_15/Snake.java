package com.example.group_15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;

class Snake {
    private static final long SPEED_DURATION_MILLISECONDS = 2500; // Duration for speed increase in milliseconds (10 seconds)
    private ArrayList<Point> segmentLocations; // The location in the grid of all the segments
    private int mSegmentSize; // How big is each segment of the snake?
    private Point mMoveRange; // How big is the entire grid
    private int halfWayPoint; // Where is the centre of the screen
    private Sound mSound;
    private static final double initialSpeed = 0.5; // need this to reset speed
    private double mSpeed; //eat golden apple gain speed
    private boolean isSpeedIncreased; // Flag to track if speed is increased
    private long speedIncreaseDuration; // Duration for speed increase in milliseconds
    private long speedIncreaseStartTime;
    // For tracking movement Heading
    // Start by heading to the right
    private Heading heading = Heading.RIGHT;
    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    private Bitmap mBitmapBody;
    private SnakeDrawer SnakeDrawer;
    private Bitmap mBitmapHeadRightFast;
    private Bitmap mBitmapHeadLeftFast;
    private Bitmap mBitmapHeadUpFast;
    private Bitmap mBitmapHeadDownFast;
    private Bitmap mBitmapBodyFast;

    public void setSound(Sound sound) {
        mSound = sound;
    }
    public void setHeading(Heading newHeading) {
        heading = newHeading;
    }
    //headings
    public enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    Snake(Context context, Point mr, int ss) {
        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();
        // Initialize the segment size and movement
        // range from the passed in parameters
        mSegmentSize = ss;
        mMoveRange = mr;
        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);
        // Create 3 more versions of the head for different headings
        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);
        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);
        // Modify the bitmaps to face the snake head
        // in the correct direction
        mBitmapHeadRight = Bitmap
                .createScaledBitmap(mBitmapHeadRight,
                        ss, ss, false);
        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);
        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);
        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);
        // Create and scale the body
        mBitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);
        mBitmapBody = Bitmap
                .createScaledBitmap(mBitmapBody,
                        ss, ss, false);
        mBitmapHeadRightFast = BitmapFactory.decodeResource(context.getResources(), R.drawable.goldenhead);
        mBitmapHeadRightFast = Bitmap.createScaledBitmap(mBitmapHeadRightFast, mSegmentSize, mSegmentSize, false);

        Matrix matrixFastLeft = new Matrix();
        matrixFastLeft.preScale(-1, 1);
        mBitmapHeadLeftFast = Bitmap.createBitmap(mBitmapHeadRightFast, 0, 0, mSegmentSize, mSegmentSize, matrixFastLeft, true);

        Matrix matrixFastUp = new Matrix();
        matrixFastUp.preRotate(-90); // Rotate counter-clockwise by 90 degrees
        matrixFastUp.postScale(-1, 1); // Flip horizontally (along the X-axis)
        mBitmapHeadUpFast = Bitmap.createBitmap(mBitmapHeadRightFast, 0, 0, mSegmentSize, mSegmentSize, matrixFastUp, true);

        Matrix matrixFastDown = new Matrix();
        matrixFastDown.preRotate(90); // Rotate clockwise by 90 degrees
        matrixFastDown.postScale(-1, 1); // Flip horizontally (along the X-axis)
        mBitmapHeadDownFast = Bitmap.createBitmap(mBitmapHeadRightFast, 0, 0, mSegmentSize, mSegmentSize, matrixFastDown, true);


        // Create and scale the fast body bitmap
        mBitmapBodyFast = BitmapFactory.decodeResource(context.getResources(), R.drawable.goldenbody);
        mBitmapBodyFast = Bitmap.createScaledBitmap(mBitmapBodyFast, mSegmentSize, mSegmentSize, false);


        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x * ss / 2;
        // Draws snake
        this.SnakeDrawer = new SnakeDrawer(segmentLocations, mSegmentSize, mBitmapHeadRight,
                mBitmapHeadLeft, mBitmapHeadUp, mBitmapHeadDown, mBitmapBody);
        //Speed duration
        speedIncreaseDuration = SPEED_DURATION_MILLISECONDS;
    }

    // Get the snake ready for a new game
    void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;

        //reset speed
        mSpeed = initialSpeed;
        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(w / 2, h / 2));
    }

    public void move() {
        // Move the snake at the current speed
        for (int i = 0; i < mSpeed; i++) {
            // Move the body
            // Start at the back and move it
            // to the position of the segment in front of it
            for (int j = segmentLocations.size() - 1; j > 0; j--) {

                // Make it the same value as the next segment
                // going forwards towards the head
                segmentLocations.get(j).x = segmentLocations.get(j - 1).x;
                segmentLocations.get(j).y = segmentLocations.get(j - 1).y;
            }
            // Move the head in the appropriate heading
            // Get the existing head position
            // renamed point p to point head for easy readability
            Point head = segmentLocations.get(0);
            // Determine the new position if the snake continues in its current direction
            Point newHead = new Point(head);
            // Move it appropriately
            switch (heading) {
                case UP:
                    newHead.y--;
                    break;

                case RIGHT:
                    newHead.x++;
                    break;

                case DOWN:
                    newHead.y++;
                    break;

                case LEFT:
                    newHead.x--;;
                    break;
            }
            // Update the head position if the movement is allowed
            head.x = newHead.x;
            head.y = newHead.y;
        }
    }

    // SnakeGame class needs this for updating the score
    // It causes for a new game
    boolean detectDeath() {
        boolean screenEdgeCollision = checkScreenEdgeCollision();
        boolean selfCollision = checkSelfCollision();

        return screenEdgeCollision || selfCollision;
    }

    //Checks out of bounds of the game
    //For the snake
    private boolean checkScreenEdgeCollision() {
        Point headLocation = segmentLocations.get(0);

        boolean leftEdgeDetection = headLocation.x == -1;
        boolean rightEdgeDetection = headLocation.x > mMoveRange.x;
        boolean topEdgeDetection = headLocation.y == -1;
        boolean bottomEdgeDetection = headLocation.y > mMoveRange.y;

        return leftEdgeDetection || rightEdgeDetection || topEdgeDetection || bottomEdgeDetection;
    }

    private boolean checkSelfCollision() {
        Point headLocation = segmentLocations.get(0);

        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            Point segmentLocation = segmentLocations.get(i);

            if (headLocation.x == segmentLocation.x && headLocation.y == segmentLocation.y) {
                return true; // Snake has collided with itself
            }
        }

        return false; // No self-collision
    }

    boolean checkDinner(Point location, GoldenApple goldenApple) {
        boolean ateDinner = false;
        // Predict the snake's future positions and check for collision with the apple
        for (int i = 0; i < segmentLocations.size(); i++) {
            Point segment = segmentLocations.get(i);

            // Predict snake's future positions and check for collision
            for (double t = 0.0; t <= 1.0; t += 0.1) {
                int predictedX = (int) (segment.x + t * (segment.x - location.x));
                int predictedY = (int) (segment.y + t * (segment.y - location.y));

                // Check if the predicted position intersects with the apple
                if (predictedX == location.x && predictedY == location.y) {
                    // Collision detected
                    if (goldenApple.getLocation().equals(location)) {
                        increaseSpeed(); // Increase speed only when eating the golden apple
                        goldenApple.setEaten(); // Set the golden apple as eaten
                    } else {
                        segmentLocations.add(new Point(-10, -10)); // Add a new segment for a regular apple
                        ateDinner = true;
                        if (isSpeedIncreased) {
                            resetSpeed(); // Reset speed if a regular apple is eaten after a golden apple
                        }
                    }
                    break;
                }
            }
            if (isSpeedIncreased && System.currentTimeMillis() - speedIncreaseStartTime >= speedIncreaseDuration) {
                resetSpeed(); // Reset speed if the speed increase duration has elapsed
            }
        }
        return ateDinner;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isSpeedIncreased) {
            this.SnakeDrawer.drawSnakeWithSpeed(canvas, paint, heading, mBitmapHeadRightFast, mBitmapHeadLeftFast,
                    mBitmapHeadUpFast, mBitmapHeadDownFast, mBitmapBodyFast);
        } else {
            this.SnakeDrawer.drawSnake(canvas, paint, heading);
        }
    }


    // Handle changing direction clicking/touching
    /*
        @Override
        public void switchHeading(MotionEvent motionEvent) {

        // Is the tap on the right hand side?
        //create separate private constructor for rotating right and rotating left
        if (motionEvent.getX() >= halfWayPoint) {
            rotateRight();
        }else {
            rotateLeft();
        }
    }

    // Rotate right class
    private void rotateRight(){
        switch (heading) {
            case UP:
                heading = Heading.RIGHT;
                break;
            case RIGHT:
                heading = Heading.DOWN;
                break;
            case DOWN:
                heading = Heading.LEFT;
                break;
            case LEFT:
                heading = Heading.UP;
                break;
        }
    }

    // Rotate left class
    private void rotateLeft(){
        switch (heading) {
            case UP:
                heading = Heading.LEFT;
                break;
            case LEFT:
                heading = Heading.DOWN;
                break;
            case DOWN:
                heading = Heading.RIGHT;
                break;
            case RIGHT:
                heading = Heading.UP;
                break;
        }
    }
    */

    private void increaseSpeed() {
        mSpeed = 1.5;
        isSpeedIncreased = true;
        speedIncreaseStartTime = System.currentTimeMillis(); // Record the time when speed was increased
    }

    //whenever the speed duration goes out
    //this method is called
    private void resetSpeed() {
        mSpeed = 1; // Resetting the speed to zero
        isSpeedIncreased = false;
    }

    private Bitmap rotateFastBitmap(Bitmap sourceBitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }


}
package com.example.group_15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

class Snake {

    // The location in the grid of all the segments
    private ArrayList<Point> segmentLocations;

    // How big is each segment of the snake?
    private int mSegmentSize;

    // How big is the entire grid
    private Point mMoveRange;

    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    public enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;
    // Initialize snakeDrawer class
    // logic of drawing the head of the snake
    private snakeDrawer SnakeDrawer;

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

        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x * ss / 2;

        // Draws snake
        this.SnakeDrawer = new snakeDrawer(segmentLocations, mSegmentSize, mBitmapHeadRight,
                mBitmapHeadLeft, mBitmapHeadUp, mBitmapHeadDown, mBitmapBody);

    }

    // Get the snake ready for a new game
    void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(w / 2, h / 2));
    }


    void move() {
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next segment
            // going forwards towards the head
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }

        // Move the head in the appropriate heading
        // Get the existing head position
        //renamed point p to point head for easy readability
        Point head = segmentLocations.get(0);

        // Move it appropriately
        switch (heading) {
            case UP:
                head.y--;
                break;

            case RIGHT:
                head.x++;
                break;

            case DOWN:
                head.y++;
                break;

            case LEFT:
                head.x--;
                break;
        }

    }

    boolean detectDeath() {
        boolean screenEdgeCollision = checkScreenEdgeCollision();
        boolean selfCollision = checkSelfCollision();

        return screenEdgeCollision || selfCollision;
    }

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






    boolean checkDinner(Point location) {
        //changed point l to point location
        //if (snakeXs[0] == l.x && snakeY
        // s[0] == l.y) {
        if (segmentLocations.get(0).x == location.x &&
                segmentLocations.get(0).y == location.y) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the segment in front of it
            segmentLocations.add(new Point(-10, -10));
            return true;
        }
        return false;
    }

    void draw(Canvas canvas, Paint paint) {
        SnakeDrawer.drawSnake(canvas, paint, heading);
    }

    // Handle changing direction
    void switchHeading(MotionEvent motionEvent) {

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
}

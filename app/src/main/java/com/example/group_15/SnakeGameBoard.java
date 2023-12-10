package com.example.group_15;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class SnakeGameBoard extends View {
    private int numRows;
    private int numCols;
    private Point snakePosition;
    private Point applePosition;
    private Point[][] parent;
    public SnakeGameBoard(Context context) {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGameBoard(canvas);
    }

    private void drawGameBoard(Canvas canvas) {
        // Draw grid lines
        // Draw snake at snakePosition
        // Draw apple at applePosition
    }

    public void updateSnakePosition(Point newPosition) {
        snakePosition = newPosition;
        invalidate(); // Redraw the view
    }

    public void updateApplePosition(Point newPosition) {
        applePosition = newPosition;
        invalidate(); // Redraw the view
    }

    public ArrayList<Point> findPathToApple() {
        // Implement DFS algorithm here to find a path for the snake to the apple
        ArrayList<Point> path = new ArrayList<>();
        Stack<Point> stack = new Stack<>();
        // Assume snake and apple positions are available
        Point start = snakePosition;
        Point goal = applePosition;

        boolean[][] visited = new boolean[numRows][numCols];
        stack.push(start);
        visited[start.x][start.y] = true;

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            if (current.equals(goal)) {
                // Found the goal, construct the path
                while (!current.equals(start)) {
                    path.add(current);
                    current = parent[current.x][current.y];
                }
                // Reverse the path to get from start to goal
                Collections.reverse(path);
                return path;
            }

            // Explore neighbors (up, down, left, right) and push to stack if not visited
            for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                Point neighbor = new Point(newX, newY);

                if (isValid(neighbor) && !visited[newX][newY]) {
                    stack.push(neighbor);
                    visited[newX][newY] = true;
                    // For backtracking
                    parent[newX][newY] = current;
                }
            }
        }

        // If no path is found, return an empty list
        return new ArrayList<>();
    }

    private boolean isValid(Point point) {
        return point.x >= 0 && point.x < numRows && point.y >= 0 && point.y < numCols;
    }
}

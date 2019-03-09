package com.ryan.eater.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * created by 2019/3/9 12:18 PM
 *
 * @author Ryan
 */
public class GameView extends View {

    private static final String ASSETS_MAP_NAME = "map";
    private static final String WALL = "#";
    private static final char CHAR_FOOD = '.';
    private static final String FOOD = ".";
    private static final String EATER = "@";
    private static final String PLACE = " ";
    private static final int MATRIX_LENGTH = 15;
    private static final int PAINT_TEXT_SIZE = 60;

    private GameView.GameController mGameController;
    private long mStartTime;
    private String[][] mGameMatrix;
    private Paint mPaint;
    private int[] mEaterCoordinate;
    private int mRemainFoodCount;
    private OnGameOverListener mGameEndListener;
    private IBestTimeSaver mBestTimeSaver;
    private int mXSpace = 0;
    private int mYSpace = 0;
    private int mTextWidth = 0;
    private int mTextHeight = 0;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGameMatrix = new String[MATRIX_LENGTH][MATRIX_LENGTH];
        mEaterCoordinate = new int[2];
        mBestTimeSaver = new BestTimeSaver(context);
        mGameController = new InnerGameController();
        initPaint();
        initEater();
        initMap();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setTextSize(PAINT_TEXT_SIZE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSpaceValue();
    }

    private void initSpaceValue() {
        mTextWidth = (int) mPaint.measureText(WALL);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mTextHeight = (int) (metrics.descent - metrics.ascent);
        mXSpace = (getWidth() - MATRIX_LENGTH * mTextWidth) / (MATRIX_LENGTH - 1);
        mYSpace = (getHeight() - MATRIX_LENGTH * mTextHeight) / (MATRIX_LENGTH - 1);
    }

    private void initEater() {
        mEaterCoordinate[0] = MATRIX_LENGTH - 2;
        mEaterCoordinate[1] = MATRIX_LENGTH - 2;
    }

    private void initMap() {
        try {
            InputStream inputStream = getContext().getAssets().open(ASSETS_MAP_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int lineNo = -1;
            while ((line = reader.readLine()) != null) {
                char[] arr = line.toCharArray();
                if (arr.length != MATRIX_LENGTH) {
                    continue;
                }
                lineNo++;
                for (int i = 0; i < arr.length; i++) {
                    char c = arr[i];
                    mGameMatrix[i][lineNo] = String.valueOf(c);
                    if (c == CHAR_FOOD) {
                        mRemainFoodCount++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMap(canvas);
    }

    private void drawMap(Canvas canvas) {
        for (int i = 0; i < mGameMatrix.length; i++) {
            for (int j = 0; j < mGameMatrix[i].length; j++) {
                String s = mGameMatrix[i][j];
                if (s == null) {
                    s = PLACE;
                }
                canvas.drawText(s, i * mXSpace + i * mTextWidth, j * mYSpace + (j + 1) * mTextHeight, mPaint);
            }
        }
    }

    private void judgeGameEnd() {
        if (mRemainFoodCount == 0) {
            if (mGameEndListener != null) {
                long spendTime = System.currentTimeMillis() - mStartTime;
                long bestTime = mBestTimeSaver.save(spendTime);
                mGameEndListener.onGameOver(spendTime, bestTime);
            }
        }
    }

    public void restartGame() {
        initEater();
        initMap();
        mStartTime = 0;
        invalidate();
    }

    public interface OnGameOverListener {
        void onGameOver(long gameTime, long bestTime);
    }

    public interface GameController {
        void moveLeft();

        void moveRight();

        void moveTop();

        void moveBottom();
    }

    private class InnerGameController implements GameController {
        @Override
        public void moveLeft() {
            int currX = mEaterCoordinate[0];
            int currY = mEaterCoordinate[1];
            if (currX <= 1) {
                return;
            }
            String move = mGameMatrix[currX - 1][currY];
            if (!WALL.equals(move)) {
                if (FOOD.equals(move)) {
                    mRemainFoodCount--;
                }
                mGameMatrix[currX - 1][currY] = EATER;
                mGameMatrix[currX][currY] = null;
                mEaterCoordinate[0] = currX - 1;
                invalidate();
                recordStartTime();
                judgeGameEnd();
            }
        }

        @Override
        public void moveRight() {
            int currX = mEaterCoordinate[0];
            int currY = mEaterCoordinate[1];
            if (currX >= MATRIX_LENGTH - 2) {
                return;
            }
            String move = mGameMatrix[currX + 1][currY];
            if (!WALL.equals(move)) {
                if (FOOD.equals(move)) {
                    mRemainFoodCount--;
                }
                mGameMatrix[currX + 1][currY] = EATER;
                mGameMatrix[currX][currY] = null;
                mEaterCoordinate[0] = currX + 1;
                invalidate();
                recordStartTime();
                judgeGameEnd();
            }
        }

        @Override
        public void moveTop() {
            int currX = mEaterCoordinate[0];
            int currY = mEaterCoordinate[1];
            if (currY <= 1) {
                return;
            }
            String move = mGameMatrix[currX][currY - 1];
            if (!WALL.equals(move)) {
                if (FOOD.equals(move)) {
                    mRemainFoodCount--;
                }
                mGameMatrix[currX][currY - 1] = EATER;
                mGameMatrix[currX][currY] = null;
                mEaterCoordinate[1] = currY - 1;
                invalidate();
                recordStartTime();
                judgeGameEnd();
            }
        }

        @Override
        public void moveBottom() {
            int currX = mEaterCoordinate[0];
            int currY = mEaterCoordinate[1];
            if (currY >= MATRIX_LENGTH - 2) {
                return;
            }
            String move = mGameMatrix[currX][currY + 1];
            if (!WALL.equals(move)) {
                if (FOOD.equals(move)) {
                    mRemainFoodCount--;
                }
                mGameMatrix[currX][currY + 1] = EATER;
                mGameMatrix[currX][currY] = null;
                mEaterCoordinate[1] = currY + 1;
                invalidate();
                recordStartTime();
                judgeGameEnd();
            }
        }
    }

    private void recordStartTime() {
        if (mStartTime == 0) {
            mStartTime = System.currentTimeMillis();
        }
    }

    public GameController getGameController() {
        return mGameController;
    }

    public void setGameOverListener(OnGameOverListener listener) {
        this.mGameEndListener = listener;
    }
}

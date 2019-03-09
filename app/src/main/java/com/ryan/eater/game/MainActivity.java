package com.ryan.eater.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements GameView.OnGameOverListener {

    private GameView mGameView;
    private GameView.GameController mGameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameView = findViewById(R.id.game_view);
        mGameView.setGameOverListener(this);
        ImageView leftBtn = findViewById(R.id.left_btn);
        ImageView rightBtn = findViewById(R.id.right_btn);
        ImageView upBtn = findViewById(R.id.up_btn);
        ImageView downBtn = findViewById(R.id.down_btn);

        mGameController = mGameView.getGameController();

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameController.moveLeft();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameController.moveRight();
            }
        });
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameController.moveTop();
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameController.moveBottom();
            }
        });
    }

    @Override
    public void onGameOver(long gameTime, long bestTime) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setCancelable(false)
                .setMessage("Game Time: " + (gameTime / 1000) + "s\n" + "Best Time: " + bestTime / 1000 + "s")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGameView.restartGame();
                    }
                })
                .show();
    }
}

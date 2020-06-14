package com.yoneyonekun.practice12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        //xml取り込み
        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);
        TextView game=findViewById(R.id.gameLabel);

        //MainActivityからスコアを受け取る
        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        //ブロックを全部消した場合
        if (score>=360){
            game.setText("CLEAR!!");
        }

        //得点をSharedPreferencesに格納
        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);

        //ハイスコア更新時
        if (score > highScore) {
            highScoreLabel.setText("High Score : " + score);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.apply();


        } else {
            highScoreLabel.setText("High Score : " + highScore);
        }

    }
    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() { }

}


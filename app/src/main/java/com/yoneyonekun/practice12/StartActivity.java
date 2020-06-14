package com.yoneyonekun.practice12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //startボタン押下時処理
    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));     //MainActivityへ遷移

    }
}

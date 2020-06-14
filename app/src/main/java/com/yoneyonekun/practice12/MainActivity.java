package com.yoneyonekun.practice12;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Handler&Timer
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //action status
    private boolean action_flg = false;
    private boolean start_flg = false;

    //Size
    private int frameWidth;
    private int frameHeight;
    private int boxSize;
    private int boxSizeY;

    //blockSize
    private float blockSizeX[] = new float[12];
    private float blockSizeY[] = new float[12];

    //ボールの座標管理
    private float orangeX, orangeY;

    //xml取り込み準備
    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView orange;
    private Button box;

    //xmlのblockの取得
    private Button block[] = new Button[12];

    //blockの衝突判定
    private boolean[] bFlg = new boolean[12];


    //boxの位置パラメータ
    private float boxX;

    //blockの位置パラメータ
    private float[] blockX = new float[12];
    private float[] blockY = new float[12];


    //ボールスピードパラメータ
    private int v = -13;
    private int w = 13;

    //スコア表示用パラメータ
    private int score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < block.length; i++) {
            bFlg[i] = true;
        }

        //xml取り込み
        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        orange = findViewById(R.id.orange);

        //block(xml)取り込み
        block[0] = findViewById(R.id.block1);
        block[1] = findViewById(R.id.block2);
        block[2] = findViewById(R.id.block3);
        block[3] = findViewById(R.id.block4);
        block[4] = findViewById(R.id.block5);
        block[5] = findViewById(R.id.block6);
        block[6] = findViewById(R.id.block7);
        block[7] = findViewById(R.id.block8);
        block[8] = findViewById(R.id.block9);
        block[9] = findViewById(R.id.block10);
        block[10] = findViewById(R.id.block11);
        block[11] = findViewById(R.id.block12);

        //screenSize取得
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        scoreLabel.setText("Score ： 0");
    }

    public void changePos() {

        hitBlock();
        hitBox();

        //ボールの座標移動準備
        orangeX += v;
        orangeY += w;

        //スクリーン上の衝突判定
        if (orangeX > frameWidth - 70) v = v * (-1);
        else if (orangeY > frameHeight - 70) {
            if (timer != null) {
                timer.cancel();
                timer = null;

                // 結果画面へ
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);      //ResultActivityへ遷移
                intent.putExtra("SCORE", score);        //スコアの受け渡し

                startActivity(intent);
            }
        } else if (orangeX < 1) v = v * (-1);
        else if (orangeY < 1) w = w * (-1);

            //ブロック全消し時の画面遷移
        else if (score >= 30 * block.length) {
            if (timer != null) {
                timer.cancel();
                timer = null;

                // 結果画面へ
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);     //ResultActivityへ遷移
                intent.putExtra("SCORE", score);       //スコアの受け渡し

                startActivity(intent);

            }
        }
        //ボールの座標移動
        orange.setX(orangeX);
        orange.setY(orangeY);

        //boxの移動条件
        if (action_flg) {
            boxX += 30;
        } else {
            boxX -= 30;
        }
        //左右のスクリーンかboxが外に出ない
        if (boxX < 0) boxX = 0;
        if (boxX > frameWidth - boxSize) boxX = frameWidth - boxSize;
        box.setX(boxX);

        //スコアの表示
        scoreLabel.setText("Score : " + score);
    }

    public void hitBlock() {

        //ボールの中心座標取得
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        for (int i = 0; i < block.length; i++) {

            //blockの座標取得
            blockX[i] = block[i].getX();
            blockY[i] = block[i].getY();

            //blockの大きさ取得
            blockSizeX[i] = block[i].getWidth();
            blockSizeY[i] = block[i].getHeight();

            if (bFlg[i]) {

                //block衝突判定条件
                if (blockY[i] <= orangeCenterY && orangeCenterY <= blockY[i] + blockSizeY[i] &&
                        blockX[i] <= orangeCenterX && orangeCenterX <= blockX[i] + blockSizeX[i]) {

                    w = w * (-1);
                    block[i].setVisibility(View.GONE);  //ブロックがの表示が消える
                    score += 30;                        //スコア加算
                    bFlg[i] = false;                    //オブジェクトとしてブロックを消す
                }
            }
        }


    }

    public void hitBox() {

        float orangeCenterX = orangeX + orange.getWidth() / 2;      //ボールのX軸中心
        float orangeCenterY = orangeY + orange.getHeight() / 2;     //ボールのY軸中心

        //ボールとboxの衝突判定
        if (frameHeight >= orangeCenterY && orangeCenterY >= frameHeight - boxSizeY &&
                boxX <= orangeCenterX && orangeCenterX <= boxX + boxSize) {

            w = w * (-1);
        }
    }

    //各種パラメータの取得
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //ゲームの初期画面設定
        if (start_flg == false) {
            start_flg = true;
            startLabel.setVisibility(View.GONE);

            //ボールの初期位置
            orangeX = 500;
            orangeY = 700;

            //画面の大きさ取得
            FrameLayout frame = findViewById(R.id.frame);
            frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();

            //boxのX座標
            boxX = box.getX();

            //boxサイズ取得
            boxSize = box.getWidth();
            boxSizeY = box.getHeight();

            //タイマーセット
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        } else {

            //タップしている状態では
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                //バーが右へ移動
                action_flg = true;

                //タップしていない状態では
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                //バーは左へ移動(定位置へ戻るよう移動)
                action_flg = false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
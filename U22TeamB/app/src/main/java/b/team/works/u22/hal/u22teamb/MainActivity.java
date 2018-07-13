package b.team.works.u22.hal.u22teamb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearContentsArea;    // 内容エリア
    private Button btnArrow;                    // 展開ボタン
    private final static int DURATION = 800;    // アニメーションにかける時間(ミリ秒)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 内容エリアの結び付け
        linearContentsArea = (LinearLayout) findViewById(R.id.llContent);
        // ボタンの結び付け
        btnArrow = (Button) findViewById(R.id.btBottom);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearContentsArea.getHeight();

        // 内容エリアを閉じるアニメーション
        ResizeAnimation closeAnimation = new ResizeAnimation(linearContentsArea, -originalHeight, originalHeight);
        closeAnimation.setDuration(DURATION);
        linearContentsArea.startAnimation(closeAnimation);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 内容エリアの結び付け
        linearContentsArea = (LinearLayout) findViewById(R.id.llContent);
        // ボタンの結び付け
        btnArrow = (Button) findViewById(R.id.btBottom);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearContentsArea.getHeight();

        // 展開ボタン押下時
        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (linearContentsArea.getHeight() > 0) {       // 内容エリアが開いている時

                    // 内容エリアを閉じるアニメーション
                    ResizeAnimation closeAnimation = new ResizeAnimation(linearContentsArea, -originalHeight, originalHeight);
                    closeAnimation.setDuration(DURATION);
                    linearContentsArea.startAnimation(closeAnimation);

                } else {

                    // 内容エリアが閉じている時、内容エリアを開くアニメーション
                    ResizeAnimation openAnimation = new ResizeAnimation(linearContentsArea, originalHeight, 0);
                    openAnimation.setDuration(DURATION);    // アニメーションにかける時間(ミリ秒)
                    linearContentsArea.startAnimation(openAnimation);   // アニメーション開始

                }
            }
        });
    }
}

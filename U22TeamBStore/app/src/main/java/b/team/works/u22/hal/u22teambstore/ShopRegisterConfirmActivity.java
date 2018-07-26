package b.team.works.u22.hal.u22teambstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShopRegisterConfirmActivity extends AppCompatActivity {

    //各部品取得用定数
    TextView tvShopId; //店舗ID
    TextView tvShopName; //店舗名
    TextView tvPhontvic; //店舗名（カナ）
    TextView tvOpenTime; //営業時間
    TextView tvTel; //電話番号
    TextView tvAddress; //住所
    TextView tvAverageBudgtv; //平均予算
    TextView tvPointLatitude; //緯度
    TextView tvPointLongitude; //経度
    TextView tvLunchServiceAvailable; //ランチ営業
    TextView tvNonSmokingSeatAvailable; //禁煙席
    TextView tvCardUsageAvailable; //カード利用
    TextView tvImage1; //店舗画像01
    TextView tvImage2; //店舗画像02
    TextView tvPassword; //パスワード
    TextView tvNo; //項番
    TextView tvFreeName; //フリーワード

    //インテントオブジェクト
    Intent _intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register_confirm);

        _intent = getIntent();

        findViewAll();
        viewStv();
    }

    /**
     * 戻るボタンが押されたとき
     */
    public void onClickBack(View view) {
        _intent = new Intent(ShopRegisterConfirmActivity.this, ShopRegisterActivity.class);
        startActivity(_intent);
    }

    /**
     * 送信ボタンが押されたとき
     */
    public void onClickSend(View view) {
        //サーブレットへと値送信
    }

    /**
     * インテントから取得した値を各画面部品にセットするメソッド
     */
    private void viewStv() {
        tvShopId.setText( _intent.getStringExtra("shopId") );
        tvShopName.setText( _intent.getStringExtra("shopName") );
        tvPhontvic.setText( _intent.getStringExtra("phonetic") );
        tvOpenTime.setText( _intent.getStringExtra("openTime") );
        tvTel.setText( _intent.getStringExtra("tel") );
        tvAddress.setText( _intent.getStringExtra("address") );
        tvAverageBudgtv.setText( _intent.getStringExtra("averageBudget") );
        tvPointLatitude.setText( _intent.getStringExtra("pointLatitude") );
        tvPointLongitude.setText( _intent.getStringExtra("pointLongitude") );
        tvLunchServiceAvailable.setText( _intent.getStringExtra("lunchService") );
        tvNonSmokingSeatAvailable.setText( _intent.getStringExtra("nonSmokingSeat") );
        tvCardUsageAvailable.setText( _intent.getStringExtra("cardUsage") );

        //画像
        tvImage1.setText( _intent.getStringExtra("image1") );
        tvImage2.setText( _intent.getStringExtra("image2") );

        tvPassword.setText( _intent.getStringExtra("password") );
        tvNo.setText( _intent.getStringExtra("no") );
        tvFreeName.setText( _intent.getStringExtra("freeName") );
    }

    /**
     * 各部品の取得を行うメソッド
     */
    private void findViewAll() {
        tvShopId = findViewById(R.id.tvShopId);
        tvShopName = findViewById(R.id.tvShopName);
        tvPhontvic = findViewById(R.id.tvPhonetic);
        tvOpenTime = findViewById(R.id.tvOpenTime);
        tvTel = findViewById(R.id.tvTel);
        tvAddress = findViewById(R.id.tvAddress);
        tvAverageBudgtv = findViewById(R.id.tvAverageBudget);
        tvPointLatitude = findViewById(R.id.tvPointLatitude);
        tvPointLongitude = findViewById(R.id.tvPointLongitude);
        tvLunchServiceAvailable = findViewById(R.id.tvLunchService);
        tvNonSmokingSeatAvailable = findViewById(R.id.tvNonSmokingSeat);
        tvCardUsageAvailable = findViewById(R.id.tvCardUsage);
        tvImage1 = findViewById(R.id.tvImage1);
        tvImage2 = findViewById(R.id.tvImage2);
        tvPassword = findViewById(R.id.tvPassword);
        tvNo = findViewById(R.id.tvNo);
        tvFreeName = findViewById(R.id.tvFreeName);
    }
}

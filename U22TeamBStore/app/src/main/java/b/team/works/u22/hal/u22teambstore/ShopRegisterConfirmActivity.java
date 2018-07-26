package b.team.works.u22.hal.u22teambstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class ShopRegisterConfirmActivity extends AppCompatActivity {

    //各部品取得用定数
    EditText etShopId; //店舗ID
    EditText etShopName; //店舗名
    EditText etPhonetic; //店舗名（カナ）
    EditText etOpenTime; //営業時間
    EditText etTel; //電話番号
    EditText etAddress; //住所
    EditText etAverageBudget; //平均予算
    EditText etPointLatitude; //緯度
    EditText etPointLongitude; //経度
    EditText etLunchServiceAvailable; //ランチ営業
    EditText etNonSmokingSeatAvailable; //禁煙席
    EditText etCardUsageAvailable; //カード利用
    ImageView ivImage1; //店舗画像01
    ImageView ivImage2; //店舗画像02
    EditText etPassword; //パスワード
    EditText etNo; //項番
    EditText etFreeName; //フリーワード

    //インテントオブジェクト
    Intent _intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register_confirm);

        _intent = getIntent();
//        String name = _intent.getStringExtra("shopName");
//        Log.d("店舗名", name);

        viewSet();
    }

    /**
     * インテントから取得した値を各画面部品にセットするメソッド
     */
    private void viewSet() {
        etShopId.setText( _intent.getStringExtra("shopId") );
        etShopName.setText( _intent.getStringExtra("shopName") );
        etPhonetic.setText( _intent.getStringExtra("phonetic") );
        etOpenTime.setText( _intent.getStringExtra("openTime") );
        etTel.setText( _intent.getStringExtra("tel") );
        etAddress.setText( _intent.getStringExtra("address") );
        etAverageBudget.setText( _intent.getStringExtra("averageBudget") );
        etPointLatitude.setText( _intent.getStringExtra("pointLatitude") );
        etPointLongitude.setText( _intent.getStringExtra("pointLongitude") );
        etLunchServiceAvailable.setText( _intent.getStringExtra("lunchService") );
        etNonSmokingSeatAvailable.setText( _intent.getStringExtra("nonSmokingSeat") );
        etCardUsageAvailable.setText( _intent.getStringExtra("cardUsage") );

        //画像

        etPassword.setText( _intent.getStringExtra("password") );
        etNo.setText( _intent.getStringExtra("no") );
        etFreeName.setText( _intent.getStringExtra("freeName") );
    }
}

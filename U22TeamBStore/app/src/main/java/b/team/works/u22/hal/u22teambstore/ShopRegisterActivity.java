package b.team.works.u22.hal.u22teambstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ShopRegisterActivity extends AppCompatActivity {

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
    RadioButton rbLunchServiceAvailable; //ランチ営業有
    RadioButton rbLunchServiceNotAvailable; //ランチ営業無
    RadioButton rbNonSmokingSeatAvailable; //禁煙席有
    RadioButton rbNonSmokingSeatNotAvailable; //禁煙席無
    RadioButton rbCardUsageAvailable; //カード利用有
    RadioButton rbCardUsageNotAvailable; //カード利用無
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
        setContentView(R.layout.activity_shop_register);

        //各部品取得
        findViewAll();
    }

    /**
     * 確認ボタンが押されたときの処理
     * @param view　画面部品
     */
    public void onClickConfirm(View view) {
        _intent = new Intent(ShopRegisterActivity.this, ShopRegisterConfirmActivity.class);

        _intent.putExtra("shopId", etShopId.getText().toString());
        _intent.putExtra("shopName", etShopName.getText().toString());
        _intent.putExtra("phonetic", etPhonetic.getText().toString());
        _intent.putExtra("openTime", etOpenTime.getText().toString());
        _intent.putExtra("tel", etTel.getText().toString());
        _intent.putExtra("address", etAddress.getText().toString());
        _intent.putExtra("averageBudget", etAverageBudget.getText().toString());
        _intent.putExtra("pointLatitude", etPointLatitude.getText().toString());
        _intent.putExtra("pointLongitude", etPointLongitude.getText().toString());

        //ランチ営業有りか無しか
        String lunchService;
        if(rbLunchServiceAvailable.isChecked()) {
            lunchService = "有";
        }
        else {
            lunchService = "無";
        }
        _intent.putExtra("lunchService", lunchService);

        //禁煙席有りか無しか
        String nonSmokingSeat;
        if(rbNonSmokingSeatAvailable.isChecked()) {
            nonSmokingSeat = "有";
        }
        else {
            nonSmokingSeat = "無";
        }
        _intent.putExtra("nonSmokingSeat", nonSmokingSeat);

        //カード利用有りか無しか
        String cardUsage;
        if(rbCardUsageAvailable.isChecked()) {
            cardUsage = "有";
        }
        else {
            cardUsage = "無";
        }
        _intent.putExtra("cardUsage", cardUsage);

        //TODO:画像はどうすれば

        _intent.putExtra("password", etPassword.getText().toString());
        _intent.putExtra("no", etNo.getText().toString());
        _intent.putExtra("freeName", etFreeName.getText().toString());

        startActivity(_intent);
    }

    /**
     * 各部品の取得を行うメソッド
     */
    private void findViewAll() {
        etShopId = findViewById(R.id.etShopId);
        etShopName = findViewById(R.id.etShopName);
        etPhonetic = findViewById(R.id.etPhonetic);
        etOpenTime = findViewById(R.id.etOpenTime);
        etTel = findViewById(R.id.etTel);
        etAddress = findViewById(R.id.etAddress);
        etAverageBudget = findViewById(R.id.etAverageBudget);
        etPointLatitude = findViewById(R.id.etPointLatitude);
        etPointLongitude = findViewById(R.id.etPointLongitude);
        rbLunchServiceAvailable = findViewById(R.id.rbLunchServiceAvailable);
        rbLunchServiceNotAvailable = findViewById(R.id.rbLunchServiceNotAvailable);
        rbNonSmokingSeatAvailable = findViewById(R.id.rbNonSmokingSeatAvailable);
        rbNonSmokingSeatNotAvailable = findViewById(R.id.rbNonSmokingSeatNotAvailable);
        rbCardUsageAvailable = findViewById(R.id.rbCardUsageAvailable);
        rbCardUsageNotAvailable = findViewById(R.id.rbCardUsageNotAvailable);
        ivImage1 = findViewById(R.id.ivImage1);
        ivImage2 = findViewById(R.id.ivImage2);
        etPassword = findViewById(R.id.etPassword);
        etNo = findViewById(R.id.etNo);
        etFreeName = findViewById(R.id.etFreeName);
    }
}

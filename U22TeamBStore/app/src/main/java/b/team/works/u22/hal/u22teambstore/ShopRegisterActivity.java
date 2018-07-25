package b.team.works.u22.hal.u22teambstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register);

        //各部品取得
        findViewAll();


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

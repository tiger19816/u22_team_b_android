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
import android.widget.Toast;

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

        //画像
        _intent.putExtra("image1", "2018-07-24-00-00-01.jpg");
        _intent.putExtra("image2", "2018-07-24-00-00-02.jpg");

        _intent.putExtra("password", etPassword.getText().toString());
        _intent.putExtra("no", etNo.getText().toString());
        _intent.putExtra("freeName", etFreeName.getText().toString());

        //エラーがなければ次画面へ遷移する
       // if( !isErrorCheck() ) {
            startActivity(_intent);
       // }
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

    /**
     * 未入力チェックを行うメソッド
     */
    public Boolean isErrorCheck() {
        //エラーがあることを示すフラグ
        Boolean flag = false;

        if( "".equals( etShopId.getText().toString() ) ) {
            etShopId.setError("店舗IDを入力してください。");
            flag = true;
        }
        else if( "".equals( etShopName.getText().toString() ) ) {
            etShopName.setError("店舗名を入力してください。");
            flag = true;
        }
        else if( "".equals( etPhonetic.getText().toString() ) ) {
            etPhonetic.setError("店舗名（カナ）を入力してください。");
            flag = true;
        }
        else if( "".equals( etOpenTime.getText().toString() ) ) {
            etOpenTime.setError("営業時間を入力してください。");
            flag = true;
        }
        else if( "".equals( etTel.getText().toString() ) ) {
            etTel.setError("電話番号を入力してください。");
            flag = true;
        }
        else if( "".equals( etAddress.getText().toString() ) ) {
            etAddress.setError("住所を入力してください。");
            flag = true;
        }
        else if( "".equals( etAverageBudget.getText().toString() ) ) {
            etAverageBudget.setError("平均予算を入力してください。");
            flag = true;
        }
        else if( "".equals( etPointLatitude.getText().toString() ) ) {
            etPointLatitude.setError("緯度を入力してください。");
            flag = true;
        }
        else if( "".equals( etPointLongitude.getText().toString() ) ) {
            etPointLongitude.setError("経度を入力してください。");
            flag = true;
        }
        else if( "".equals( etPassword.getText().toString() ) ) {
            etPassword.setError("パスワードを入力してください。");
            flag = true;
        }
        else if( "".equals( etNo.getText().toString() ) ) {
            etNo.setError("項番を入力してください。");
            flag = true;
        }
        else if( "".equals( etFreeName.getText().toString() ) ) {
            etFreeName.setError("フリーワードを入力してください。");
            flag = true;
        }

        return flag;
    }
}

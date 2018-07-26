package b.team.works.u22.hal.u22teambstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShopRegisterConfirmActivity extends AppCompatActivity {

    //各部品取得用定数
    TextView tvShopId; //店舗ID
    TextView tvShopName; //店舗名
    TextView tvPhonetic; //店舗名（カナ）
    TextView tvOpenTime; //営業時間
    TextView tvTel; //電話番号
    TextView tvAddress; //住所
    TextView tvAverageBudget; //平均予算
    TextView tvPointLatitude; //緯度
    TextView tvPointLongitude; //経度
    TextView tvLunchService; //ランチ営業
    TextView tvNonSmokingSeat; //禁煙席
    TextView tvCardUsage; //カード利用
    TextView tvImage1; //店舗画像01
    TextView tvImage2; //店舗画像02
    TextView tvPassword; //パスワード
    TextView tvNo; //項番
    TextView tvFreeName; //フリーワード

    //取得した画面部品にセットされた値を格納する変数
    String shopId;
    String shopName;
    String phonetice;
    String openTime;
    String tel;
    String address;
    String averageBudget;
    String pointLatitude;
    String pointLongitude;
    String LunchService;
    String nonSmokingSeat;
    String cardUsage;
    String image1;
    String image2;
    String password;
    String no;
    String freeName;

    //インテントオブジェクト
    Intent _intent;

    //URL
    private static final String LOGIN_URL = "http://10.0.2.2:8080/u22_team_b_web/ShopRegisterServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register_confirm);

        _intent = getIntent();

        //画面部品取得
        findViewAll();

        //取得した画面部品に値をセット
        viewSet();


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
        //非同期処理を開始する。
        LoginTaskReceiver receiver = new LoginTaskReceiver();
        receiver.execute(LOGIN_URL, shopName);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス
     */
    private class LoginTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";

        /**
         * 非同期に処理したい内容を記述するメソッド
         *
         * @param params String型の配列。（可変長）
         * @return String型の結果JSONデータ。
         */
        @Override
        public String doInBackground(String... params) {
            String urlStr = params[0];
            String name = params[1];

            //POSTで送りたいデータ
            String postData = name;

            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();

                //GET通信かPOST通信かを指定する。
                con.setRequestMethod("POST");

                //自動リダイレクトを許可するかどうか。
                con.setInstanceFollowRedirects(false);

                //時間制限。（ミリ秒単位）
                con.setReadTimeout(10000);
                con.setConnectTimeout(20000);

                con.connect();

                //POSTデータ送信処理。InputStream処理よりも先に記述する。
                OutputStream os = null;
                try {
                    os = con.getOutputStream();

                    //送信する値をByteデータに変換する（UTF-8）
                    os.write(postData.getBytes("UTF-8"));
                    os.flush();
                } catch (IOException ex) {
                    Log.e(DEBUG_TAG, "POST送信エラー", ex);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException ex) {
                            Log.e(DEBUG_TAG, "OutputStream解放失敗", ex);
                        }
                    }
                }

                is = con.getInputStream();

                result = is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }

            return result;
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while (0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

    /**
     * インテントから取得した値を各画面部品にセットするメソッド
     */
    private void viewSet() {
        tvShopId.setText( _intent.getStringExtra("shopId") );
        tvShopName.setText( _intent.getStringExtra("shopName") );
        shopName = tvShopName.getText().toString();
        tvPhonetic.setText( _intent.getStringExtra("phonetic") );
        tvOpenTime.setText( _intent.getStringExtra("openTime") );
        tvTel.setText( _intent.getStringExtra("tel") );
        tvAddress.setText( _intent.getStringExtra("address") );
        tvAverageBudget.setText( _intent.getStringExtra("averageBudget") );
        tvPointLatitude.setText( _intent.getStringExtra("pointLatitude") );
        tvPointLongitude.setText( _intent.getStringExtra("pointLongitude") );
        tvLunchService.setText( _intent.getStringExtra("lunchService") );
        tvNonSmokingSeat.setText( _intent.getStringExtra("nonSmokingSeat") );
        tvCardUsage.setText( _intent.getStringExtra("cardUsage") );

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
        tvPhonetic = findViewById(R.id.tvPhonetic);
        tvOpenTime = findViewById(R.id.tvOpenTime);
        tvTel = findViewById(R.id.tvTel);
        tvAddress = findViewById(R.id.tvAddress);
        tvAverageBudget = findViewById(R.id.tvAverageBudget);
        tvPointLatitude = findViewById(R.id.tvPointLatitude);
        tvPointLongitude = findViewById(R.id.tvPointLongitude);
        tvLunchService = findViewById(R.id.tvLunchService);
        tvNonSmokingSeat = findViewById(R.id.tvNonSmokingSeat);
        tvCardUsage = findViewById(R.id.tvCardUsage);
        tvImage1 = findViewById(R.id.tvImage1);
        tvImage2 = findViewById(R.id.tvImage2);
        tvPassword = findViewById(R.id.tvPassword);
        tvNo = findViewById(R.id.tvNo);
        tvFreeName = findViewById(R.id.tvFreeName);
    }
}

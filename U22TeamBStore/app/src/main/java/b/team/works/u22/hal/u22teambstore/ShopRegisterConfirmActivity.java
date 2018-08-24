package b.team.works.u22.hal.u22teambstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
    String phonetic;
    String openTime;
    String tel;
    String address;
    String averageBudget;
    String pointLatitude;
    String pointLongitude;
    String lunchService;
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
    private static final String LOGIN_URL = "http://localhost:8080/u22_team_b_web/ShopRegisterServlet";

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
        //非同期処理を開始する。
        LoginTaskReceiver receiver = new LoginTaskReceiver();
        receiver.execute(
                LOGIN_URL,
                shopId,
                shopName,
                phonetic,
                openTime,
                tel,
                address,
                averageBudget,
                pointLatitude,
                pointLongitude,
                lunchService,
                nonSmokingSeat,
                cardUsage,
                image1,
                image2,
                password,
                no,
                freeName
        );
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
            String shopIdStr = params[1];
            String shopNameStr = params[2];
            String phoneticStr = params[3];
            String openTimeStr = params[4];
            String telStr = params[5];
            String addressStr = params[6];
            String averageBudgetStr = params[7];
            String pointLatitudeStr = params[8];
            String pointLongitudeStr = params[9];
            String lunchServiceStr = params[10];
            String nonSmokingSeatStr = params[11];
            String cardUsageStr = params[12];
            String passwordStr = params[13];
            String noStr = params[14];
            String freeNameStr = params[15];

            //POSTで送りたいデータ
            String postShopId = shopIdStr;
            String postShopName = shopNameStr;
            String postPhonetic = phoneticStr;
            String postOpenTime = openTimeStr;
            String postTel = telStr;
            String postAddress = addressStr;
            String postAverageBudget = averageBudgetStr;
            String postPointLatitude = pointLatitudeStr;
            String postPointLongitude = pointLongitudeStr;
            String postLunchService = lunchServiceStr;
            String postNonSmokingSeat = nonSmokingSeatStr;
            String postCardUsage = cardUsageStr;
            String postPassword = passwordStr;
            String postNo = noStr;
            String postFreeName = freeNameStr;

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
                    os.write(postShopId.getBytes("UTF-8"));
                    os.write(postShopName.getBytes("UTF-8"));
                    os.write(postPhonetic.getBytes("UTF-8"));
                    os.write(postOpenTime.getBytes("UTF-8"));
                    os.write(postTel.getBytes("UTF-8"));
                    os.write(postAddress.getBytes("UTF-8"));
                    os.write(postAverageBudget.getBytes("UTF-8"));
                    os.write(postPointLatitude.getBytes("UTF-8"));
                    os.write(postPointLongitude.getBytes("UTF-8"));
                    os.write(postLunchService.getBytes("UTF-8"));
                    os.write(postNonSmokingSeat.getBytes("UTF-8"));
                    os.write(postCardUsage.getBytes("UTF-8"));
                    os.write(postPassword.getBytes("UTF-8"));
                    os.write(postNo.getBytes("UTF-8"));
                    os.write(postFreeName.getBytes("UTF-8"));

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

        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject rootJSON = new JSONObject(result);

                boolean resultJSON  = rootJSON.getBoolean("result");
                if(resultJSON) {
                    Toast.makeText(ShopRegisterConfirmActivity.this, "登録が完了しました。", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ShopRegisterConfirmActivity.this, "登録に失敗しました。", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
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
     * インテントから取得した値を各画面部品にセット＋変数へ格納するメソッド
     */
    private void viewSet() {
        //店舗ID
        tvShopId.setText( _intent.getStringExtra("shopId") );
        shopId = tvShopId.getText().toString();

        //店舗名
        tvShopName.setText( _intent.getStringExtra("shopName") );
        shopName = tvShopName.getText().toString();

        //店舗名（カナ）
        tvPhonetic.setText( _intent.getStringExtra("phonetic") );
        phonetic = tvPhonetic.getText().toString();

        //営業時間
        tvOpenTime.setText( _intent.getStringExtra("openTime") );
        openTime = tvOpenTime.getText().toString();

        //電話番号
        tvTel.setText( _intent.getStringExtra("tel") );
        tel = tvTel.getText().toString();

        //住所
        tvAddress.setText( _intent.getStringExtra("address") );
        address = tvAddress.getText().toString();

        //平均予算
        tvAverageBudget.setText( _intent.getStringExtra("averageBudget") );
        averageBudget = tvAverageBudget.getText().toString();

        //緯度
        tvPointLatitude.setText( _intent.getStringExtra("pointLatitude") );
        pointLatitude = tvPointLatitude.getText().toString();

        //経度
        tvPointLongitude.setText( _intent.getStringExtra("pointLongitude") );
        pointLongitude = tvPointLongitude.getText().toString();

        //ランチ営業
        tvLunchService.setText( _intent.getStringExtra("lunchService") );
        lunchService = tvLunchService.getText().toString();

        //禁煙席
        tvNonSmokingSeat.setText( _intent.getStringExtra("nonSmokingSeat") );
        nonSmokingSeat = tvNonSmokingSeat.getText().toString();

        //カード利用
        tvCardUsage.setText( _intent.getStringExtra("cardUsage") );
        cardUsage = tvCardUsage.getText().toString();

        //画像（※ファイルパスを想定してます）
        tvImage1.setText( _intent.getStringExtra("image1") );
        image1 = tvImage1.getText().toString();
        tvImage2.setText( _intent.getStringExtra("image2") );
        image2 = tvImage2.getText().toString();

        //パスワード
        tvPassword.setText( _intent.getStringExtra("password") );
        password = tvPassword.getText().toString();

        //項番
        tvNo.setText( _intent.getStringExtra("no") );
        no = tvNo.getText().toString();

        //フリーワード
        tvFreeName.setText( _intent.getStringExtra("freeName") );
        freeName = tvFreeName.getText().toString();
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

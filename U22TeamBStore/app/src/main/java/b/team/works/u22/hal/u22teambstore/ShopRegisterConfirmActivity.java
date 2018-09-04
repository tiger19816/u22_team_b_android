package b.team.works.u22.hal.u22teambstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
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
import java.util.List;

public class ShopRegisterConfirmActivity extends AppCompatActivity {

    private String _id;

    //各部品取得用定数
    TextView tvShopName; //店舗名
    TextView tvPhonetic; //店舗名（カナ）
    TextView tvOpenTime; //営業時間
    TextView tvTel; //電話番号
    TextView tvAddress; //住所
    TextView tvAverageBudget; //平均予算
    TextView tvLunchService; //ランチ営業
    TextView tvNonSmokingSeat; //禁煙席
    TextView tvCardUsage; //カード利用
    TextView tvImage1; //店舗画像01
    TextView tvImage2; //店舗画像02
    TextView tvPassword; //パスワード
    TextView tvFreeName; //フリーワード

    //取得した画面部品にセットされた値を格納する変数
    String shopName;
    String phonetic;
    String openTime;
    String tel;
    String address;
    String averageBudget;
    String lunchService;
    String nonSmokingSeat;
    String cardUsage;
    String image1;
    String image2;
    String password;
    String freeName;
    String latitude;
    String longitude;

    //インテントオブジェクト
    Intent _intent;

    //URL
    private static final String LOGIN_URL = Word.SHOP_URL;

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
     * 送信ボタンが押されたとき
     */
    public void onClickSend(View view) {
        //非同期処理を開始する。
        UserInformationTaskReceiver receiver = new UserInformationTaskReceiver();
        receiver.execute(LOGIN_URL);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class UserInformationTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";

        /**
         * 非同期に処理したい内容を記述するメソッド.
         * このメソッドは必ず実装する必要がある。
         *
         * @param params String型の配列。（可変長）
         * @return String型の結果JSONデータ。
         */
        @Override
        public String doInBackground(String... params) {
            String urlStr = params[0];

            //POSTで送りたいデータ
            String postData = "postPassword="+password+"&postShopName="+shopName+"&postPhonetic="+phonetic
                    +"&postOpenTime="+openTime+"&postTel="+tel+"&postAddress="+address
                    +"&postAverageBudget="+averageBudget+"&postPointLatitude="+latitude+"&postLongitude="+longitude+"&postLunchService="+lunchService+"&postNonSmokingSeat="+nonSmokingSeat
                    +"&postCardUsage="+cardUsage+"&image1="+"image01.jpg"+"&image2="+"image02.jpg"+"&postFreeName="+freeName;


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
                }
                catch (IOException ex) {
                    Log.e(DEBUG_TAG, "POST送信エラー", ex);
                }
                finally {
                    if(os != null) {
                        try {
                            os.close();
                        }
                        catch (IOException ex) {
                            Log.e(DEBUG_TAG, "OutputStream解放失敗", ex);
                        }
                    }
                }

                is = con.getInputStream();

                result = is2String(is);
            }
            catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            }
            catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            }
            finally {
                if(con != null) {
                    con.disconnect();
                }
                if(is != null) {
                    try {
                        is.close();
                    }
                    catch (IOException ex) {
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
                _id  = rootJSON.getString("shop_id");

                if(resultJSON) {
                    //DBチェックの結果により、画面遷移先を変更。
                    SharedPreferences setting = getSharedPreferences("SHOPUSER" , 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("ID" , _id);
                    editor.commit();

                    FullDialogFragment2 dialog = new FullDialogFragment2();
                    // ダイアログに値を渡す
                    Bundle bundle = new Bundle();
                    // キーと値の順で渡す
                    bundle.putString("id", _id);
                    bundle.putString("name", shopName);
                    // 値をdialogにセット
                    dialog.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    dialog.show(manager,"FullDialogFragment2");
                }
                else {
                    Toast.makeText(ShopRegisterConfirmActivity.this, getString(R.string.register_failure_warning), Toast.LENGTH_SHORT).show();
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
        DataConversion dc = new DataConversion();
        List<String> listAddress = dc.getLatLongFromAddress(this,address);
        //緯度経度
        latitude = listAddress.get(0);
        longitude = listAddress.get(1);

        //平均予算
        tvAverageBudget.setText( _intent.getStringExtra("averageBudget") );
        averageBudget = tvAverageBudget.getText().toString();

        //ランチ営業
        tvLunchService.setText( _intent.getStringExtra("lunchService") );
        lunchService = tvLunchService.getText().toString();
        if("有".equals(lunchService)){
            lunchService = "1";
        }else{
            lunchService = "0";
        }

        //禁煙席
        tvNonSmokingSeat.setText( _intent.getStringExtra("nonSmokingSeat") );
        nonSmokingSeat = tvNonSmokingSeat.getText().toString();
        if("有".equals(nonSmokingSeat)){
            nonSmokingSeat = "1";
        }else{
            nonSmokingSeat = "0";
        }

        //カード利用
        tvCardUsage.setText( _intent.getStringExtra("cardUsage") );
        cardUsage = tvCardUsage.getText().toString();
        if("有".equals(cardUsage)){
            cardUsage = "1";
        }else{
            cardUsage = "0";
        }

        //画像（※ファイルパスを想定してます）
        tvImage1.setText( _intent.getStringExtra("image1") );
        image1 = tvImage1.getText().toString();
        tvImage2.setText( _intent.getStringExtra("image2") );
        image2 = tvImage2.getText().toString();

        //パスワード
        tvPassword.setText( _intent.getStringExtra("password") );
        password = tvPassword.getText().toString();

        //フリーワード
        tvFreeName.setText( _intent.getStringExtra("freeName") );
        freeName = tvFreeName.getText().toString();
    }

    /**
     * 各部品の取得を行うメソッド
     */
    private void findViewAll() {
        tvShopName = findViewById(R.id.tvShopName);
        tvPhonetic = findViewById(R.id.tvPhonetic);
        tvOpenTime = findViewById(R.id.tvOpenTime);
        tvTel = findViewById(R.id.tvTel);
        tvAddress = findViewById(R.id.tvAddress);
        tvAverageBudget = findViewById(R.id.tvAverageBudget);
        tvLunchService = findViewById(R.id.tvLunchService);
        tvNonSmokingSeat = findViewById(R.id.tvNonSmokingSeat);
        tvCardUsage = findViewById(R.id.tvCardUsage);
        tvImage1 = findViewById(R.id.tvImage1);
        tvImage2 = findViewById(R.id.tvImage2);
        tvPassword = findViewById(R.id.tvPassword);
        tvFreeName = findViewById(R.id.tvFreeName);
    }
}

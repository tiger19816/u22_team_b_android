package b.team.works.u22.hal.u22teambstore;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * #35 予約リストを表示し、選択するとQRコードリーダが起動するアクティビティ。
 *
 * @author Yuki Yoshida
 */
public class ReservationListActivity extends AppCompatActivity {
    /**
     * ListViewのフィールド。
     */
    private ListView _lvReservationList;

    /**
     * QRコードリーダで読み取った夫IDの保存フィールド。
     */
    private String _maleId;

    /**
     * 受け取ったJSONの保存フィールド。
     */
    private JSONObject _receivedObject;

    /**
     * onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        this._lvReservationList = (ListView) findViewById(R.id.lvReservationList);
        onResume();
    }

    /**
     * onResume
     */
    @Override
    protected void onResume() {
        super.onResume();

        // JSONを受け取り、ListViewに表示。
    }

    /**
     * オプションメニュー設定。
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    /**
     * QRコードリーダの起動。
     */
    public void onClickQr(MenuItem item) {
        new IntentIntegrator(ReservationListActivity.this).initiateScan();
    }

    /**
     * QRコードデータの読み取り。
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String strData = "";
        if (null != result) {
            if (null != result.getContents()) {
                strData = result.getContents().toString();
                Log.d("QRコード", "読み取った値:" + strData);
                this._maleId = strData;
                HttpResponseAsync hra = new HttpResponseAsync(this);
                hra.execute(this._maleId);   // パラメータをサーブレットへ送信。
            } else {
                // 戻るが押されたときの操作。
                Log.i("QRコード", "読取取消");
                this.onResume();
            }
        } else {
            Log.e("QRコード", "読取失敗");
            super.onActivityResult(requestCode, resultCode, data);
            this.onResume();
        }
    }

    /**
     * サーブレットへパラメータを送信する
     */
    public class HttpResponseAsync extends AsyncTask<String, Void, Void> {

        private Activity _activity;

        /**
         * コンストラクタ。
         *
         * @param activity 遷移元アクティビティ。
         */
        public HttpResponseAsync(Activity activity) {

            this._activity = activity;

        }

        /**
         * サーブレットへパラメータを送信するメソッド。
         *
         * @param maleId ①夫ID。
         * @return null
         */
        @Override
        protected Void doInBackground(String... maleId) {
            HttpURLConnection cnct = null;
            URL url = null;
            OutputStream opStream = null;
            String urlStr = "http://10.0.2.2:8080/team_b_web/maleIdReceiveServlet";   // 接続先URL

            String postData = "male_id=" + maleId[0];  // POSTで送りたいデータ

//            Log.d("URL", "アクセス先: " + urlStr + "?" + postData);

            try {
                url = new URL(urlStr);  // URLの作成
                cnct = (HttpURLConnection) url.openConnection(); // 接続用HttpURLConnectionオブジェクト作成
                cnct.setRequestMethod("POST");  // リクエストメソッドの設定
                cnct.setInstanceFollowRedirects(false); // リダイレクトの可否
                cnct.setDoOutput(true); // データ書き込みの可否
                cnct.setReadTimeout(10000); // タイムアウトまでの時間（読込）
                cnct.setConnectTimeout(20000);  // タイムアウトまでの時間（接続）
                cnct.setUseCaches(false);   // キャッシュ使用の可否

                cnct.connect(); // 接続

                try {
                    opStream = cnct.getOutputStream();

                    // 送信する値をByteデータに変換する（UTF-8）
                    opStream.write(postData.getBytes("UTF-8"));
                    opStream.flush();

                    switch (cnct.getResponseCode()) {
                        case HttpURLConnection.HTTP_OK:
                            Log.d("URL", "コネクション状況: 成功");
                            break;
                        case HttpURLConnection.HTTP_INTERNAL_ERROR:
                            Log.e("URL", "エラー内容: 500 内部サーバーエラー");
                            break;
                        default:
                            Log.e("URL", "コネクションレスポンスコード: " + cnct.getResponseCode());
                            break;
                    }
                } catch (Exception e) {
                    Log.e("URL", "POST送信エラー: " + e.toString());
                } finally {

                    if (opStream != null) {
                        try {
                            opStream.close();
                        } catch (Exception e) {
                            Log.e("URL", "OutputStream解放失敗: " + e.toString());
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("URL", "コネクションエラー: " + e.toString());
            } finally {
                if (cnct != null) {
                    cnct.disconnect();
                }
            }

            return null;
        }

        /**
         * サーブレットへアクセスした後に実行されるメソッド。
         */
        @Override
        public void onPostExecute(Void param) {
            Log.d("HTTP", "onPostExecute: 通過");
        }
    }
}

///**
// * 編集中。予約情報を送信し、夫のIDをを受信するクラス。
// */
//public class MaleIdReceiver extends AsyncTask<String, Void, String> {
//    /**
//     * ログに記載するタグ用の文字列。
//     */
//    private static final String DEBUG_TAG = "パラメータ送信";
//    private static final String _urlStr = "http://10.0.2.2:8080/u22_team_b_web/ReservationListFromShopSenderServlet";
//
//    @Override
//    public String doInBackground(String... params) {
//        String reservationId = params[0];
//
//        HttpURLConnection con = null;
//        OutputStream os = null;
//        InputStream is = null;
//        String result = "";
//
//        try {
//            URL url = new URL(this._urlStr);
//            con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.connect();
//            is = con.getInputStream();
//
////            result = is2String(is);
//        } catch (MalformedURLException ex) {
//            Log.e(DEBUG_TAG, "URL変換失敗", ex);
//        } catch (IOException ex) {
//            Log.e(DEBUG_TAG, "通信失敗", ex);
//        } finally {
//            if (con != null) {
//                con.disconnect();
//            }
//
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException ex) {
//                    Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    @Override
//    public void onPostExecute(String result) {
////        String title = "";
////        String text = "";
////        String dateLabel = "";
////        String telop = "";
////        try {
////            JSONObject rootJSON = new JSONObject(result);
////            title = rootJSON.getString("title");
////            JSONObject descriptionJSON = rootJSON.getJSONObject("description");
////            text = descriptionJSON.getString("text");
////            JSONArray forecasts = rootJSON.getJSONArray("forecasts");
////            JSONObject forecastNow = forecasts.getJSONObject(0);
////            dateLabel = forecastNow.getString("dateLabel");
////            telop = forecastNow.getString("telop");
////        } catch (JSON Exception ex) {
////            Log.e(DEBUG_TAG, "JSON解析失敗", ex);
////        }
////
////        String msg = dateLabel + "の天気: " + telop + "\n" + text;
////
////        WeatherInfoDialog dialog = new WeatherInfoDialog();
////        Bundle extras = new Bundle();
////        extras.putString("title", title);
////        extras.putString("msg", msg);
////        dialog.setArguments(extras);
////        FragmentManager manager = getSupportFragmentManager();
////        dialog.show(manager, "WeatherInfoDialog");
//    }
//}

package b.team.works.u22.hal.u22teambstore;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 遷移元から受け取った店IDを格納するフィールド。
     */
    private String _shopId;

    /**
     * QRコードリーダで読み取った夫IDの保存フィールド。
     */
    private String _maleId;

    /**
     * 受け取ったJSONの保存フィールド。
     */
    private JSONObject _receivedObject;

    /**
     * 受け取ったJSONの保存フィールド。
     */
    private JSONArray _reservationList;

    /**
     * onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

//        this._shopId = getIntent().getStringExtra("shopId");
        this._shopId = "7116760";   // デバッグ用。

        this._lvReservationList = (ListView) findViewById(R.id.lvReservationList);
        onResume();
    }

    /**
     * onResume
     */
    @Override
    protected void onResume() {
        super.onResume();

        // JSONを受け取る。
        GetListAsync gla = new GetListAsync(ReservationListActivity.this);
        gla.execute(this._shopId);

        // ListViewに表示。
        ListView lvReservationList = findViewById(R.id.lvReservationList);

        String[] from = new String[]{"name", "date"};
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            for (int i = 0; i < this._reservationList.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", this._reservationList.getJSONObject(i).getString("maleName"));
                String strUseDateTime = "";
                JSONObject aryUseDateTime = this._reservationList.getJSONObject(i).getJSONObject("use_date_time");
                strUseDateTime += aryUseDateTime.getString("year");
                strUseDateTime += "年";  // TODO:strings.xmlに対応。
                strUseDateTime += aryUseDateTime.getString("month");
                strUseDateTime += "月";
                strUseDateTime += aryUseDateTime.getString("day");
                strUseDateTime += "日";
                strUseDateTime += " ";
                strUseDateTime += aryUseDateTime.getString("hour");
                strUseDateTime += "時";
                strUseDateTime += aryUseDateTime.getString("minute");
                strUseDateTime += "分";
                map.put("date", strUseDateTime);
                list.add(map);
            }
        } catch (JSONException e) {
            Log.e("JSON", e.toString());
        }
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        SimpleAdapter adapter = new SimpleAdapter(ReservationListActivity.this, list, android.R.layout.simple_list_item_2, from, to);
        lvReservationList.setAdapter(adapter);
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
     * リストを取得する。
     */
    public class GetListAsync extends AsyncTask<String, Void, JSONArray> {

        private Activity _activity;

        /**
         * コンストラクタ。
         *
         * @param activity 実行元アクティビティ。
         */
        public GetListAsync(Activity activity) {

            this._activity = activity;

        }

        /**
         * サーブレットへパラメータを送信するメソッド。
         *
         * @param shopId ①店ID。
         * @return null
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected JSONArray doInBackground(String... shopId) {
            HttpURLConnection cnct = null;
            URL url = null;
            OutputStream opStream = null;
            String urlStr = "http://10.0.2.2:8080/team_b_web/SendReservationListServlet";   // 接続先URL

            String postData = "shop_id=" + shopId[0];  // POSTで送りたいデータ

            JSONArray result = null;

            InputStream inStream = null;
            InputStreamReader inReader = null;
            BufferedReader bufReader = null;

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

                    // 送信する値をByteデータに変換する。（UTF-8）
                    opStream.write(postData.getBytes("UTF-8"));
                    opStream.flush();

                    // エラーコードの取得。
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

                    // JSON文字列を取得。
                    StringBuilder resultBuf = null;
                    //responseの読み込み。
                    inStream = cnct.getInputStream();
                    String encoding = cnct.getContentEncoding();
                    inReader = new InputStreamReader(inStream, encoding);
                    bufReader = new BufferedReader(inReader);
                    String line = null;
                    line = bufReader.readLine();
                    while(line != null) {
                        resultBuf.append(line);
                    }
                    String strResult = resultBuf.toString();
                    result = new JSONArray(new JSONObject(strResult));

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

                    if (bufReader != null) {
                        try {
                            bufReader.close();
                        } catch (Exception e) {
                            Log.e("JSON", "InputStream解放失敗: " + e.toString());
                        }
                    }
                    if (inReader != null) {
                        try {
                            inReader.close();
                        } catch (Exception e) {
                            Log.e("JSON", "InputStream解放失敗: " + e.toString());
                        }
                    }
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (Exception e) {
                            Log.e("JSON", "InputStream解放失敗: " + e.toString());
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

            return result;
        }

        /**
         * サーブレットへアクセスした後に実行されるメソッド。
         */
        @Override
        public void onPostExecute(JSONArray param) {
            Log.i("HTTP", "onPostExecute: 通過");

            _reservationList = param;
//            onResume();
//            finish();
        }
    }

    /**
     * サーブレットへパラメータを送信する
     */
    public class HttpResponseAsync extends AsyncTask<String, Void, JSONObject> {

        private Activity _activity;

        /**
         * コンストラクタ。
         *
         * @param activity 実行元アクティビティ。
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
        protected JSONObject doInBackground(String... maleId) {
            HttpURLConnection cnct = null;
            URL url = null;
            OutputStream opStream = null;
            String urlStr = "http://10.0.2.2:8080/team_b_web/sendReceivationDetailServlet";   // 接続先URL

            String postData = "male_id=" + maleId[0];  // POSTで送りたいデータ

            JSONObject result = null;

            InputStream inStream = null;
            InputStreamReader inReader = null;
            BufferedReader bufReader = null;

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

                    // 送信する値をByteデータに変換する。（UTF-8）
                    opStream.write(postData.getBytes("UTF-8"));
                    opStream.flush();

                    // エラーコードの取得。
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

                    // JSON文字列を取得。
                    StringBuilder resultBuf = null;
                    //responseの読み込み。
                    inStream = cnct.getInputStream();
                    String encoding = cnct.getContentEncoding();
                    inReader = new InputStreamReader(inStream, encoding);
                    bufReader = new BufferedReader(inReader);
                    String line = null;
                    line = bufReader.readLine();
                    while(line != null) {
                        resultBuf.append(line);
                    }
                    String strResult = resultBuf.toString();
                    result = new JSONObject(strResult);

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

                    if (bufReader != null) {
                        try {
                            bufReader.close();
                        } catch (Exception e) {
                            Log.e("JSON", "InputStream解放失敗: " + e.toString());
                        }
                    }
                    if (inReader != null) {
                        try {
                            inReader.close();
                        } catch (Exception e) {
                            Log.e("JSON", "InputStream解放失敗: " + e.toString());
                        }
                    }
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (Exception e) {
                            Log.e("JSON", "InputStream解放失敗: " + e.toString());
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

            return result;
        }

        /**
         * サーブレットへアクセスした後に実行されるメソッド。
         */
        @Override
        public void onPostExecute(JSONObject param) {
            Log.i("HTTP", "onPostExecute: 通過");

            Intent intent = new Intent(getApplicationContext(), ReservationDetailActivity.class);
            intent.putExtra("jsonParam", param.toString());
            startActivity(intent);
//            onResume();
//            finish();
        }
    }
}

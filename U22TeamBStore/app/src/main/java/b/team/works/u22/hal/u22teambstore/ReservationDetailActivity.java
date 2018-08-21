package b.team.works.u22.hal.u22teambstore;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReservationDetailActivity extends AppCompatActivity {
    /**
     * 予約IDを格納するフィールド。
     */
    private String _reservationId;
    /**
     * 予約者の氏名を格納し、表示するフィールド。
     */
    TextView _tvFemaleName;
    /**
     * 予約された来店者の氏名を格納し、表示するフィールド。
     */
    TextView _tvMaleName;
    /**
     * 予約されたメニューを格納し、表示するフィールド。
     */
    TextView _tvMenu;
    /**
     * 予約された来店予定時刻を格納し、表示するフィールド。
     */
    TextView _tvUseDateTime;

    /**
     * 取得したJSONを格納するフィールド。
     */
    JSONObject _objReservation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        this._tvFemaleName = findViewById(R.id.tvFemaleName);
        this._tvMaleName = findViewById(R.id.tvMaleName);
        this._tvMenu = findViewById(R.id.tvMenu);
        this._tvUseDateTime = findViewById(R.id.tvUseDateTime);

        try {
            this._objReservation = new JSONObject(getIntent().getStringExtra("param"));

            this._reservationId = this._objReservation.getString("reservation_id");
            this._tvFemaleName.setText(this._objReservation.getString("female_name"));
            this._tvMaleName.setText(this._objReservation.getString("male_name"));
            String strMenu = "";
            JSONArray aryMenu = this._objReservation.getJSONArray("menu");
            for (int i = 0; i < aryMenu.length(); i++) {
                 strMenu += aryMenu.getString(i);   // TODO:メニュー名をstrings.xmlから取得。
                 if (i != ( aryMenu.length() - 1 ) ) {
                     strMenu += "\n";
                 }
            }
            this._tvMenu.setText(strMenu);
            String strUseDateTime = "";
            JSONObject aryUseDateTime =this._objReservation.getJSONObject("use_date_time");
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
            this._tvUseDateTime.setText(strUseDateTime);
        } catch (JSONException e) {
            Log.e("JSON", e.toString());
        }

    }

    // ボタンクリック時。
    public void onClickVisited(View view) {
        new AlertDialog.Builder(ReservationDetailActivity.this)
                .setMessage("よろしいですか?")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HttpResponseAsync hra = new ReservationDetailActivity.HttpResponseAsync(ReservationDetailActivity.this);
                        hra.execute(ReservationDetailActivity.this._reservationId);
                    }
                })
                .setNegativeButton("いいえ", null)
                .show();
    }

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
         * @param reservationId ①来店フラグを更新したいレコードの予約ID
         * @return null
         */
        @Override
        protected Void doInBackground(String... reservationId) {
            HttpURLConnection cnct = null;
            URL url = null;
            OutputStream opStream = null;
            String urlStr = "http://10.0.2.2:8080/team_b_web/UpdateVisitFlagServlet";   // 接続先URL

            String postData = "reservation_id=" + reservationId[0];  // POSTで送りたいデータ

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
                        case HttpURLConnection.HTTP_OK :
                            Log.d("URL", "コネクション状況: 成功");
                            break;
                        case HttpURLConnection.HTTP_INTERNAL_ERROR:
                            Log.e("URL", "エラー内容: 500 内部サーバーエラー");
                            break;
                        default:
                            Log.e("URL", "コネクションレスポンスコード: " + cnct.getResponseCode());
                            break;
                    }
                }
                catch (Exception e) {
                    Log.e("URL", "POST送信エラー: " + e.toString());
                }
                finally {

                    if(opStream != null) {
                        try {
                            opStream.close();
                        }
                        catch (Exception e) {
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
            String msg = "来店処理が完了しました。";
            Toast.makeText(ReservationDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}

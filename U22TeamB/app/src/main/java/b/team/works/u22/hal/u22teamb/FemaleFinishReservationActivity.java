package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FemaleFinishReservationActivity extends AppCompatActivity {

    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_finish_reservation);

        setTitle("予約内容確認");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Intentからの取得
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        TextView tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreName.setText(reservation.getName());

        TextView tvMenu = findViewById(R.id.tvMenu);
        tvMenu.setText(getResources().obtainTypedArray(R.array.sp_reservation_store_menu_list).getString(Integer.valueOf(reservation.getMenuNo())));

        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        tvSubtotal.setText("650");

        TextView tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText("650");

        TextView tvCardNo = findViewById(R.id.tvCardNo);
        tvCardNo.setText("XXXX-XXXX-XXXX-1234");

        TextView tvCardname = findViewById(R.id.tvCardName);
        tvCardname.setText("TARO YAMADA");

        TextView tvCardDate = findViewById(R.id.tvCardDate);
        tvCardDate.setText("08/20");
    }

    public void onNextReservationListClick(View view){

        //非同期処理を開始する。
        FemaleFinishReservationActivity.ReservationTaskReceiver receiver = new FemaleFinishReservationActivity.ReservationTaskReceiver();

        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(Word.RESERVATION_URL);

        Intent intent = new Intent(FemaleFinishReservationActivity.this , FemaleStoreMapListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * アクションバーの戻るボタン用。
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 非同期通信を行い画像を取得するAsyncTaskクラスを継承したメンバクラス.
     */
    private class ReservationTaskReceiver extends AsyncTask<String, Void, String> {

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

            HttpURLConnection con = null;
            InputStream is = null;
            String result = null;
            String postData = "shops_id=" + reservation.getId() + "&shop_name=" + reservation.getName() + "&menu_no=" + reservation.getMenuNo() + "&female_id=" + 1 + "&use_date_time=" + reservation.getDataConversion();

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

                con.setDoOutput(true);

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

                result = Tools.is2String(is);
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
        }
    }
}

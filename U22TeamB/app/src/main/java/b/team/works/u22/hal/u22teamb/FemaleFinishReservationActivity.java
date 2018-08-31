package b.team.works.u22.hal.u22teamb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FemaleFinishReservationActivity extends AppCompatActivity {

    private Reservation reservation;
    private String _id;

    private Context context;
    private String toastMessage;

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

        //ユーザーIDの取得。
        SharedPreferences setting = getSharedPreferences("USER" , 0);
        _id = setting.getString("ID" , "");

        //Intentからの取得
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        TextView tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreName.setText(reservation.getName());

        TextView tvMenu = findViewById(R.id.tvMenu);
        tvMenu.setText(getResources().obtainTypedArray(R.array.sp_reservation_store_menu_list).getString(Integer.valueOf(reservation.getMenuNo())));

        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(reservation.getDate());

        TextView tvTime = findViewById(R.id.tvTime);
        tvTime.setText(reservation.getTime());

        int price = 0;
        if(Integer.parseInt(reservation.getMenuNo()) != 0){
            price = Integer.parseInt(reservation.getMenuNo())-1;
        }

        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        tvSubtotal.setText(getResources().getStringArray(R.array.sp_reservation_store_menu_price_list)[price]);

        TextView tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(getResources().getStringArray(R.array.sp_reservation_store_menu_price_list)[price]);

        //非同期処理を開始する。
        CardInformationTaskReceiver receiver = new CardInformationTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(Word.RESERVATION_CARD_URL);

        if(!"".equals(reservation.getMessage())) {
            TextView tvMessage = findViewById(R.id.tvMessage);
            tvMessage.setText(reservation.getMessage());
        }
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class CardInformationTaskReceiver extends AsyncTask<String, Void, String> {

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
            String postData = "femaleId=" + _id;

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
                String cardNo = rootJSON.getString("cardNo");
                String cardName = rootJSON.getString("cardName");
                String cardDate = rootJSON.getString("cardDate");
                String cardCode = rootJSON.getString("cardCode");

                TextView tvCardNo = findViewById(R.id.tvCardNo);
                tvCardNo.setText(cardNo);

                TextView tvCardname = findViewById(R.id.tvCardName);
                tvCardname.setText(cardName);

                TextView tvCardDate = findViewById(R.id.tvCardDate);
                tvCardDate.setText(cardDate);

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

    public void onNextReservationListClick(View view){

        //非同期処理を開始する。
        ReservationTaskReceiver receiver = new ReservationTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(Word.RESERVATION_URL);

        // ImageViewをToast表示する
        ImageView imageView = new ImageView(FemaleFinishReservationActivity.this);
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.female_gif).into(target);
        Toast toast = new Toast(FemaleFinishReservationActivity.this);
        toast.setView(imageView);
        /**
         * 第1引数 : 水平方向のマージンを指定します。数値は-1~1の間で設定できます。正の値は水平右方向、負の値は水平左方向に移動されます。
         * 第2引数 : 垂直方向のマージンを指定します。数値は-1~1の間で設定できます。正の値は垂直上方向、負の値は垂直下方向に移動されます。
         */
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 0);
        //時間
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // ここに３秒後に実行したい処理。
                Intent intent = new Intent(FemaleFinishReservationActivity.this , FemaleStoreMapListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 4000);
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
            String postData = "shops_id=" + reservation.getId() + "&shop_name=" + reservation.getName() + "&menu_no=" + reservation.getMenuNo() + "&female_id=" +  _id + "&use_date_time=" + reservation.getDataConversion() + "&message=" + reservation.getMessage();

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

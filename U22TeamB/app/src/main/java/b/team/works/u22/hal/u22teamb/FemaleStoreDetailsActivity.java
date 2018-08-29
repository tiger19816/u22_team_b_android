package b.team.works.u22.hal.u22teamb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【妻】店の詳細画面のアクティビティクラス.
 *
 * @author Taiga Hirai
 */
public class FemaleStoreDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, StoreDetailsTab1Fragment.OnFragmentInteractionListener, StoreDetailsTab2Fragment.OnFragmentInteractionListener, OnMapReadyCallback {

    public static final int MODE_FEMALE = 0;
    public static final int MODE_MALE = 1;

    public String storeId;
    public GoogleMap mMap;
    public Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //IDの取得
        Intent intent = getIntent();
        storeId = intent.getStringExtra("id");
        int mode = intent.getIntExtra("mode", 0);
        if(mode == MODE_FEMALE) {
            setTheme(R.style.MyCustomTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_store_details);

        setTitle("店舗詳細");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //戻るボタンの表示。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(mode == MODE_MALE) {
            Button btNextReservation = findViewById(R.id.btNextReservation);
            btNextReservation.setVisibility(View.GONE);
        }

        //xmlからTabLayoutの取得
        TabLayout tabLayout = findViewById(R.id.tabs);

        //xmlからViewPagerを取得
        ViewPager viewPager = findViewById(R.id.pager);

        //ページタイトル配列
        final String[] pageTitle = {getString(R.string.tab_title_detail), getString(R.string.tab_title_map)};

        //表示Pageに必要な項目を設定
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            /**
             * Tabの内を売を設定するメソッド.
             * switch文で設定するFragmentを分けている。
             *
             * @param position 設定するTabのページ番号(0から)。
             * @return Tabページに設定するFragment。
             */
            @Override
            public Fragment getItem(int position) {
                //各タブに設定するFragmentを選択する。
                switch (position) {
                    case 0:
                        return StoreDetailsTab1Fragment.newInstance(position + 1);
                    case 1:
                        return StoreDetailsTab2Fragment.newInstance(position + 1);
                    default:
                        return StoreDetailsTab1Fragment.newInstance(position + 1);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageTitle[position];
            }

            @Override
            public int getCount() {
                return pageTitle.length;
            }
        };
        //ViewPagerにページを設定
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        //ViewPagerをTabLayoutを設定
        tabLayout.setupWithViewPager(viewPager);

        //非同期処理を開始する。
        FemaleStoreDetailsActivity.StoreDetailsTaskReceiver receiver = new FemaleStoreDetailsActivity.StoreDetailsTaskReceiver();

        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(Word.STORE_DETAILS_URL);
    }

    /**
     *予約へのボタンがクリックされた時。
     * @param view
     */
    public void onNextReservationClick(View view){
        Intent intent = new Intent(FemaleStoreDetailsActivity.this,FemaleNewReservationActivity.class);
        intent.putExtra("id", map.get("id"));
        intent.putExtra("name", map.get("name"));
        startActivity(intent);
    }

    /**
     * オプションメニューのアイテムが押された時.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
//        LatLng latLng = new LatLng(34.699886, 135.493033);
//        mMap.addMarker(new MarkerOptions().position(latLng).title("HAL大阪").visible(true)).showInfoWindow();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    //以下の内容を実装する必要がある。（空で問題ない）

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class StoreDetailsTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";
        private ProgressDialog _pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // プログレスダイアログの生成。
            _pDialog = new ProgressDialog(FemaleStoreDetailsActivity.this);
            _pDialog.setMessage(getString(R.string.progress_message));  // メッセージを設定。

            // プログレスダイアログの表示。
            _pDialog.show();

        }

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
            String result = "";
            String postData = "id=" + storeId;

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

//                con.connect();
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
                JSONObject restNow = rootJSON.getJSONObject("rest");
                map.put("id",restNow.getString("id"));
                map.put("name", restNow.getString("name"));
                map.put("latitude", restNow.getString("latitude"));
                map.put("longitude", restNow.getString("longitude"));
                map.put("opentime", restNow.getString("opentime"));
                map.put("holiday", restNow.getString("holiday"));
                map.put("tel", restNow.getString("tel"));
                map.put("address", restNow.getString("address"));
                JSONObject imageUrl = restNow.getJSONObject("image_url");
                map.put("image1", imageUrl.getString("shop_image1"));
                JSONObject pr = restNow.getJSONObject("pr");
                map.put("pr_long", pr.getString("pr_long"));
                map.put("budget", restNow.getString("lunch"));
            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }


            if( (!"0".equals(map.get("latitude"))) && (!"0".equals(map.get("longitude"))) && (map.get("latitude") != null) && (map.get("longitude") != null) ) {
                //マーカー表示
                LatLng latLng = new LatLng(Float.parseFloat(map.get("latitude")), Float.parseFloat(map.get("longitude")));
                mMap.addMarker(new MarkerOptions().position(latLng).title(map.get("name"))).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }

            TextView tvStoreDetail = findViewById(R.id.tvStoreTitle);
            tvStoreDetail.setText(map.get("name"));

            tvStoreDetail = findViewById(R.id.tvStoreDetailAddress);
            tvStoreDetail.setText(map.get("address"));

            tvStoreDetail = findViewById(R.id.tvStoreDetailTel);
            tvStoreDetail.setText(map.get("tel"));

            tvStoreDetail = findViewById(R.id.tvStoreDetailOpentime);
            tvStoreDetail.setText(Tools.replaceBr(map.get("opentime")));

            tvStoreDetail = findViewById(R.id.tvStoreDetailBudget);
            tvStoreDetail.setText("約" + map.get("budget") + "円");

            tvStoreDetail = findViewById(R.id.tvStoreDetailPr);
            tvStoreDetail.setText(Tools.replaceBr(map.get("pr_long")));

            //非同期処理を開始する。
            FemaleStoreDetailsActivity.StoreImageGetTaskReceiver imageGetTaskReceiver = new FemaleStoreDetailsActivity.StoreImageGetTaskReceiver();

            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            imageGetTaskReceiver.execute(map.get("image1"));

            // ロード画面を消す。
            if (_pDialog != null && _pDialog.isShowing()) {
                _pDialog.dismiss();
            }

        }
    }


    /**
     * 非同期通信を行い画像を取得するAsyncTaskクラスを継承したメンバクラス.
     */
    private class StoreImageGetTaskReceiver extends AsyncTask<String, Void, Bitmap> {

        private static final String DEBUG_TAG = "RestAccess";

        /**
         * 非同期に処理したい内容を記述するメソッド.
         * このメソッドは必ず実装する必要がある。
         *
         * @param params String型の配列。（可変長）
         * @return String型の結果JSONデータ。
         */
        @Override
        public Bitmap doInBackground(String... params) {
            String urlStr = params[0];

            HttpURLConnection con = null;
            InputStream is = null;
            Bitmap result = null;

            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();

                //GET通信かPOST通信かを指定する。
                con.setRequestMethod("GET");

                //自動リダイレクトを許可するかどうか。
                con.setInstanceFollowRedirects(false);

                //時間制限。（ミリ秒単位）
                con.setReadTimeout(10000);
                con.setConnectTimeout(20000);

                con.connect();

                is = con.getInputStream();

                result = BitmapFactory.decodeStream(is);
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
        public void onPostExecute(Bitmap result) {
            ImageView ivStoreImage = findViewById(R.id.ivStoreImage);

            //ビットマップをImageViewに設定
            if (result != null) {
                ivStoreImage.setImageBitmap(result);
            }
        }
    }
}

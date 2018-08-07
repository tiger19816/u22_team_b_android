package b.team.works.u22.hal.u22teamb;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
 * 【妻】店の一覧表示-Map画面のアクティビティクラス.
 *
 * @author Taiga Hirai
 */
public class FemaleStoreMapListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private ListView lvStoreList;    // 内容エリア
    private LinearLayout linearLayoutArea;
    private FloatingActionButton fab;
    private final static int DURATION = 400;    // アニメーションにかける時間(ミリ秒)
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_store_map_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.storeMaps);
        mapFragment.getMapAsync(this);

        setTitle("店一覧");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //レフトナビ本体。
        NavigationView navigationView = findViewById(R.id.nvSideMenuButton);
        navigationView.setNavigationItemSelectedListener(this);

        // 内容エリアの結び付け
        lvStoreList = findViewById(R.id.lvStoreList);
        // ボタンの結び付け
        fab = findViewById(R.id.fabOpenList);

        linearLayoutArea = findViewById(R.id.llStoreMapMain);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearLayoutArea.getHeight() / 2;

        // 内容エリアを閉じるアニメーション
        FemaleStoreMapAnimation closeAnimation = new FemaleStoreMapAnimation(lvStoreList, -originalHeight, originalHeight);
        closeAnimation.setDuration(DURATION);
        lvStoreList.startAnimation(closeAnimation);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(34.699886, 135.493033);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("HAL大阪"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //非同期処理を開始する。
        StoreMapTaskReceiver receiver = new StoreMapTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(Word.STORE_MAP_URL);
    }


    /**
     * レフトナビ以外をクリックした時の動き。
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * レフトナビをクリックした時。
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_map) {
//            intent = new Intent(FemaleStoreMapListActivity.this,FemaleStoreMapListActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        } else if (id == R.id.nav_reservation) {
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleReservationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleHistoryListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_my_page) {
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleMyPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            //ユーザーID削除。
            SharedPreferences setting = getSharedPreferences("USER" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("ID");
            editor.commit();
            intent = new Intent(FemaleStoreMapListActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 内容エリアの結び付け
        lvStoreList = findViewById(R.id.lvStoreList);
        // ボタンの結び付け
        fab = findViewById(R.id.fabOpenList);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearLayoutArea.getHeight() / 2;

        // 展開ボタン押下時
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lvStoreList.getHeight() > 0) {       // 内容エリアが開いている時

                    // 内容エリアを閉じるアニメーション
                    FemaleStoreMapAnimation closeAnimation = new FemaleStoreMapAnimation(lvStoreList, -originalHeight, originalHeight);
                    closeAnimation.setDuration(DURATION);
                    lvStoreList.startAnimation(closeAnimation);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                    params.bottomMargin = 190;
                    fab.setLayoutParams(params);
                } else {

                    // 内容エリアが閉じている時、内容エリアを開くアニメーション
                    FemaleStoreMapAnimation openAnimation = new FemaleStoreMapAnimation(lvStoreList, originalHeight, 0);
                    openAnimation.setDuration(DURATION);    // アニメーションにかける時間(ミリ秒)
                    lvStoreList.startAnimation(openAnimation);   // アニメーション開始
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                    params.bottomMargin = originalHeight + 170;
                    fab.setLayoutParams(params);
                }
            }
        });
    }

    /**
     * フローティングアクションボタンが押された時のイベント処理用メソッド.
     *
     * @param view 画面部品。
     */
    public void onFabOpenListClick(View view) {
    }

    public void onButtonClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.rowBtStoreDetail:
                intent = new Intent(FemaleStoreMapListActivity.this, FemaleStoreDetailsActivity.class);
                intent.putExtra("id", (String) view.getTag());
                startActivity(intent);
                break;
            case R.id.rowBtStoreReservation:
                intent = new Intent(FemaleStoreMapListActivity.this, FemaleNewReservationActivity.class);
                StoreMapListReservationButtonTag reservationButtonTag = (StoreMapListReservationButtonTag) view.getTag();
                intent.putExtra("id", reservationButtonTag.getId());
                intent.putExtra("name", reservationButtonTag.getName());
                startActivity(intent);
                break;
        }
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class StoreMapTaskReceiver extends AsyncTask<String, Void, String> {

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
            String result = "";
            String postData = "lat=34.699886&lon=135.493033";

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
            final List<Map<String, String>> restList = new ArrayList<>();
            try {
                JSONObject rootJSON = new JSONObject(result);
                int hitPerPage = rootJSON.getInt("hit_per_page");
                JSONArray rest = rootJSON.getJSONArray("rest");
                for(int i = 0; i < hitPerPage; i++) {
                    Map<String, String> map = new HashMap<>();
                    JSONObject restNow = rest.getJSONObject(i);
                    map.put("id",restNow.getString("id"));
                    map.put("name", restNow.getString("name"));
                    map.put("latitude", restNow.getString("latitude"));
                    map.put("longitude", restNow.getString("longitude"));
                    map.put("opentime", restNow.getString("opentime"));
                    JSONObject pr = restNow.getJSONObject("pr");
                    map.put("short_pr", pr.getString("pr_short"));
                    restList.add(map);
                }
            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }

            for(Map<String, String> map : restList) {
                //マーカー表示
                LatLng latLng = new LatLng(Float.parseFloat(map.get("latitude")), Float.parseFloat(map.get("longitude")));
                mMap.addMarker(new MarkerOptions().position(latLng).title(map.get("name"))).setTag(map);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(FemaleStoreMapListActivity.this, FemaleStoreDetailsActivity.class);
                    Map<String, String> map = (Map<String, String>) marker.getTag();
                    intent.putExtra("id", map.get("id"));
                    intent.putExtra("name", map.get("name"));
                    startActivity(intent);
                }
            });

            String[] from = {"name", "short_pr", "id", "id"};
            int[] to = {R.id.rowTvStoreName, R.id.rowTvStoreShortPr, R.id.rowBtStoreDetail, R.id.rowBtStoreReservation};
            final SimpleAdapter adapter = new SimpleAdapter(FemaleStoreMapListActivity.this, restList, R.layout.row_store_list, from, to);
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                private String strStoreName = "";
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    int id = view.getId();
                    String strData = (String) data;
                    switch (id) {
                        case R.id.rowTvStoreName:
                            TextView tvStoreName = (TextView) view;
                            tvStoreName.setText(strData);
                            strStoreName = strData;
                            return true;
                        case R.id.rowTvStoreShortPr:
                            TextView rowTvStoreShortPr = (TextView) view;
                            rowTvStoreShortPr.setText(Tools.replaceBr(strData));
                            return true;
                        case R.id.rowBtStoreDetail:
                            Button btStoreDetail = (Button) view;
                            btStoreDetail.setTag(strData);
                            return true;
                        case R.id.rowBtStoreReservation:
                            Button btStoreReservation = (Button) view;
                            StoreMapListReservationButtonTag reservationButtonTag = new StoreMapListReservationButtonTag();
                            reservationButtonTag.setId(strData);
                            reservationButtonTag.setName(strStoreName);
                            btStoreReservation.setTag(reservationButtonTag);
                            return true;
                    }
                    return false;
                }
            });
            lvStoreList.setAdapter(adapter);
            lvStoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(FemaleStoreMapListActivity.this, FemaleStoreDetailsActivity.class);
                    Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                    intent.putExtra("id", map.get("id"));
                    intent.putExtra("name", map.get("name"));
                    startActivity(intent);
                }
            });
        }
    }
}

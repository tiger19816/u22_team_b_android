package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FemaleReservationListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOGIN_URL = Word.RESERVATION_LIST_URL;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_reservation_list);

        setTitle(getString(R.string.female_reservation_list_title));

        //ユーザーIDの取得。
        SharedPreferences setting = getSharedPreferences("USER" , 0);
        _id = setting.getString("ID" , "");

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

        //ユーザ名を表示する
        SharedPreferences pref = getSharedPreferences("USER",0);
        if(Build.VERSION.SDK_INT < 23) {
            TextView navTvUserName = navigationView.findViewById(R.id.navTvUserName);
            navTvUserName.setText(pref.getString("NAME", getString(R.string.female_reservation_list_user_name)));
        } else {
            View headerView = navigationView.getHeaderView(0);
            TextView navTvUserName = headerView.findViewById(R.id.navTvUserName);
            navTvUserName.setText(pref.getString("NAME", getString(R.string.female_reservation_list_user_name)));
        }

        //非同期処理を開始する。
        ReservationTaskReceiver receiver = new ReservationTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , _id );

    }

    @Override
    public void onResume() {
        super.onResume();

        //非同期処理を開始する。
        ReservationTaskReceiver receiver = new ReservationTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , _id );

    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class ReservationTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";
        private List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();

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
            String id = params[1];

            //POSTで送りたいデータ
            String postData = "id=" + id + "&sex=0";

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

                JSONArray datas = rootJSON.getJSONArray("reservationList");

                //予約情報
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    Map map = new HashMap<String , Object>();
                    map.put("storeName" , data.getString("storeName"));
                    //予約日を年、月、日の３つに分解する。
                    String dy = data.getString("reservationDate").substring(0, 4);//年
                    String dm = data.getString("reservationDate").substring(5, 7);//月
                    String dd = data.getString("reservationDate").substring(8, 10);//日
                    //現在日時の取得
                    java.util.Calendar cal =  java.util.Calendar.getInstance();
                    int nowYear = cal.get(Calendar.YEAR);
                    int nowMonth = cal.get(Calendar.MONTH);
                    int nowDay = cal.get(Calendar.DAY_OF_MONTH);
                    //予約時当日の場合、「今日」と表示する
                    if (    Integer.valueOf(dy) == nowYear &&
                            Integer.valueOf(dm) == (nowMonth + 1) &&
                            Integer.valueOf(dd) == nowDay){
                        map.put("reservationDate" , getString(R.string.female_reservation_list_today));
                    }else{
                        DataConversion dc = new DataConversion();
                        map.put("reservationDate" , dc.getFullDataConversion02(data.getString("reservationDate")));
                    }
                    map.put("reservationId" , data.getString("reservationId"));
                    map.put("storeId" , data.getString("storeId"));
                    _list.add(map);
                }

                String[] from = {"storeName", "reservationDate"};
                int[] to = {R.id.tvStoreName, R.id.tvReservationDate};
                final SimpleAdapter adapter = new SimpleAdapter(FemaleReservationListActivity.this, _list, R.layout.row_reservation2, from, to);
                adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        int id = view.getId();
                        String strData = (String) data;
                        switch (id) {
                            case R.id.tvStoreName:
                                TextView tvStoreName = (TextView) view;
                                tvStoreName.setText(strData);
                                return true;
                            case R.id.tvReservationDate:
                                TextView rowTvStoreShortPr = (TextView) view;
                                rowTvStoreShortPr.setText(Tools.replaceBr(strData));
                                return true;
                        }
                        return false;
                    }
                });
                ListView lvReservationList = findViewById(R.id.lvReservationList);
                lvReservationList.setAdapter(adapter);
                lvReservationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(FemaleReservationListActivity.this, FemaleChangeReservationActivity.class);
                        Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                        intent.putExtra("storeId", map.get("storeId"));
                        intent.putExtra("reservationId" , map.get("reservationId"));
                        startActivity(intent);
                    }
                });

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
            intent = new Intent(FemaleReservationListActivity.this,FemaleStoreMapListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_reservation) {
//            intent = new Intent(FemaleReservationListActivity.this,FemaleReservationListActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(FemaleReservationListActivity.this,FemaleHistoryListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_my_page) {
            intent = new Intent(FemaleReservationListActivity.this,FemaleMyPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences setting = getSharedPreferences("USER" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("ID");
            editor.remove("SEX");
            editor.remove("NAME");
            editor.commit();
            intent = new Intent(FemaleReservationListActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

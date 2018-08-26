package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
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

import b.team.works.u22.hal.u22teamb.fcm.FcmInstance;

public class MaleReservationListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOGIN_URL = Word.RESERVATION_LIST_URL;
    private static final String LOGIN_URL2 = Word.RESERVATION_CHECK_URL;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_reservation_list);

        setTitle("予約一覧");

        //ユーザーIDの取得。
        SharedPreferences setting = getSharedPreferences("USER" , 0);
        _id = setting.getString("ID" , "");

        //トークンの更新 + バックグラウンド処理開始(?)。 by Yuki Yoshida
        FcmInstance fcm = new FcmInstance(FcmInstance.setChannel(getApplicationContext()));
        try {
            fcm.sendToken(this, _id, 1);
        } catch (Exception e) {
            Log.e("Push通知",e.toString());
        }

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
            String postData = "id=" + id + "&sex=1";

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
                    map.put("reservationId" , data.getString("reservationId"));
                    map.put("storeName" , data.getString("storeName"));
                    map.put("reservationDate" , data.getString("reservationDate"));
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
                        map.put("reservationDate" , "今日");
                    }else{
                        map.put("reservationDate" , data.getString("reservationDate"));
                    }
                    map.put("newImage" , data.getString("newImage"));
                    _list.add(map);

                }

                String[] from = {"storeName", "reservationDate","newImage"};
                int[] to = {R.id.tvStoreName, R.id.tvReservationDate , R.id.ivNewImage};
                final SimpleAdapter adapter = new SimpleAdapter(MaleReservationListActivity.this, _list, R.layout.row_reservation, from, to);
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
                            case R.id.ivNewImage:
                                ImageView ivNewImage = (ImageView) view;
                                String newImage = Tools.replaceBr(strData);
                                if("0".equals(newImage)) {
                                    ivNewImage.setImageResource(R.drawable.newicon);
                                }else{
                                    ivNewImage.setImageResource(R.drawable.colorwhite);
                                }
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
                        Intent intent = new Intent(MaleReservationListActivity.this, MaleEnterShopActivity.class);
                        Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                        intent.putExtra("storeName", map.get("storeName"));
                        intent.putExtra("maleId", _id);

                        if("0".equals(map.get("newImage"))) {
                            //非同期処理を開始する。(初めて見る場合はUPDATE)
                            ReservationCheckTaskReceiver receiver = new ReservationCheckTaskReceiver();
                            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
                            receiver.execute(LOGIN_URL2, map.get("reservationId"));
                            intent.putExtra("newInformation", "1");
                        }else{
                            intent.putExtra("newInformation", "0");
                        }

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
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class ReservationCheckTaskReceiver extends AsyncTask<String, Void, String> {

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
            String postData = "id=" + id;

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

                String datas = rootJSON.getString("updateResult");

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
        if (id == R.id.nav_reservation) {
            intent = new Intent(MaleReservationListActivity.this,MaleReservationListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(MaleReservationListActivity.this,MaleHistoryListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences setting = getSharedPreferences("USER" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("ID");
            editor.commit();
            intent = new Intent(MaleReservationListActivity.this,MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

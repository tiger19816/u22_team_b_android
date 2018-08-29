package b.team.works.u22.hal.u22teamb;

import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class FemaleMyPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * URLを入れる定数.
     */
    private static final String LOGIN_URL = Word.USER_MYPAGE_URL;
    private String _id;
    private String maleRegistrationCode;
    private Boolean maleRegistered = true;
    private MenuItem menuToggleVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_my_page);

        setTitle("MyPage");

        //ユーザーIDの取得。
        SharedPreferences setting = getSharedPreferences("USER" , 0);
        _id = setting.getString("ID" , "");

        //非同期処理を開始する。
        LoginTaskReceiver receiver = new LoginTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , _id );

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
            navTvUserName.setText(pref.getString("NAME", "ユーザー名"));
        } else {
            View headerView = navigationView.getHeaderView(0);
            TextView navTvUserName = headerView.findViewById(R.id.navTvUserName);
            navTvUserName.setText(pref.getString("NAME", "ユーザー名"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.female_my_page , menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuToggleVisible = menu.findItem(R.id.btnQrCode);
        menuToggleVisible.setVisible(maleRegistered);
        return true;
    }

    private void toggleMenuVisible() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemid = item.getItemId();
        switch (itemid) {
            case R.id.btnQrCode:
                Intent intent = new Intent(FemaleMyPageActivity.this,FemaleQrCodeActivity.class);
                intent.putExtra("MALEREGISTRATIONCODE",maleRegistrationCode);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class LoginTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";
//        private String maleRegistrationCode;
//        private Boolean maleRegistered;

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

                Male male = new Male();
                Female female = new Female();

                //妻情報
                String femaleName = rootJSON.getString("femaleName");
                TextView tvFemaleName = findViewById(R.id.tvFemaleName);
                tvFemaleName.setText(femaleName);

                String femaleBirthday = rootJSON.getString("femaleBirthday");
                TextView tvFemaleBirthday = findViewById(R.id.tvFemaleBirthday);
                tvFemaleBirthday.setText(female.getDataConversion2(femaleBirthday));

                String femalePassword = rootJSON.getString("femalePassword");
                TextView tvFemalePassword = findViewById(R.id.tvFemalePassword);
                String password = "";
                for(int i=0; i<femalePassword.length(); i++){
                    password += "*";
                }
                tvFemalePassword.setText(password);

                String femaleIcon = rootJSON.getString("femaleIcon");
                ImageView imFemaleIcon = findViewById(R.id.ivFemaleIcon);
                imFemaleIcon.setImageResource(R.drawable.icon);

                String femaleAddress = rootJSON.getString("femaleAddress");
                TextView tvFemaleAddress = findViewById(R.id.tvFemaleAddress);
                tvFemaleAddress.setText(femaleAddress);

                String femaleMail = rootJSON.getString("femaleMail");
                TextView tvFemaleMail = findViewById(R.id.tvFemaleMail);
                tvFemaleMail.setText(femaleMail);

                String femaleCardNo = rootJSON.getString("cardNumber");
                TextView tvFemaleCardNo = findViewById(R.id.tvFemaleCreditCardNumber);
                tvFemaleCardNo.setText(femaleCardNo);

                String femaleCardDoneDeadline = rootJSON.getString("cardExpirationDate");
                TextView tvFemaleCardDoneDeadline = findViewById(R.id.tvFemaleCreditCardExpirationDate);
                tvFemaleCardDoneDeadline.setText(femaleCardDoneDeadline);

                String femaleCardSecurityCode = rootJSON.getString("cardSecurityCode");
                TextView tvFemaleCardSecurityCode = findViewById(R.id.tvFemaleCreditCardSecurityNumber);
                tvFemaleCardSecurityCode.setText(femaleCardSecurityCode);

                String femaleCardNominee = rootJSON.getString("cardNominee");
                TextView tvFemaleNomineeName = findViewById(R.id.tvFemaleCreditCardHolder);
                tvFemaleNomineeName.setText(femaleCardNominee);

                //夫情報
                String maleName = rootJSON.getString("maleName");
                TextView tvMaleName = findViewById(R.id.tvMaleName);
                tvMaleName.setText(maleName);

                String maleMail = rootJSON.getString("maleMail");
                TextView tvMaleMail = findViewById(R.id.tvMaleMail);
                tvMaleMail.setText(maleMail);

                String malePassword = rootJSON.getString("malePassword");
                TextView tvMalePassword = findViewById(R.id.tvMalePassword);
                tvMalePassword.setText(malePassword);

                String maleBirthday = rootJSON.getString("maleBirthday");
                TextView tvMaleBirthday = findViewById(R.id.tvMaleBirthday);
                tvMaleBirthday.setText(female.getDataConversion2(maleBirthday));

                String maleHeight = rootJSON.getString("height");
                TextView tvMaleHeight = findViewById(R.id.tvMaleHeight);
                tvMaleHeight.setText(maleHeight + "cm");

                String maleWeight = rootJSON.getString("weight");
                TextView tvMaleWeight = findViewById(R.id.tvMaleWeight);
                tvMaleWeight.setText(maleWeight + "kg");

                String maleProfession = rootJSON.getString("profession");
                TextView tvMaleProfession = findViewById(R.id.tvMaleProfession);
                tvMaleProfession.setText(male.setMaleProfessionName(maleProfession));

                maleRegistrationCode = rootJSON.getString("maleRegistrationCode");
//                Log.e("確認" , maleRegistrationCode);
//                if("".equals(maleRegistrationCode)) {
//                    maleRegistered = false;
//                    toggleMenuVisible();
//                }else{
//                    maleRegistered = true;
//                    toggleMenuVisible();
//                }

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
            intent = new Intent(FemaleMyPageActivity.this,FemaleStoreMapListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_reservation) {
            intent = new Intent(FemaleMyPageActivity.this,FemaleReservationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(FemaleMyPageActivity.this,FemaleHistoryListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_my_page) {
//            intent = new Intent(FemaleMyPageActivity.this,FemaleMyPageActivity.class);
//            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences setting = getSharedPreferences("USER" , 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.remove("ID");
            editor.commit();
            intent = new Intent(FemaleMyPageActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

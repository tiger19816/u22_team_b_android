package b.team.works.u22.hal.u22teamb;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FemaleNewMemberRegistrationConfirmationScreenActivity extends AppCompatActivity {

    /**
     * URL。
     */
    private static final String LOGIN_URL = Word.USER__URL;

    private String _id;

    private String femaleName;
    private String femaleBirthday;
    private String femalePassword;
    private String femaleMail;
    private String femaleIcon;
    private String femaleCardNo;
    private String femaleCardDoneDeadline;
    private String femaleCardSecurityCode;
    private String femaleCardNomineeName;
    private String femaleAddress;
    private String femaleLatitude;//緯度
    private String femaleLongitude;//経度

    private String maleName;
    private String maleBirthday;
    private String maleMail;
    private String maleHeight;
    private String maleWeight;
    private String maleProfession;

    public ProgressDialog _pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_new_member_registration_confirmation_screen);

        // プログレスダイアログの生成。
        _pDialog = new ProgressDialog(FemaleNewMemberRegistrationConfirmationScreenActivity.this);
        _pDialog.setMessage(getString(R.string.progress_message));  // メッセージを設定。

        // プログレスダイアログの表示。
        _pDialog.show();

        setTitle(getString(R.string.female_new_member_registration_confirmation_screen_title));

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //確認画面に値をセットする。
        setUserTextCreate();

        // ロード画面を消す。
        if (_pDialog != null && _pDialog.isShowing()) {
            _pDialog.dismiss();
        }

    }

    public void onFinishRegistrationClick(View view){
        //非同期処理を開始する。
        LoginTaskReceiver receiver = new LoginTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , femaleName , femaleBirthday , femalePassword , femaleMail , femaleIcon , femaleCardNo , femaleCardDoneDeadline , femaleCardSecurityCode, femaleCardNomineeName , femaleAddress , femaleLatitude , femaleLongitude ,maleName , "" , maleBirthday , maleMail , maleHeight , maleWeight , maleProfession);
    }

    public void setUserTextCreate(){

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");
        Male male = (Male) getIntent().getSerializableExtra("MALE");

        this.femaleName = female.getFemaleName();
        this.femaleBirthday = female.getFemaleBirthDay();
        this.femalePassword = female.getFemalePassword();
        this.femaleMail = female.getFemaleMail();
        this.femaleIcon = female.getFemaleIcon();
        this.femaleCardNo = female.getFemaleCardNo();
        this.femaleCardDoneDeadline = female.getFemaleCardExpirationDate();
        this.femaleCardSecurityCode = female.getFemaleCardSecurityCode();
        this.femaleCardNomineeName = female.getFemaleCardNominee();
        this.femaleAddress = female.getFemaleAddress();
        this.femaleLatitude = female.getFemaleLatitude();
        this.femaleLongitude = female.getFemaleLongitude();

        this.maleName = male.getMaleName();
        this.maleBirthday = male.getMaleBirthday();
        this.maleMail = male.getMaleMail();
        this.maleHeight = male.getMaleHeight();
        this.maleWeight = male.getMaleWeight();
        this.maleProfession = male.getMaleProfession();

        //妻情報
      ImageView ivIcon = findViewById(R.id.ivFemaleIcon);
        ivIcon.setImageResource(R.drawable.icon);

        TextView tvFemaleName = findViewById(R.id.tvFemaleName);
        tvFemaleName.setText(femaleName);

        TextView tvFemaleBirthday = findViewById(R.id.tvFemaleBirthday);
        tvFemaleBirthday.setText(femaleBirthday);

//        TextView tvFemalePassword = findViewById(R.id.tvFemalePassword);
//        String password = "";
//        for(int i=0; i<femalePassword.length(); i++){
//            password += "*";
//        }
//        tvFemalePassword.setText(password);

        TextView tvFemaleAddress = findViewById(R.id.tvFemaleAddress);
        tvFemaleAddress.setText(femaleAddress);

        TextView tvFemaleMail = findViewById(R.id.tvFemaleMail);
        tvFemaleMail.setText(femaleMail);

        TextView tvFemaleCardNo = findViewById(R.id.tvFemaleCreditCardNumber);
        tvFemaleCardNo.setText(femaleCardNo);

        TextView tvFemaleDoneDeadline = findViewById(R.id.tvFemaleCreditCardExpirationDate);
        tvFemaleDoneDeadline.setText(femaleCardDoneDeadline);

//        TextView tvFemaleSecurityCode = findViewById(R.id.tvFemaleCreditCardSecurityNumber);
//        tvFemaleSecurityCode.setText(femaleCardSecurityCode);

//        TextView tvFemaleNomineeName = findViewById(R.id.tvFemaleCreditCardHolder);
//        tvFemaleNomineeName.setText(femaleCardNomineeName);

        //夫情報
        TextView tvMaleName = findViewById(R.id.tvMaleName);
        tvMaleName.setText(maleName);

        TextView tvMaleBirthDay = findViewById(R.id.tvMaleBirthday);
        tvMaleBirthDay.setText(maleBirthday);

        TextView tvMaleMail = findViewById(R.id.tvMaleMail);
        tvMaleMail.setText(maleMail);

        TextView tvMaleHeight = findViewById(R.id.tvMaleHeight);
        tvMaleHeight.setText(maleHeight);

        TextView tvMaleWeight = findViewById(R.id.tvMaleWeight);
        tvMaleWeight.setText(maleWeight);

        TextView tvMaleProfession = findViewById(R.id.tvMaleProfession);
        tvMaleProfession.setText(male.setMaleProfessionName(maleProfession));
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class LoginTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";

        /**
         * 非同期に処理したい内容を記述するメソッド.
         *
         * @param params String型の配列。（可変長）
         * @return String型の結果JSONデータ。
         */
        @Override
        public String doInBackground(String... params) {

            Female female = new Female();

            String urlStr = params[0];
            String femaleName = params[1];
            String femaleBirthday = female.getDataConversion(params[2]);
            String femalePassword = params[3];
            String femaleMail = params[4];
            String femaleIcon = params[5];
            String femaleCardNo = params[6];
            String femaleCardDoneDeadLine = params[7];
            String femaleCardSecurityCode = params[8];
            String femaleCardNomineeName = params[9];
            String femaleAddress = params[10];
            String femaleLatitude = params[11];
            String femaleLongitude = params[12];
            String maleName = params[13];
            String malePassword = params[14];
            String maleBirthday = female.getDataConversion(params[15]);
            String maleMail = params[16];
            String maleHeight = params[17];
            String maleWeight = params[18];
            String maleProfession = params[19];

            //POSTで送りたいデータ
            String postData = "femaleName=" + femaleName + "&femaleBirthday=" + femaleBirthday + "&femalePassword=" + femalePassword + "&femaleMail=" + femaleMail + "&femaleIcon=" + femaleIcon
                    + "&femaleCardNo=" + femaleCardNo + "&femaleCardDoneDeadline=" + femaleCardDoneDeadLine + "&femaleSecurityCode=" + femaleCardSecurityCode + "&femaleCardNomineeName=" + femaleCardNomineeName
                    + "&femaleAddress=" + femaleAddress + "&femaleLatitude=" + femaleLatitude + "&femaleLongitude=" + femaleLongitude
                    + "&maleName=" + maleName + "&malePassword=" + malePassword + "&maleBirthday=" + maleBirthday + "&maleMail=" + maleMail + "&maleHeight=" + maleHeight + "&maleWeight=" + maleWeight + "&maleProfession=" + maleProfession;

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
            Boolean isInsert = false;
            try {
                JSONObject rootJSON = new JSONObject(result);
                isInsert = rootJSON.getBoolean("result");
                _id = rootJSON.getString("id");
            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
            if (isInsert) {
                Toast.makeText(FemaleNewMemberRegistrationConfirmationScreenActivity.this , getString(R.string.female_new_member_registration_confirmation_screen_registered_message) , Toast.LENGTH_SHORT).show();
                SharedPreferences setting = getSharedPreferences("USER" , 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putString("ID" , _id);
                editor.putString("SEX" , "0");
                editor.putString("NAME" , femaleName);
                editor.commit();
                Intent intent = new Intent(FemaleNewMemberRegistrationConfirmationScreenActivity.this,FemaleStoreMapListActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(FemaleNewMemberRegistrationConfirmationScreenActivity.this , getString(R.string.female_new_member_registration_confirmation_screen_did_not_register_warning) , Toast.LENGTH_SHORT).show();
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
     * アクションバー。
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

}

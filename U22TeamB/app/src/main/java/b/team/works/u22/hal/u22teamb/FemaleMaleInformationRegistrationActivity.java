package b.team.works.u22.hal.u22teamb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FemaleMaleInformationRegistrationActivity extends AppCompatActivity {

    private static final String MAIL_CHECK_URL = Word.MAIL_CHECK_URL;
    private Calendar cal;
    private int nowYear;
    private int nowMonth;
    private int nowDayOfMonth;
    private SimpleDateFormat dfBirthday = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat dfDayOfMonth = new SimpleDateFormat("dd");
    private Female female;
    private Male male;
    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_male_information_registration);

        setTitle(getString(R.string.female_male_information_registration_title));

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.cal = Calendar.getInstance();
        this.nowYear = cal.get(Calendar.YEAR);
        this.nowMonth = cal.get(Calendar.MONTH);
        this.nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        EditText etBirthday = findViewById(R.id.etInputBirthday);
        etBirthday.setFocusable(false);
    }

    /**
     * 入力内容確認画面に遷移するボタンをクリックした時。
     * @param view
     */
    public void onCardRegistrationClick(View view){

        female = (Female) getIntent().getSerializableExtra("FEMALE");
        female.setInputChecked();

        male = new Male();

        EditText etMaleName = findViewById(R.id.etInputName);
        String maleName = etMaleName.getText().toString();
        male.setMaleName(maleName);

        EditText etMaleAddress = findViewById(R.id.etInputAddress);
        String maleAddress = etMaleAddress.getText().toString();//住所
        female.setFemaleAddress(this , maleAddress);
        String mpMaleLatitude = female.getFemaleLatitude();//緯度
        String mpMaleLongitude = female.getFemaleLongitude();//経度

        EditText etMaleBirthDate = findViewById(R.id.etInputBirthday);
        String maleBirthDate = etMaleBirthDate.getText().toString();
        male.setMaleBirthday(maleBirthDate);

        EditText etMaleMail = findViewById(R.id.etInputMail);
        String maleMail = etMaleMail.getText().toString();
        mail = maleMail;
        male.setMaleMail(maleMail);

        EditText etMaleHeight = findViewById(R.id.etInputMaleHeight);
        String maleHeight = etMaleHeight.getText().toString();
        male.setMaleHeight(maleHeight);

        EditText etMaleWeight = findViewById(R.id.etInputMaleWeight);
        String maleWeight = etMaleWeight.getText().toString();
        male.setMaleWeight(maleWeight);

        Spinner spMaleProfession = findViewById(R.id.spInputMaleOccupation);
        String maleProfession = String.valueOf(spMaleProfession.getSelectedItemPosition());
        male.setMaleProfession(maleProfession);

        if(female.getInputChecked() && male.getInputChecked()) {
            //非同期処理を開始する。
            MailInformationCheckTaskReceiver receiver = new MailInformationCheckTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(MAIL_CHECK_URL);
        }else{
            Toast.makeText(FemaleMaleInformationRegistrationActivity.this , getString(R.string.female_male_information_registration_input_check_complete_3) , Toast.LENGTH_SHORT).show();
        }
   }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class MailInformationCheckTaskReceiver extends AsyncTask<String, Void, String> {

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
            String postData = "mail=" + mail;

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
                Boolean checkMail = rootJSON.getBoolean("checkMail");
                if(checkMail) {
                    Intent intent = new Intent(FemaleMaleInformationRegistrationActivity.this,FemaleNewMemberRegistrationConfirmationScreenActivity.class);
                    intent.putExtra("FEMALE", female);
                    intent.putExtra("MALE", male);
                    startActivity(intent);
                }else{
                    Toast.makeText(FemaleMaleInformationRegistrationActivity.this , "このメールアドレスはすでに登録されています。" , Toast.LENGTH_SHORT).show();
                }

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
     * 日付選択ダイアログ表示ボタンが押された時のイベント処理用メソッド。
     */
    public void onDateClick(View view){
        EditText etBirthday = findViewById(R.id.etInputBirthday);
        String Birthday = etBirthday.getText().toString();
        if(!"".equals(Birthday)) {
            try {
                Date date = dfBirthday.parse(Birthday);
                this.nowYear = Integer.parseInt(dfYear.format(date));
                this.nowMonth = Integer.parseInt(dfMonth.format(date))-1;
                this.nowDayOfMonth = Integer.parseInt(dfDayOfMonth.format(date));
                DatePickerDialog dialog = new DatePickerDialog(FemaleMaleInformationRegistrationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
                dialog.show();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("データ変換失敗", "ダイアログ表示の時");
            }
        }else{
            DatePickerDialog dialog = new DatePickerDialog(FemaleMaleInformationRegistrationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
            dialog.show();
        }
    }

    /**
     * 日付選択ダイアログの完了ボタンが押された時のイベント処理用メソッド。
     */
    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
            String dateMessage = year + getString(R.string.year_title) + (monthOfYear + 1) + getString(R.string.month_title) + dayOfMonth + getString(R.string.date_title);
            EditText etBirthday = findViewById(R.id.etInputBirthday);
            etBirthday.setText(dateMessage);
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

package b.team.works.u22.hal.u22teamb;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

public class MaleAdditionalInformationEntryActivity extends AppCompatActivity {

    private static final String LOGIN_URL = Word.USER_LOGIN_URL;
    private String _id;
    private String _code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.male_additional_information_entry);

        setTitle(getString(R.string.male_additional_information_entry_title));

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //QRコードリーダを起動する。
        new IntentIntegrator(MaleAdditionalInformationEntryActivity.this).initiateScan();
    }

    /**
     * QRコードリーダが終了した時の処理.
     * 何も読み込まれなかった場合もこのメソッドが呼ばれる。
     *
     * @param requestCode 要求コード（特に気にしない）。
     * @param resultCode 操作が成功したRESULT_OKか、バックアウトしたりなど理由で失敗した場合のRESULT_CANCELEDが入っている。
     * @param data 結果のデータを運ぶIntent。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        //resultCodeがRESULT_OKの場合は、処理を行う。
        if(resultCode == RESULT_OK) {
            //読み込まれた文字列。
            _code = result.getContents();
            TextView tvUserName = findViewById(R.id.tvUserName);
            tvUserName.setText(_code+getString(R.string.honor_title));
        } else {
            finish();
        }
    }

    /**
     * 登録ボタンが押された時。
     */
    public void onMalePasswordClick(View view){
        //パスワード01
        EditText etMalePassword = findViewById(R.id.etInputPassword);
        String malePassword = etMalePassword.getText().toString();
        //パスワード02
        EditText etMalePasswordRe = findViewById(R.id.etInputPasswordRe);
        String malePasswordRe = etMalePasswordRe.getText().toString();
        //チェック。
        if("".equals(malePassword) || "".equals(malePasswordRe)){
            Toast.makeText(MaleAdditionalInformationEntryActivity.this , getString(R.string.male_additional_information_entry_empty_password_warning) , Toast.LENGTH_SHORT).show();
        }else if(!malePassword.equals(malePasswordRe)){
            Toast.makeText(MaleAdditionalInformationEntryActivity.this , getString(R.string.male_additional_information_entry_incorrect_password_warning) , Toast.LENGTH_SHORT).show();
        }else{
            //非同期処理を開始する。
            MaleAdditionalInformationEntryTaskReceiver receiver = new MaleAdditionalInformationEntryTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(LOGIN_URL , _code , malePassword);
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

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class MaleAdditionalInformationEntryTaskReceiver extends AsyncTask<String, Void, String> {

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
            String code = params[1];
            String password = params[2];

            //POSTで送りたいデータ
            String postData = "code=" + code + "&password=" + password;

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
                Boolean done = rootJSON.getBoolean("done");
                _id = rootJSON.getString("maleId");
                if(done){
                    Toast.makeText(MaleAdditionalInformationEntryActivity.this , getString(R.string.male_additional_information_entry_registered_message) , Toast.LENGTH_SHORT).show();
                    SharedPreferences setting = getSharedPreferences("USER" , 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("ID" , _id);
                    editor.commit();
                    Intent intent = new Intent(MaleAdditionalInformationEntryActivity.this, MaleReservationListActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MaleAdditionalInformationEntryActivity.this , getString(R.string.male_additional_information_entry_did_not_register_warning) , Toast.LENGTH_SHORT).show();
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
}

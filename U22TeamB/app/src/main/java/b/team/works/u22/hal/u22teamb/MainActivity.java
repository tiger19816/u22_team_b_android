package b.team.works.u22.hal.u22teamb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private static final String LOGIN_URL = Word.USER_LOGIN_URL;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ユーザーIDの取得。
        SharedPreferences setting = getSharedPreferences("USER" , 0);
        _id = setting.getString("ID" , "");
        if(!"".equals(_id)){
            Intent intent = new Intent(MainActivity.this, FemaleStoreMapListActivity.class);
            startActivity(intent);
        }
    }

    public void onUserLoginClick(View view){

        //メールアドレス
        EditText etMail = findViewById(R.id.etMail);
        String strMail = etMail.getText().toString();
        //パスワード
        EditText etPassword = findViewById(R.id.etPassword);
        String strPassword = etPassword.getText().toString();

        String strMessage = "";

        if(!"".equals(strMail) && !"".equals(strPassword)) {

            //非同期処理を開始する。
            LoginTaskReceiver receiver = new LoginTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(LOGIN_URL , strMail , strPassword);

        }else{
            strMessage = "登録された、メールアドレスとパスワードを入力してください。";
            Toast.makeText(MainActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class LoginTaskReceiver extends AsyncTask<String, Void, String> {

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
            String mail = params[1];
            String password = params[1];

            //POSTで送りたいデータ
            String postData = "mail=" + mail + "&password=" + password;

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
                int userSex = rootJSON.getInt("userSex");
                String userId = rootJSON.getString("userId");

                //DBチェックの結果により、画面遷移先を変更。
                Intent intent;
                SharedPreferences setting = getSharedPreferences("USER" , 0);
                SharedPreferences.Editor editor = setting.edit();
                if (userSex == 0) {
                    //妻がログインした時。
                    editor.putString("ID" , userId);
                    editor.commit();
                    intent = new Intent(MainActivity.this, FemaleStoreMapListActivity.class);
                    startActivity(intent);
                } else if (userSex == 1) {
                    //夫がログインした時。
                    editor.putString("ID" , userId);
                    editor.commit();
                    intent = new Intent(MainActivity.this, MaleReservationListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "メールアドレスか、パスワードを間違えています。", Toast.LENGTH_SHORT).show();
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
     * 新規登録が押された時のイベント処理用メソッド。
     */
    public void onNewUserPageClick(View view){
        FullDialogFragment dialog = new FullDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager,"FullDialogFragment");
    }
}
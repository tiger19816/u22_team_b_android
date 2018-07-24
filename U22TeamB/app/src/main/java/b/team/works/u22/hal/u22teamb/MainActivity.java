package b.team.works.u22.hal.u22teamb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onUserLoginClick(View view){

        //ユーザーID
        EditText etMail = findViewById(R.id.etMail);
        String strMail = etMail.getText().toString();
        //パスワード
        EditText etPassword = findViewById(R.id.etPassword);
        String strPassword = etPassword.getText().toString();

        String strMessage = "ここにエラーチェックの結果により、メッセージを記述。";
        //DBチェック

        //DBチェックの結果により、画面遷移先を変更。
        Intent intent;
        if ("0".equals(strMail)) {
            //妻がログインした時。
            intent = new Intent(MainActivity.this , FemaleStoreMapListActivity.class);
            startActivity(intent);
        }else if ("1".equals(strMail)) {
            //夫がログインした時。
            intent = new Intent(MainActivity.this , MaleReservationListActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this , strMessage , Toast.LENGTH_SHORT).show();
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
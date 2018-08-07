package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MaleAdditionalInformationEntryActivity extends AppCompatActivity {

    private String _code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.male_additional_information_entry);

        setTitle("追加情報");

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
            tvUserName.setText(_code+"様");
        } else {
            finish();
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

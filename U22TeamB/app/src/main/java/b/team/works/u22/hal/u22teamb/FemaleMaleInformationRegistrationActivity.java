package b.team.works.u22.hal.u22teamb;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FemaleMaleInformationRegistrationActivity extends AppCompatActivity {

    private Calendar cal;
    private int nowYear;
    private int nowMonth;
    private int nowDayOfMonth;
    private SimpleDateFormat dfBirthday = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat dfDayOfMonth = new SimpleDateFormat("dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_male_information_registration);

        setTitle("夫情報入力画面");

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

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");
        female.setInputChecked();

        Male male = new Male();

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
            Intent intent = new Intent(FemaleMaleInformationRegistrationActivity.this,FemaleNewMemberRegistrationConfirmationScreenActivity.class);
            intent.putExtra("FEMALE", female);
            intent.putExtra("MALE", male);
            startActivity(intent);
        }else{
            Toast.makeText(FemaleMaleInformationRegistrationActivity.this , "入力チェック3完了" , Toast.LENGTH_SHORT).show();
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
            String dateMessage = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
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

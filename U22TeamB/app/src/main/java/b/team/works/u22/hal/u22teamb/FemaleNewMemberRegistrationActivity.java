package b.team.works.u22.hal.u22teamb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

public class FemaleNewMemberRegistrationActivity extends AppCompatActivity {

    private List<String> _list;
    private Calendar cal;
    private int nowYear;
    private int nowMonth;
    private int nowDayOfMonth;
    private String dateData;
    private SimpleDateFormat dfBirthday = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat dfDayOfMonth = new SimpleDateFormat("dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_new_member_registration);

        this.cal = Calendar.getInstance();
        this.nowYear = cal.get(Calendar.YEAR);
        this.nowMonth = cal.get(Calendar.MONTH);
        this.nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        EditText etBirthday = findViewById(R.id.etInputBirthdate);
        etBirthday.setFocusable(false);
    }

    /**
     * 日付選択ダイアログ表示ボタンが押された時のイベント処理用メソッド。
     */
    public void onDateClick(View view){
        EditText etBirthday = findViewById(R.id.etInputBirthdate);
        String Birthday = etBirthday.getText().toString();
        if(!"".equals(Birthday)) {
            try {
                Date date = dfBirthday.parse(Birthday);
                this.nowYear = Integer.parseInt(dfYear.format(date));
                this.nowMonth = Integer.parseInt(dfMonth.format(date))-1;
                this.nowDayOfMonth = Integer.parseInt(dfDayOfMonth.format(date));
                DatePickerDialog dialog = new DatePickerDialog(FemaleNewMemberRegistrationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
                dialog.show();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("データ変換失敗", "ダイアログ表示の時");
            }
        }else{
            DatePickerDialog dialog = new DatePickerDialog(FemaleNewMemberRegistrationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
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
            EditText etBirthday = findViewById(R.id.etInputBirthdate);
            etBirthday.setText(dateMessage);
        }
    }

    /**
     *「次へボタンをクリックした時。」
     * @param view
     */
    public void onMaleInformationClick(View view){

        //妻情報のエンティティークラス
        Female female = new Female();

        String femaleIcon = "icon.jpg";
        female.setFemaleIcon(femaleIcon);

        EditText etFemaleName = findViewById(R.id.etInputName);
        String femaleName = etFemaleName.getText().toString();
        female.setFemaleName(femaleName);

        EditText etFemaleBirthday = findViewById(R.id.etInputBirthdate);
        String femaleBirthday = etFemaleBirthday.getText().toString();
        female.setFemaleBirthDay(femaleBirthday);

        EditText etFemaleMail = findViewById(R.id.etInputMail);
        String femaleMail = etFemaleMail.getText().toString();
        female.setFemaleMail(femaleMail);

        EditText etFemalePassword = findViewById(R.id.etInputPassword);
        String femalePassword = etFemalePassword.getText().toString();
        female.setFemalePassword(femalePassword);

        if(female.getInputChecked()) {
            Intent intent = new Intent(FemaleNewMemberRegistrationActivity.this, FemaleCardRegistrationActivity.class);
            intent.putExtra("FEMALE", female);
            startActivity(intent);
        }else{
            Toast.makeText(FemaleNewMemberRegistrationActivity.this , "入力チェック完了" , Toast.LENGTH_SHORT).show();
        }
    }
}

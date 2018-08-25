package b.team.works.u22.hal.u22teamb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FemaleNewReservationActivity extends AppCompatActivity {

    private Reservation reservation;
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
        setContentView(R.layout.activity_female_new_reservation);

        setTitle( "予約内容入力" );

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        reservation = new Reservation();

        //IDの取得
        Intent intent = getIntent();
        reservation.setId(intent.getStringExtra("id"));
        reservation.setName(intent.getStringExtra("name"));

        this.cal = Calendar.getInstance();
        this.nowYear = cal.get(Calendar.YEAR);
        this.nowMonth = cal.get(Calendar.MONTH);
        this.nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        TextView tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreName.setText(reservation.getName());
    }

    /**
     * 日付選択ダイアログ表示ボタンが押された時のイベント処理用メソッド。
     */
    public void onDateClickDialog(View view){
        EditText etDate = findViewById(R.id.etDate);
        String strDate = etDate.getText().toString();
        if(!"".equals(strDate)) {
            try {
                Date date = dfBirthday.parse(strDate);
                this.nowYear = Integer.parseInt(dfYear.format(date));
                this.nowMonth = Integer.parseInt(dfMonth.format(date))-1;
                this.nowDayOfMonth = Integer.parseInt(dfDayOfMonth.format(date));
                DatePickerDialog dialog = new DatePickerDialog(FemaleNewReservationActivity.this, new FemaleNewReservationActivity.DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
                dialog.show();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("データ変換失敗", "ダイアログ表示の時");
            }
        }else{
            DatePickerDialog dialog = new DatePickerDialog(FemaleNewReservationActivity.this, new FemaleNewReservationActivity.DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
            dialog.show();
        }
    }

    /**
     * 日付選択ダイアログの完了ボタンが押された時のイベント処理用メソッド。
     */
    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
            String dataMonth = "";
            String dataDate = "";
            if(monthOfYear<10){ dataMonth = "0" + String.valueOf(monthOfYear+1); }else{ dataMonth = String.valueOf(monthOfYear+1); }
            if(dayOfMonth<10){ dataDate = "0" + String.valueOf(dayOfMonth); }else{ dataDate = String.valueOf(dayOfMonth); }
            String dateMessage = year + "年" + dataMonth + "月" + dataDate + "日";
            EditText etDate = findViewById(R.id.etDate);
            etDate.setText(dateMessage);
        }
    }

    /**
     * 時間選択ダイアログ表示ボタンが押された時のイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onTimeClickDialog(View view) {
        TimePickerDialog dialog = new TimePickerDialog(FemaleNewReservationActivity.this, new TimePickerDialogTimeSetListener(), 0, 0, true);
        dialog.show();
    }


    /**
     * 時間選択ダイアログの完了ボタンが押された時処理が記述されたメンバクラス。
     */
    private class TimePickerDialogTimeSetListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = "";
            if(minute<10){
                time = hourOfDay + "時" + minute + "0分";
            }else{
                time = hourOfDay + "時" + minute + "分";
            }
            EditText etTime = findViewById(R.id.etTime);
            etTime.setText(time);
        }
    }

    /**
     * 次へボタンがクリックされた時。
     * @param view
     */
    public void onFinishReservationClick(View view){

        reservation.setHasNoError(true);

        Spinner spMenu = findViewById(R.id.spMenu);
        reservation.setMenuNo(spMenu.getSelectedItemPosition() + "");

        EditText etDate = findViewById(R.id.etDate);
        reservation.setDate(etDate.getText().toString());

        EditText etTime = findViewById(R.id.etTime);
        reservation.setTime(etTime.getText().toString());

        EditText etMessage = findViewById(R.id.etMessage);
        reservation.setMessage(etMessage.getText().toString());

        if(reservation.isHasNoError()) {
            Intent intent = new Intent(FemaleNewReservationActivity.this, FemaleFinishReservationActivity.class);
            intent.putExtra("reservation", reservation);
            startActivity(intent);
        } else {
            Toast.makeText(FemaleNewReservationActivity.this , "入力チェック完了" , Toast.LENGTH_SHORT).show();
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

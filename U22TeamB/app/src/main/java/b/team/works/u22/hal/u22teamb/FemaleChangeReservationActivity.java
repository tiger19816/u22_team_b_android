package b.team.works.u22.hal.u22teamb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import java.util.HashMap;
import java.util.Map;

public class FemaleChangeReservationActivity extends AppCompatActivity {

    private static final String LOGIN_URL = Word.RESERVATION_DATA_URL;
    private String reservationId;
    private String storeId;
    private String SIGNAL_VERSION = "START";
    private Reservation reservation;

    private Calendar cal;
    private int nowYear;
    private int nowMonth;
    private int nowDayOfMonth;
    private SimpleDateFormat dfBirthday = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat dfDayOfMonth = new SimpleDateFormat("dd");

    private String etReservation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_change_reservation);

        setTitle("予約内容変更");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        this.reservationId = intent.getStringExtra("reservationId");
        this.storeId = intent.getStringExtra("storeId");

        this.SIGNAL_VERSION = "START";

        //非同期処理を開始する。
        ReservationTaskReceiver receiver = new ReservationTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , "false" , reservationId );


    }

    public void onReservationUpdateClick(View view){

        this.SIGNAL_VERSION = "UPDATE";
        Boolean isUpdate = false;

        Reservation reservation2 = new Reservation();
        DataConversion dataConversion = new DataConversion();

        Spinner spMenu = findViewById(R.id.spMenu);
        reservation2.setMenuNo(spMenu.getSelectedItemPosition() + "");
        String menuNo = "";
        if(!reservation.getMenuNo().equals(reservation2.getMenuNo())){
            menuNo = reservation2.getMenuNo();
            isUpdate = true;
        }

        EditText etDate = findViewById(R.id.etDate);
        reservation2.setDate(dataConversion.getDataConversion01(etDate.getText().toString()));
        String date = "";
        if(!reservation.getDate().equals(reservation2.getDate())){
            date = reservation2.getDate();
            isUpdate = true;
        }
        

        EditText etTime = findViewById(R.id.etTime);
        reservation2.setTime(dataConversion.getTimeConversion01(etTime.getText().toString()));
        String time = "";
        if(!reservation.getTime().equals(reservation2.getTime())){
            time = reservation2.getTime();
            isUpdate = true;
        }

        EditText etMessage = findViewById(R.id.etMessage);
        reservation2.setTime(etMessage.getText().toString());
        String message = "";
        if(!reservation.getMessage().equals(reservation2.getMessage())){
            message = reservation2.getMessage();
            isUpdate = true;
        }

        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        tvSubtotal.setText("650");

        TextView tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText("650");

        if(reservation2.isHasNoError()) {
            //非同期処理を開始する。
            ReservationTaskReceiver receiver = new ReservationTaskReceiver();
            //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
            receiver.execute(LOGIN_URL, String.valueOf(isUpdate),  reservationId , menuNo , date , time , message);
        }else{
            Toast.makeText(FemaleChangeReservationActivity.this , "入力チェック完了" , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class ReservationTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";
        private Map map = new HashMap<String , Object>();

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

            String postData = "";
            if("START".equals(SIGNAL_VERSION)) {
                String isStart = params[1];
                String id = params[2];
                //POSTで送りたいデータ(予約ID)
                 postData = "version=" + isStart + "&id=" + id;
            }else{
                String isStart = params[1];
                String id = params[2];
                String menuNo = params[3];
                String date = params[4];
                String time = params[5];
                String message = params[6];
                postData = "version=" + isStart + "&id=" + id + "&menuNo=" + menuNo + "&date=" + date + "&time=" + time + "&message=" + message;
            }

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

                if("START".equals(SIGNAL_VERSION)) {

                    JSONObject rootJSON = new JSONObject(result);

                    //予約情報
                    reservation = new Reservation();

                    //データ変換用クラス。
                    DataConversion dataConversion = new DataConversion();

                    String reservationId = rootJSON.getString("reservationId");
                    reservation.setId(reservationId);

                    TextView tvStoreName = findViewById(R.id.tvStoreName);
                    String storeName = rootJSON.getString("shopName");
                    reservation.setName(storeName);
                    tvStoreName.setText(storeName);

                    Spinner spMenu = findViewById(R.id.spMenu);
                    String menuNo = rootJSON.getString("menuNo");
                    reservation.setMenuNo(menuNo);
                    spMenu.setSelection(Integer.parseInt(menuNo));

                    EditText etDate = findViewById(R.id.etDate);
                    String date = rootJSON.getString("dateTime");
                    etReservation = date;
                    reservation.setDate(date);
                    date = dataConversion.getDataConversion02(date);
                    etDate.setText(date);

                    EditText etMessage = findViewById(R.id.etMessage);
                    String message = rootJSON.getString("message");
                    reservation.setMessage(message);
                    etMessage.setText(message);

                    //予約日時の分解
                    String reservationYear = etReservation.substring(0, 4);//年
                    String reservationMonth = etReservation.substring(5, 7);//月
                    String reservationDay = etReservation.substring(8, 10);//日

                    //現在日時の取得
                    java.util.Calendar cal =  java.util.Calendar.getInstance();
                    int nowYear = cal.get(Calendar.YEAR);
                    int nowMonth = cal.get(Calendar.MONTH);
                    int nowDay = cal.get(Calendar.DAY_OF_MONTH);


                    if (    Integer.valueOf(reservationYear) == nowYear &&
                            Integer.valueOf(reservationMonth) == (nowMonth + 1) &&
                            Integer.valueOf(reservationDay) >= (nowDay+3) ){

                        TextView tv1 = findViewById(R.id.etDate);
                        tv1.setEnabled(false);

                        TextView tv2 = findViewById(R.id.etTime);
                        tv2.setEnabled(false);

                        TextView tv3 = findViewById(R.id.etMessage);
                        tv3.setEnabled(false);

                        Spinner spinner = (Spinner) findViewById(R.id.spMenu);
                        spinner.setEnabled(false);

                        Toast.makeText(FemaleChangeReservationActivity.this , "予約日から3日以内のため変更はできません" , Toast.LENGTH_SHORT).show();

                    }else if( Integer.valueOf(reservationYear) < nowYear ||
                            Integer.valueOf(reservationYear) <= nowYear &&
                            Integer.valueOf(reservationMonth) < (nowMonth + 1) ||
                            Integer.valueOf(reservationYear) <= nowYear &&
                            Integer.valueOf(reservationMonth) <= (nowMonth + 1) &&
                            Integer.valueOf(reservationDay) <= nowDay ){

                        TextView tv1 = findViewById(R.id.etDate);
                        tv1.setEnabled(false);

                        Button button = (Button) findViewById(R.id.ReservationUpdateClick);
                        button.setEnabled(false);

                        TextView tv2 = findViewById(R.id.etTime);
                        tv2.setEnabled(false);

                        TextView tv3 = findViewById(R.id.etMessage);
                        tv3.setEnabled(false);

                        Spinner spinner = (Spinner) findViewById(R.id.spMenu);
                        spinner.setEnabled(false);

                        Toast.makeText(FemaleChangeReservationActivity.this , "過去の予約は変更できません" , Toast.LENGTH_SHORT).show();

                    }

                    EditText etTime = findViewById(R.id.etTime);
                    String time = rootJSON.getString("dateTime");
                    reservation.setTime(time);
                    time = dataConversion.getTimeConversion02(time);
                    etTime.setText(time);

                    int price = 0;
                    if(Integer.parseInt(menuNo) != 0){
                        price = Integer.parseInt(menuNo)-1;
                    }

                    TextView tvSubtotal = findViewById(R.id.tvSubtotal);
                    tvSubtotal.setText(getResources().getStringArray(R.array.sp_reservation_store_menu_price_list)[price]);

                    TextView tvTotal = findViewById(R.id.tvTotal);
                    tvTotal.setText(getResources().getStringArray(R.array.sp_reservation_store_menu_price_list)[price]);
                }
                else{
                    JSONObject rootJSON = new JSONObject(result);
                    Boolean executionResult = rootJSON.getBoolean("result");
                    if(executionResult){
                        Toast.makeText(FemaleChangeReservationActivity.this , "変更完了" , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(FemaleChangeReservationActivity.this , "変更されませんでした。" , Toast.LENGTH_SHORT).show();
                    }
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
    public void onDateClickDialog(View view){
        EditText etBirthday = findViewById(R.id.etDate);
        String Birthday = etBirthday.getText().toString();
        if(!"".equals(Birthday)) {
            try {
                Date date = dfBirthday.parse(Birthday);
                this.nowYear = Integer.parseInt(dfYear.format(date));
                this.nowMonth = Integer.parseInt(dfMonth.format(date))-1;
                this.nowDayOfMonth = Integer.parseInt(dfDayOfMonth.format(date));
                DatePickerDialog dialog = new DatePickerDialog(FemaleChangeReservationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
                dialog.show();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("データ変換失敗", "ダイアログ表示の時");
            }
        }else{
            DatePickerDialog dialog = new DatePickerDialog(FemaleChangeReservationActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
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
            EditText etBirthday = findViewById(R.id.etDate);
            etBirthday.setText(dateMessage);
        }
    }

    /**
     * 時間選択ダイアログ表示ボタンが押された時のイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onTimeClickDialog(View view) {
        TimePickerDialog dialog = new TimePickerDialog(FemaleChangeReservationActivity.this, new TimePickerDialogTimeSetListener(), 0, 0, true);
        dialog.show();
    }


    /**
     * 時間選択ダイアログの完了ボタンが押された時処理が記述されたメンバクラス。
     */
    private class TimePickerDialogTimeSetListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + "時" + minute + "分";
            EditText etTime = findViewById(R.id.etTime);
            etTime.setText(time);
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

package b.team.works.u22.hal.u22teamb;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation implements Serializable {

    private String id;
    private String name;
    private String menuNo;
    private String date;
    private String time;
    private String message;

    private String errorMenuNo;
    private String errorDate;
    private String errorTime;
    private String errorMessage;

    private boolean hasNoError;

    public Reservation() {
        this.id = "";
        this.name = "";
        this.menuNo = "";
        this.date = "";
        this.time = "";
        this.message = "";

        this.errorMenuNo = "";
        this.errorDate = "";
        this.errorTime = "";
        this.errorMessage = "";

        hasNoError = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
        if("0".equals(menuNo)) {
            this.errorMenuNo = "メニューを選択してください。";
            this.hasNoError = false;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        if("".equals(date)) {
            this.errorDate = "日付を選択してください。";
            this.hasNoError = false;
        }
     }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        if("".equals(time)) {
            this.errorTime = "時刻を選択してください。";
            this.hasNoError = false;
        }
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getErrorMenuNo() {
        return errorMenuNo;
    }

    public void setErrorMenuNo(String errorMenuNo) {
        this.errorMenuNo = errorMenuNo;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }

    public String getErrorMessage() { return errorMessage; }

    public boolean isHasNoError() {
        return hasNoError;
    }

    public void setHasNoError(boolean hasError) {
        this.hasNoError = hasError;
    }

    /**
     *yyyy年MM年dd日からyyyy-MM-ddに変換。
     * @return
     */
    public String getDataConversion(){
        String strData = "";
        SimpleDateFormat dfBirthday01 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
        SimpleDateFormat dfBirthday02 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = dfBirthday01.parse(date +  " " + time);
            strData = dfBirthday02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "予約用クラスの時。");
        }
        return strData;
    }
}

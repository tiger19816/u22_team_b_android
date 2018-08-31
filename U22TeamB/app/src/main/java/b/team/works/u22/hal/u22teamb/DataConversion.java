package b.team.works.u22.hal.u22teamb;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * データ変換用クラス。
 *
 * @author takadahonoka
 */

public class DataConversion {

    private static final SimpleDateFormat dfFullDate01 = new SimpleDateFormat("yyyy年MM月dd日 hh時mm分");
    private static final SimpleDateFormat dfFullDate02 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat dfDate01 = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat dfDate02 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dfDate03 = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat dfDate04 = new SimpleDateFormat("MM");
    private static final SimpleDateFormat dfDate05 = new SimpleDateFormat("dd");
    private static final SimpleDateFormat dfTime01 = new SimpleDateFormat("hh時mm分");
    private static final SimpleDateFormat dfTime02 = new SimpleDateFormat("hh:mm:ss");

    /**
     * yyyy-MM-ddから3日前より未来の日付だった場合、false。
     */
    public Boolean getDateCheckConversion01(String date){
        Boolean result = false;
        try {
            //予約日の3日前を求める。
            Date d = dfFullDate02.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DAY_OF_MONTH, -3);
            Date d2 = calendar.getTime();
            //今日の日月を求める。
            Calendar calendar2 = Calendar.getInstance();
            Date d3 = calendar2.getTime();
            //比較する(d2 = 予約日の三日前,d3 = 今日の日月)。
            int diff = d2.compareTo(d3);
            Log.e("確認(d2)", String.valueOf(d2));
            Log.e("確認(d3)", String.valueOf(d3));
            if (diff == 0) {
                result = false;
            }else if (diff > 0) {
                result = true;
            }else{
                result = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのgetFullDataConversion01時。");
        }
        Log.e("確認", String.valueOf(result));
        return result;
    }

    /**
     *yyyy年MM年dd日からyyyy-MM-ddに変換。
     * @param date(String)
     * @return
     */
    public String getFullDataConversion01(String date){
        String strData = "";
        try {
            Date d = dfFullDate01.parse(date);
            strData = dfFullDate02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのgetFullDataConversion01時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからyyyy年MM年dd日に変換。
     * @param date(String)
     * @return String
     */
    public String getFullDataConversion02(String date){
        String strData = "";
        try {
            Date d = dfFullDate02.parse(date);
            strData = dfFullDate01.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのgetFullDataConversion02時。");
        }
        return strData;
    }

    /**
     *yyyy年MM年dd日からyyyy-MM-ddに変換。
     * @param date(String)
     * @return
     */
    public String getDataConversion01(String date){
        String strData = "";
        try {
            Date d = dfDate01.parse(date);
            strData = dfDate02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからyyyy年MM年dd日に変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion02(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate01.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからyyyyに変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion03(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate03.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからMMに変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion04(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate04.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからddに変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion05(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate05.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     *hh時mm分からhh:mm:ssに変換。
     * @param date(String)
     * @return String
     */
    public String getTimeConversion01(String date){
        String strData = "";
        try {
            Date d = dfTime01.parse(date);
            strData = dfTime02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     *hh:mm:ssからhh時mm分に変換。
     * @param date(String)
     * @return String
     */
    public String getTimeConversion02(String date){
        String strData = "";
        try {
            Date fullDate = dfFullDate02.parse(date);
            String time02 = dfTime01.format(fullDate);
            strData = time02;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }
}

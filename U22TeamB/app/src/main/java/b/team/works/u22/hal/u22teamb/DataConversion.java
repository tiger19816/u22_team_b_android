package b.team.works.u22.hal.u22teamb;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * データ変換用クラス。
 *
 * @author takadahonoka
 */

public class DataConversion {

    private static final SimpleDateFormat dfFullDate02 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat dfDate01 = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat dfDate02 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dfTime01 = new SimpleDateFormat("hh時mm分");
    private static final SimpleDateFormat dfTime02 = new SimpleDateFormat("hh:mm:ss");

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
     *hh時mm分からhh:mm:ssに変換。
     * @param date(String)
     * @return String
     */
    public String getTimeConversion01(String date){
        String strData = "";
        try {
            Log.e("日" , date);
            Date d = dfTime02.parse(date);
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

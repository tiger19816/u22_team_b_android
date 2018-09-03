package b.team.works.u22.hal.u22teambstore;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * データ変換用クラス。
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
    private static final SimpleDateFormat dfTime03 = new SimpleDateFormat("hh");
    private static final SimpleDateFormat dfTime04 = new SimpleDateFormat("mm");
    private static final SimpleDateFormat dfTime05 = new SimpleDateFormat("mm");

    /**
     * 住所から、緯度経度を取得するメソッド。
     * @param address
     * @return
     */
    public List<String> getLatLongFromAddress(Context context , String address)
    {
        Geocoder geoCoder = new Geocoder(context , Locale.getDefault());
        List<String> latLong = new ArrayList<String>();
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0) {
                latLong.add(String.valueOf(addresses.get(0).getLatitude()));
                latLong.add(String.valueOf(addresses.get(0).getLongitude()));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return latLong;
        }

        return latLong;
    }

}

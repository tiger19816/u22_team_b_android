package b.team.works.u22.hal.u22teambstore;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * データ変換用クラス。
 */

public class DataConversion {

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

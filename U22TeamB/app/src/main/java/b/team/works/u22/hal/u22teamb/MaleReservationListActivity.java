package b.team.works.u22.hal.u22teamb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaleReservationListActivity extends AppCompatActivity {

    private List<Map<String , Object>> _list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_reservation_list);

        setTitle("予約一覧");

        _list = createList();

        String[] from = {"storeName", "reservationDate"};
        int[] to = {R.id.tvStoreName, R.id.tvReservationDate};
        SimpleAdapter adapter = new SimpleAdapter(MaleReservationListActivity.this, _list, R.layout.row_reservation, from, to);
        adapter.setViewBinder(new CustomViewBinder());
        ListView lvReservationList = findViewById(R.id.lvReservationList);
        lvReservationList.setAdapter(adapter);

    }

    /**
     * リストビューのカスタムビューバインダークラス。
     */
    private class CustomViewBinder implements SimpleAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view , Object data , String textRepresentation){
            int viewId = view.getId();
            switch (viewId){
                case R.id.tvStoreName:
                    TextView tvStoreName = (TextView) view;
                    String strStoreName = (String) data;
                    tvStoreName.setText(strStoreName);
                    return true;
                case R.id.tvReservationDate:
                    TextView tvReservationDate = (TextView) view;
                    String strReservationDate = (String) data;
                    tvReservationDate.setText(strReservationDate);
                    return true;
            }
            return false;
        }
    }

    /**
     * リストビューに表示させるリストデータを生成するメソッド。
     * @return List<Map<String , Object>> 生成されたリストデータ。
     */
    private List<Map<String , Object>> createList(){
        List<Map<String , Object>> list = new ArrayList<>();

        for (int i=0; i<=30; i++){
            Map<String , Object> map = new HashMap<>();
            map.put("storeName" , "店名");
            map.put("reservationDate" , "99月99日(月) 99時99分");
            list.add(map);
        }
        return list;
    }
}

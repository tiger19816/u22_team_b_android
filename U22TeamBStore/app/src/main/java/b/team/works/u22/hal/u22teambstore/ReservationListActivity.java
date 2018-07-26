package b.team.works.u22.hal.u22teambstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * #35 予約リストを表示し、選択するとQRコードリーダが起動するアクティビティ。
 *
 * @author Yuki Yoshida
 */
public class ReservationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);
    }
}

package b.team.works.u22.hal.u22teamb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FemaleNewReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_new_reservation);

        setTitle( "予約内容入力" );

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}

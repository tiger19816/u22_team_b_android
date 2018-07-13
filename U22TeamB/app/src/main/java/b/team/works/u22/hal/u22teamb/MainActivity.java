package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private String USER_TYPE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewUserPageClick(View view){

//        Intent intent;
//
//        if ("".equals("")) {
//            //ログインが妻の時。
//            USER_TYPE = "FEMALE";
//            intent = new Intent(MainActivity.this , FemaleStoreMapListActivity.class);
//        }else{
//            //ログインが夫の時。
//            USER_TYPE = "MALE";
//            intent = new Intent(MainActivity.this , MaleReservationListActivity.class);
//        }
//        startActivity(intent);
    }
}

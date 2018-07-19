package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FemaleHistoryListActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private List<Map<String , Object>> _list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_history_list);

        setTitle("履歴一覧");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //レフトナビ本体。
        NavigationView navigationView = findViewById(R.id.nvSideMenuButton);
        navigationView.setNavigationItemSelectedListener(this);

        _list = createList();

        String[] from = {"storeName" , "historyPrice" , "historyDate"};
        int[] to = {R.id.tvStoreName , R.id.tvPrice , R.id.tvHistoryDate};
        SimpleAdapter adapter = new SimpleAdapter(FemaleHistoryListActivity.this , _list , R.layout.row_history , from , to);
        adapter.setViewBinder(new CustomViewBinder());
        ListView lvHistoryList = findViewById(R.id.lvHistoryList);
        lvHistoryList.setAdapter(adapter);

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
                case R.id.tvPrice:
                    TextView tvPrice = (TextView) view;
                    String strPrice = (String) data;
                    tvPrice.setText(strPrice);
                    return true;
                case R.id.tvHistoryDate:
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
            map.put("historyPrice" , "利用額:¥1,200");
            map.put("historyDate" , "9999年99月99日(月) 99時99分");
            list.add(map);
        }
        return list;
    }

    /**
     * レフトナビ以外をクリックした時の動き。
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * レフトナビをクリックした時。
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_map) {
            intent = new Intent(FemaleHistoryListActivity.this,FemaleStoreMapListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_reservation) {
            intent = new Intent(FemaleHistoryListActivity.this,FemaleReservationListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(FemaleHistoryListActivity.this,FemaleHistoryListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_page) {
            intent = new Intent(FemaleHistoryListActivity.this,FemaleStoreMapListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
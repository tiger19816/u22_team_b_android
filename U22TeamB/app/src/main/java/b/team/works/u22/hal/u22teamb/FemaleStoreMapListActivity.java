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

public class FemaleStoreMapListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_store_map_list);

        setTitle("店一覧");

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

    }

    /**
     *予約へのボタンがクリックされた時。
     * @param view
     */
    public void onNextReservationClick(View view){
        Intent intent = new Intent(FemaleStoreMapListActivity.this,FemaleNewReservationActivity.class);
        startActivity(intent);
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
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleStoreMapListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_reservation) {
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleReservationListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleHistoryListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_page) {
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleMyPageActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            intent = new Intent(FemaleStoreMapListActivity.this,MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

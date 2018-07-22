package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FemaleStoreMapListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private ListView linearContentsArea;    // 内容エリア]
    private LinearLayout linearLayoutArea;
    private FloatingActionButton fab;
    private final static int DURATION = 400;    // アニメーションにかける時間(ミリ秒)
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_store_map_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.storeMaps);
        mapFragment.getMapAsync(this);

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

        // 内容エリアの結び付け
        linearContentsArea = findViewById(R.id.lvStoreList);
        // ボタンの結び付け
        fab = findViewById(R.id.fabOpenList);

        linearLayoutArea = findViewById(R.id.llStoreMapMain);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearLayoutArea.getHeight() / 2;

        // 内容エリアを閉じるアニメーション
        FemaleStoreMapAnimation closeAnimation = new FemaleStoreMapAnimation(linearContentsArea, -originalHeight, originalHeight);
        closeAnimation.setDuration(DURATION);
        linearContentsArea.startAnimation(closeAnimation);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(34.699886, 135.493033);
        mMap.addMarker(new MarkerOptions().position(sydney).title("HAL大阪"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
            intent = new Intent(FemaleStoreMapListActivity.this,FemaleStoreMapListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            intent = new Intent(FemaleStoreMapListActivity.this,MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 内容エリアの結び付け
        linearContentsArea = findViewById(R.id.lvStoreList);
        // ボタンの結び付け
        fab = findViewById(R.id.fabOpenList);

        // ExpandするViewの元のサイズを保持
        final int originalHeight = linearLayoutArea.getHeight() / 2;

        // 展開ボタン押下時
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (linearContentsArea.getHeight() > 0) {       // 内容エリアが開いている時

                    // 内容エリアを閉じるアニメーション
                    FemaleStoreMapAnimation closeAnimation = new FemaleStoreMapAnimation(linearContentsArea, -originalHeight, originalHeight);
                    closeAnimation.setDuration(DURATION);
                    linearContentsArea.startAnimation(closeAnimation);
                } else {

                    // 内容エリアが閉じている時、内容エリアを開くアニメーション
                    FemaleStoreMapAnimation openAnimation = new FemaleStoreMapAnimation(linearContentsArea, originalHeight, 0);
                    openAnimation.setDuration(DURATION);    // アニメーションにかける時間(ミリ秒)
                    linearContentsArea.startAnimation(openAnimation);   // アニメーション開始
                }
            }
        });
    }

    /**
     * フローティングアクションボタンが押された時のイベント処理用メソッド.
     *
     * @param view 画面部品。
     */
    public void onFabOpenListClick(View view) {
    }
}

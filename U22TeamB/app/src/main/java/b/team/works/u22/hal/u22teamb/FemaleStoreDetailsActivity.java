package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 【妻】店の詳細画面のアクティビティクラス.
 *
 * @author Taiga Hirai
 */
public class FemaleStoreDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, StoreDetailsTab1Fragment.OnFragmentInteractionListener, StoreDetailsTab2Fragment.OnFragmentInteractionListener, OnMapReadyCallback {

    public GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_store_details);

        setTitle("店舗詳細");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //戻るボタンの表示。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        String storeId = intent.getStringExtra("id");

        //xmlからTabLayoutの取得
        TabLayout tabLayout = findViewById(R.id.tabs);

        //xmlからViewPagerを取得
        ViewPager viewPager = findViewById(R.id.pager);

        //ページタイトル配列
        final String[] pageTitle = {getString(R.string.tab_title_detail), getString(R.string.tab_title_map)};

        //表示Pageに必要な項目を設定
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            /**
             * Tabの内を売を設定するメソッド.
             * switch文で設定するFragmentを分けている。
             *
             * @param position 設定するTabのページ番号(0から)。
             * @return Tabページに設定するFragment。
             */
            @Override
            public Fragment getItem(int position) {
                //各タブに設定するFragmentを選択する。
                switch (position) {
                    case 0:
                        return StoreDetailsTab1Fragment.newInstance(position + 1);
                    case 1:
                        return StoreDetailsTab2Fragment.newInstance(position + 1);
                    default:
                        return StoreDetailsTab1Fragment.newInstance(position + 1);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageTitle[position];
            }

            @Override
            public int getCount() {
                return pageTitle.length;
            }
        };
        //ViewPagerにページを設定
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        //ViewPagerをTabLayoutを設定
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     *予約へのボタンがクリックされた時。
     * @param view
     */
    public void onNextReservationClick(View view){
        Intent intent = new Intent(FemaleStoreDetailsActivity.this,FemaleNewReservationActivity.class);
        startActivity(intent);
    }

    /**
     * オプションメニューのアイテムが押された時.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(34.699886, 135.493033);
        mMap.addMarker(new MarkerOptions().position(latLng).title("HAL大阪").visible(true)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    //以下の内容を実装する必要がある。（空で問題ない）

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}

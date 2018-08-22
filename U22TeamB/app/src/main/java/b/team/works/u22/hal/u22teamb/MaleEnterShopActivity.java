package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AndroidRuntimeException;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;

public class MaleEnterShopActivity extends AppCompatActivity {

    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_enter_shop);

        setTitle("QRコード表示");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String storeName = intent.getStringExtra("storeName");
        _id = intent.getStringExtra("maleId");

        TextView tvReservationStoreName = findViewById(R.id.tvReservationStoreName);
        tvReservationStoreName.setText(storeName);


        //QRコード化する文字列が1文字以上であるか確認
        if(0 < _id.length()) {
            //QRコード画像の大きさを指定(pixel)
            int size = 1000;

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                HashMap hints = new HashMap();

                //QRコードの文字コードを指定（日本語を扱う場合は"Shift_JIS"。
                hints.put(EncodeHintType.CHARACTER_SET, "Shift_JIS");

                //QRコードをBitmapで作成。
                Bitmap bitmap = barcodeEncoder.encodeBitmap(_id, BarcodeFormat.QR_CODE, size, size, hints);

                //作成したQRコードを画面上に配置。
                ImageView imQrCode = findViewById(R.id.imQrCode);
                imQrCode.setImageBitmap(bitmap);

            } catch (WriterException e) {
                throw new AndroidRuntimeException("Barcode Error.", e);
            }
        }
    }

    /**
     * アクションバー。
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

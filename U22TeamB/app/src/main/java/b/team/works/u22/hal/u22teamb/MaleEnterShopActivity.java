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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;

public class MaleEnterShopActivity extends AppCompatActivity {

    private String _id;
    private String _newInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyCustomTheme_Default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_enter_shop);

        setTitle(getString(R.string.male_enter_shop_title));

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
        _newInfo = intent.getStringExtra("newInformation");

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

        if("1".equals(_newInfo)) {
            gifStart();
        }
    }

    /**
     * GIF画像をToastで表示。
     */
    public void gifStart(){
        // ImageViewをToast表示する
        ImageView imageView = new ImageView(MaleEnterShopActivity.this);
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.gifsample01).into(target);
        Toast toast = new Toast(MaleEnterShopActivity.this);
        toast.setView(imageView);
        /**
         * 第1引数 : 水平方向のマージンを指定します。数値は-1~1の間で設定できます。正の値は水平右方向、負の値は水平左方向に移動されます。
         * 第2引数 : 垂直方向のマージンを指定します。数値は-1~1の間で設定できます。正の値は垂直上方向、負の値は垂直下方向に移動されます。
         */
        toast.setMargin(1.0f,-1.0f);
        toast.show();
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

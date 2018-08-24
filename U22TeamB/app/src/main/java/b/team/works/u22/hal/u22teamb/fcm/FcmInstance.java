package b.team.works.u22.hal.u22teamb.fcm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * プッシュ通知利用の手順。
 *
 * ① 以下より、Google Firebaseサービスに登録。
 * --------------------------------------------------
 *      https://firebase.google.com/?hl=ja
 * --------------------------------------------------
 * ② Firebase上でプロジェクトを作成。
 * ③ プロジェクトに該当Androidプロジェクトのパッケージ名を追加。
 * ④ Firebaseコンソール上から該当Androidプロジェクトに対応する「google-services.json」をダウンロードし、該当Androidプロジェクト下の「app\src」に配置。
 * ⑤ 「AndroidManifest.xml」の<Application>内に以下を追記。但し、meta-data上のパラメータは適当な値を入力。
 * --------------------------------------------------
 * <service android:name=".MyInstanceIDListenerService">
 *     <intent-filter>
 *         <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
 *     </intent-filter>
 * </service>
 * <service android:name=".FcmInstance$MyFcmListenerService">
 *     <intent-filter>
 *         <action android:name="com.google.firebase.MESSAGING_EVENT"/>
 *     </intent-filter>
 * </service>
 * <meta-data android:name="com.google.firebase.messaging.default_notification_icon" android:resource="@android:drawable/ic_notification_overlay" />
 * <meta-data android:name="com.google.firebase.messaging.default_notification_color" android:resource="@android:color/holo_red_light" />
 * <meta-data android:name="com.google.firebase.messaging.default_notification_channel_id" android:value="notification_channel"/>
 * <meta-data android:name="firebase_messaging_auto_init_enabled" android:value="false" />
 * <meta-data android:name="firebase_analytics_collection_enabled"android:value="false" />
 * --------------------------------------------------
 * ⑥ 該当Androidプロジェクト直下の「build.gradle」上で「buildscript{dependencies{}}」内に以下を追記。
 * --------------------------------------------------
 * classpath 'com.google.gms:google-services:4.0.1'
 * --------------------------------------------------
 * ⑦ 該当Androidプロジェクト直下の「build.gradle」上で「allprojects{repositories{}}」内に以下を追記。
 * --------------------------------------------------
 * maven {
 *     url "https://maven.google.com"
 * }
 * --------------------------------------------------
 * ⑧ 該当Androidプロジェクト下、「app」内の「build.gradle」上で「dependencies{}」内に以下を追記。
 * --------------------------------------------------
 * implementation "com.google.android.gms:play-services-base:15.0.1"
 * implementation 'com.google.firebase:firebase-core:16.0.1'
 * implementation 'com.google.firebase:firebase-messaging:17.1.0'
 * implementation 'com.google.android.gms:play-services-gcm:15.0.1'
 * --------------------------------------------------
 * ⑨ 該当Androidプロジェクト下、「app」内の「build.gradle」上で、最下段に以下を追記。
 * --------------------------------------------------
 * apply plugin: 'com.google.gms.google-services'
 * --------------------------------------------------
 * ⑩ 該当Androidプロジェクトを、クリーンしリビルド。
 *
 * @author Yuki Yoshida
 */
public class FcmInstance {
    private String _token;

    /**
     * コンストラクタFcmInstance()の引数に当メソッドを代入する。
     *
     * @return トークン。
     */
    public static final String setChannel() {
        /**
         * 起動アクティビティで実行しなければならないメソッド群
         */
        MyInstanceIDListenerService service = new MyInstanceIDListenerService();
        service.onTokenRefresh();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        String token = FirebaseInstanceId.getInstance().getToken();

        String channelId = "u22teamb_customer";
        String channelName = "notify_u22teamb_customer";
        String channelDescription = "U22夫管理アプリのチャンネル";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelManager ncm = new NotificationChannelManager();
            ncm.create(
                    getApplicationContext(),
                    channelId,
                    channelName,
                    channelDescription);
        }

        return token;
    }

    /**
     * コンストラクタ。
     * 例: FcmInstance(FcmInstance.setChannel)
     *
     * @param token トークンID。
     */
    public FcmInstance(String token) {
        this._token = token;
    }

    /**
     * サーバへトークンを送信するメソッド。
     *
     * @param view 画面部品。
     */
    public void onSendClick(View view) {
        HttpResponsAsync hra = new HttpResponsAsync(this);
        hra.execute(this._token);   // パラメータをサーブレットへ送信。
    }


    public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

        private final String TAG = "インスタンスID";

        /**
         * トークンを更新するメソッド。
         */
        @Override
        public void onTokenRefresh() {
            // インスタンスIDの更新。
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "生成されたトークン:" + refreshedToken);
        }

    }

    public class NotificationChannelManager {

        /**
         * 通知チャンネルを設定するメソッド。Android O以降で必須。
         *
         * @param context アプリケーションコンテキスト。
         * @param channelId チャンネルID。
         * @param channelTitle チャンネル名。
         * @param channelDescription チャンネルの説明。
         */
        @TargetApi(Build.VERSION_CODES.O)
        public void create(Context context, String channelId, String channelTitle, String channelDescription) {
            String title = channelTitle;
            String description = channelDescription;

            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public class HttpResponsAsync extends AsyncTask<String, Void, Void> {

        private Activity _activity;

        /**
         * コンストラクタ。
         *
         * @param activity 遷移元アクティビティ。
         */
        public HttpResponsAsync(Activity activity) {

            this._activity = activity;

        }

        /**
         * サーブレットへパラメータを送信するメソッド。
         *
         * @param token ①発行されたトークン
         * @return null
         */
        @Override
        protected Void doInBackground(String... token) {
            HttpURLConnection cnct = null;
            URL url = null;
            OutputStream opStream = null;
            String urlStr = "http://10.0.2.2:8080/U22Verification/Verification01Servlet";   // 接続先URL

            String postData = "token=" + token[0];  // POSTで送りたいデータ

//            Log.d("URL", "アクセス先: " + urlStr + "?" + postData);

            try {
                url = new URL(urlStr);  // URLの作成
                cnct = (HttpURLConnection) url.openConnection(); // 接続用HttpURLConnectionオブジェクト作成
                cnct.setRequestMethod("POST");  // リクエストメソッドの設定
                cnct.setInstanceFollowRedirects(false); // リダイレクトの可否
                cnct.setDoOutput(true); // データ書き込みの可否
                cnct.setReadTimeout(10000); // タイムアウトまでの時間（読込）
                cnct.setConnectTimeout(20000);  // タイムアウトまでの時間（接続）
                cnct.setUseCaches(false);   // キャッシュ使用の可否

                cnct.connect(); // 接続

                try {
                    opStream = cnct.getOutputStream();

                    // 送信する値をByteデータに変換する（UTF-8）
                    opStream.write(postData.getBytes("UTF-8"));
                    opStream.flush();

                    switch (cnct.getResponseCode()) {
                        case HttpURLConnection.HTTP_OK :
                            Log.d("URL", "コネクション状況: 成功");
                            break;
                        case HttpURLConnection.HTTP_INTERNAL_ERROR:
                            Log.e("URL", "エラー内容: 500 内部サーバーエラー");
                            break;
                        default:
                            Log.e("URL", "コネクションレスポンスコード: " + cnct.getResponseCode());
                            break;
                    }
                }
                catch (Exception e) {
                    Log.e("URL", "POST送信エラー: " + e.toString());
                }
                finally {

                    if(opStream != null) {
                        try {
                            opStream.close();
                        }
                        catch (Exception e) {
                            Log.e("URL", "OutputStream解放失敗: " + e.toString());
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("URL", "コネクションエラー: " + e.toString());
            } finally {
                if (cnct != null) {
                    cnct.disconnect();
                }
            }

            return null;
        }

        /**
         * サーブレットへアクセスした後に実行されるメソッド。
         */
        @Override
        public void onPostExecute(Void param) {
            Log.d("HTTP", "onPostExecute: 通過");
        }

    }
}

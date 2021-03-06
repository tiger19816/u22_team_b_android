package b.team.works.u22.hal.u22teamb.fcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import b.team.works.u22.hal.u22teamb.R;

public class MyFcmListenerService extends FirebaseMessagingService {

    private final static String TAG = "Push通知";

    /**
     * Firebaseを利用し、サーバから通知を受信するメソッド。
     *
     * @param message サーバから受信する通知内容。
     */
    @Override
    public void onMessageReceived(RemoteMessage message){
        String msg = message.getNotification().getBody();
        Log.d(TAG, "サーバからの通知:" + msg);
        sendNotification("お知らせ", msg);
    }

    /**
     * 通知を表示するメソッド。
     *
     * @param subTitle 小タイトル。
     * @param message 表示内容。
     */
    private void sendNotification(String subTitle, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // アイコン。
                .setContentTitle("U22夫管理アプリからの通知")  // タイトル。
                .setContentText(message)    // メッセージ。
                .setSubText(subTitle)  // メッセージの概要。
                .setPriority(NotificationCompat.PRIORITY_MAX)   // 表示の優先度。
//                .setSound(defaultSoundUri)    // 通知音の設定(?)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message)) // 表示スタイルの設定(?)
//                .setContentIntent(pendingIntent)  // タップした際の遷移先インテントの設定(?)
                ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // SDKバージョンが26以上の場合、通知チャンネルの設定を行う。
            notificationBuilder.setChannelId("u22teamb_customer");
        }

        notificationManager.notify(0 , notificationBuilder.build());    // チャンネルがnull?
    }

}

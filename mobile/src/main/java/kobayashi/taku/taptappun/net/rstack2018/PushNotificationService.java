package kobayashi.taku.taptappun.net.rstack2018;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // FCMメッセージを受信したときに呼び出される
        Gson gson = new Gson();
        Log.d(Config.TAG, remoteMessage.getMessageId());
        Log.d(Config.TAG, remoteMessage.getFrom());
        Log.d(Config.TAG, remoteMessage.getTo());
        Log.d(Config.TAG, remoteMessage.getCollapseKey());
        Log.d(Config.TAG, remoteMessage.getMessageType());
        Log.d(Config.TAG, String.valueOf(remoteMessage.getSentTime()));
        for(Map.Entry<String, String> kv : remoteMessage.getData().entrySet()){
            Log.d(Config.TAG, kv.getKey() + ":" + kv.getValue());
        }


        // プッシュメッセージのdataに含めた値を取得
        Map<String, String> data = remoteMessage.getData();
        String contentId = data.get("id");
        String contentType = data.get("type");

        // Notificationを生成
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS);
        builder.setAutoCancel(true);

        // タップ時に呼ばれるIntentを生成
        Intent intent = new Intent(this, PushNotificationService.class);
        intent.putExtra("type", contentType);
        intent.putExtra("id", contentId);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Notification表示
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Integer.parseInt(contentId), builder.build());
    }
}

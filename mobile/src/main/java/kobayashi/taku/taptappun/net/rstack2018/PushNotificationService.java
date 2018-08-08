package kobayashi.taku.taptappun.net.rstack2018;

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
    }
}

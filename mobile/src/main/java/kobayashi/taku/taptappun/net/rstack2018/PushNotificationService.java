package kobayashi.taku.taptappun.net.rstack2018;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class PushNotificationService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // FCMメッセージを受信したときに呼び出される
        Gson gson = new Gson();
        Log.d(Config.TAG, remoteMessage.getMessageId());
        Log.d(Config.TAG, gson.toJson(remoteMessage.getData()));
    }
}

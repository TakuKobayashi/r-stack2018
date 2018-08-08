package kobayashi.taku.taptappun.net.rstack2018;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import net.taptappun.taku.kobayashi.runtimepermissionchecker.RuntimePermissionChecker;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task<InstanceIdResult> instanceIdTask = FirebaseInstanceId.getInstance().getInstanceId();
        instanceIdTask.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                HttpRequestTask request = new HttpRequestTask();
                HashMap<String, Object> urlQueries = new HashMap<String, Object>();
                urlQueries.put("type", "drink");
                urlQueries.put("drink", "beer");
                urlQueries.put("token", task.getResult().getToken());
                request.addCallback(new HttpRequestTask.ResponseCallback() {
                    @Override
                    public void onSuccess(String url, ResponseBody response) {
                        Log.d(Config.TAG, url);
                        try {
                            Log.d(Config.TAG, response.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                request.setParams(urlQueries);
                request.execute(Config.ROOT_URL + "/demo/");
            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        RuntimePermissionChecker.requestAllPermissions(this, 1);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}

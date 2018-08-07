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
                Log.d(Config.TAG, "00000000000000000000000000000000000000");
                Log.d(Config.TAG, task.getResult().getToken());
                Log.d(Config.TAG, task.getResult().getId());
            }
        });
        Log.d(Config.TAG, "---------------------");
        Log.d(Config.TAG, FirebaseInstanceId.getInstance().getId());

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

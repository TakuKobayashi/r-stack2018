package kobayashi.taku.taptappun.net.rstack2018;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import net.taptappun.taku.kobayashi.runtimepermissionchecker.RuntimePermissionChecker;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private MenuImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ImageView bgImage = (ImageView) findViewById(R.id.bg_image);
        bgImage.setImageResource(R.mipmap.hinoki_bg);

        mAdapter = new MenuImageAdapter(this);
        GridView menuGridView = (GridView) findViewById(R.id.menu_gridView);
        menuGridView.setAdapter(mAdapter);
        menuGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int nLastVisibleNum = firstVisibleItem + visibleItemCount;
                if(nLastVisibleNum >= totalItemCount){
                    nLastVisibleNum = totalItemCount + 1;
                }
                //メモリリーク対策
                mAdapter.nonUsingImageRelease(firstVisibleItem - 1,nLastVisibleNum);
            }
        });
        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("注文しますか?")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                                HttpRequestTask request = new HttpRequestTask();
                                HashMap<String, Object> urlQueries = new HashMap<String, Object>();
                                urlQueries.put("type", "drink");
                                urlQueries.put("drink", "beer");
                                urlQueries.put("token", sp.getString("pushNotificationToken", ""));
                                request.addCallback(new HttpRequestTask.ResponseCallback() {
                                    @Override
                                    public void onSuccess(String url, ResponseBody response) {
                                        Log.d(Config.TAG, url);
                                        try {
                                            String reponseBody = response.string();
                                            String sanitized = reponseBody.replaceAll("^\"(.*)\"$", "$1");
                                            Log.d(Config.TAG, sanitized);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                request.setParams(urlQueries);
                                request.execute(Config.ROOT_URL + "/demo/");
                            }
                        })
                        .show();
            }
        });

        Task<InstanceIdResult> instanceIdTask = FirebaseInstanceId.getInstance().getInstanceId();
        instanceIdTask.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("pushNotificationToken", task.getResult().getToken());
                editor.apply();
            }
        });

        RuntimePermissionChecker.requestAllPermissions(this, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.release();
        Util.releaseImageView((ImageView) findViewById(R.id.bg_image));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}

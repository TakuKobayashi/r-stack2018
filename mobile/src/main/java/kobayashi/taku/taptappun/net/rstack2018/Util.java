package kobayashi.taku.taptappun.net.rstack2018;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    //ImageViewを使用したときのメモリリーク対策
    public static void releaseImageView(ImageView imageView){
        if (imageView != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable)(imageView.getDrawable());
            if (bitmapDrawable != null) {
                bitmapDrawable.setCallback(null);
            }
            imageView.setImageBitmap(null);
        }
    }

    //WebViewを使用したときのメモリリーク対策
    public static void releaseWebView(WebView webview){
        webview.stopLoading();
        webview.setWebChromeClient(null);
        webview.setWebViewClient(null);
        webview.destroy();
        webview = null;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static boolean isAssetFileIsDirectory(Context context, String path) {
        AssetManager mngr = context.getAssets();
        boolean isDirectory = false;
        try {
            if (mngr.list(path).length > 0){ //子が含まれる場合はディレクトリ
                isDirectory = true;
            } else {
                // オープン可能かチェック
                mngr.open(path);
            }
        } catch (FileNotFoundException fnfe) {
            isDirectory = true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return isDirectory;
    }

    public static Bitmap loadImageFromAsset(Context context, String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        AssetManager mngr = context.getAssets();
        try {
            InputStream is = mngr.open(path);
            Bitmap image = BitmapFactory.decodeStream(is, null, options);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] loadFilePathes(Context context, String path){
        AssetManager mngr = context.getAssets();
        try {
            return  mngr.list(path);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new String[]{};
    }

    public static Rect getDisplaySize(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return new Rect(0,0,point.x, point.y);
    }

    public static Rect getRealDisplaySize(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point(0, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Android 4.2~
            display.getRealSize(point);
            return new Rect(0,0,point.x, point.y);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Android 3.2~
            try {
                Method getRawWidth = Display.class.getMethod("getRawWidth");
                Method getRawHeight = Display.class.getMethod("getRawHeight");
                int width = (Integer) getRawWidth.invoke(display);
                int height = (Integer) getRawHeight.invoke(display);
                point.set(width, height);
                return new Rect(0,0,point.x, point.y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Rect(0,0,point.x, point.y);
    }

    public static Bitmap bitmapScaled(Bitmap orgImage, int width, int height){
        Bitmap scaledImage = Bitmap.createScaledBitmap(orgImage, width, height, true);
        return scaledImage;
    }

    public static Bitmap horizontalMirror(Bitmap orgImage){
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(orgImage, 0, 0, orgImage.getWidth(), orgImage.getHeight(), matrix, false);
    }

    public static Bitmap getViewCapture(View view) {
        view.setDrawingCacheEnabled(true);

        // Viewのキャッシュを取得
        Bitmap cache = view.getDrawingCache();
        Bitmap screenShot = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return screenShot;
    }

    public static Bitmap getBitmapFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

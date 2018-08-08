package kobayashi.taku.taptappun.net.rstack2018;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MenuImageAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList<Drink> mDrinks = new ArrayList<Drink>();
    private Bitmap[] mImages;

    public MenuImageAdapter(Activity activity){
        mActivity = activity;
        loadMasterDataCSV();
        mImages = new Bitmap[mDrinks.size()];
    }

    private Bitmap loadImage(String path){
       return Util.loadImageFromAsset(mActivity, path);
    }

    @Override
    public int getCount() {
        return mDrinks.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mActivity.getLayoutInflater().inflate(R.layout.menu_cell, null);
        }
        Drink drink = mDrinks.get(position);
        if(mImages[position] == null){
            mImages[position] = loadImage(drink.imagePath);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.icon_view);
        iconImageView.setImageBitmap(mImages[position]);

        TextView menuTitleView = (TextView) convertView.findViewById(R.id.menu_title);
        menuTitleView.setText(drink.title);

        TextView menuDescriptionView = (TextView) convertView.findViewById(R.id.menu_description);
        menuDescriptionView.setText(drink.description);

        return convertView;
    }

    //使用していない画像は全てreleaseしてメモリを空ける
    public void nonUsingImageRelease(int nFirstVisible,int nLastVisible){
        for(int i = 0;i < nFirstVisible;++i){
            if(mImages[i] != null){
                mImages[i].recycle();
                mImages[i] = null;
            }
        }
        for(int i = nLastVisible;i < mImages.length;++i){
            if(mImages[i] != null){
                mImages[i].recycle();
                mImages[i] = null;
            }
        }
    }

    public void release(){
        for(int i = 0;i < mImages.length;i++){
            if(mImages[i] != null){
                mImages[i].recycle();
                mImages[i] = null;
            }
        }
    }

    private void loadMasterDataCSV(){
        AssetManager assetManager = mActivity.getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("mst_drink.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                //カンマ区切りで１つづつ配列に入れる
                Drink drink = new Drink();
                String[] rowData = line.split(",");
                drink.title = rowData[0];
                drink.description = rowData[1];
                drink.imagePath = rowData[2];
                mDrinks.add(drink);
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

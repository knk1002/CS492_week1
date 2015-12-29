package kaist.test.TapC;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import kaist.test.R;

/**
 * Created by Sia on 2015-12-29.
 */
public class CustomListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<TestActivity.CrawlingData> data;
    private int layout;

    public CustomListViewAdapter(Context context, int layout, ArrayList<TestActivity.CrawlingData> data){
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getComicName();}
    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
        {
            convertView=inflater.inflate(layout,parent,false);
        }

        TestActivity.CrawlingData listviewitem = data.get(position);
        ImageView icon = (ImageView)convertView.findViewById(R.id.imageview);
        TextView name = (TextView)convertView.findViewById(R.id.textview);
        name.setText(listviewitem.getComicName());
        return convertView;
    }
}



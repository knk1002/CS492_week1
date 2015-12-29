package kaist.test.TapB;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kaist.test.R;
import kaist.test.TapC.TestActivity;

/**
 * Created by Akrso on 15. 12. 29..
 */
public class ImageAdapter extends BaseAdapter {

    private Context m_Context;
    private ArrayList<Bitmap> data;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, ArrayList<Bitmap> data)
    {
        this.m_Context = context;
        this.data = data;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount(){return data.size();}
    @Override
    public Bitmap getItem(int position){return data.get(position);}
    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        Bitmap gridviewitem = data.get(position);
        if(convertView == null)
        {
            imageView = new ImageView(m_Context);
            imageView.setLayoutParams(new GridView.LayoutParams(300,300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(gridviewitem);
        return imageView;
    }

}

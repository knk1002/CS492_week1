package kaist.test.TapB;

/**
 * Created by Akrso on 15. 12. 30..
 */

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kaist.test.R;
/**
 * Created by Akrso on 15. 12. 29..
 */
@SuppressLint("ValidFragment")
public class GalleryActivity extends Fragment
{

    Context mContext;

    private GridView m_GridView;
    private ListAdapter adapter;
    Document doc;
    ArrayList<Bitmap> temp = new ArrayList<Bitmap>();

    public GalleryActivity()
    {

    }



    public GalleryActivity(Context context)
    {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_gallery, null);

        m_GridView = (GridView) view.findViewById(R.id.gridView);
        ImageAdapter adapter = new ImageAdapter(getContext(),temp);

        m_GridView.setAdapter(adapter);

        TestAsyncTask jsoupAsyncTask = new TestAsyncTask();
        jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }
    private class TestAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect("http://marumaru.in/c/1").userAgent("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52").get();
                Elements items = doc.select("div.picbox");
                for (Element item : items) {
                    Elements elems2 = item.getElementsByClass("crop");
                    for (Element elem2 : elems2)
                    {
                        Elements elems3 = item.getElementsByTag("img");
                        for (Element elem3 : elems3)
                        {
                            String img_url = elem3.attr("src");
                            Bitmap temp_img = downloadImage(img_url);
                            temp.add(temp_img);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            m_GridView.requestLayout();
        }

        private Bitmap downloadImage(String url)
        {
            final int MAX_IMAGE_SIZE = 500;

            try {
                byte[] datas = getImageDataFromUrl( new URL(url) );

                // CHECK IMAGE SIZE BEFORE LOAD
                int scale = 1;
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);

                // IF IMAGE IS BIGGER THAN MAX, DOWN SAMPLING
                if (opts.outHeight > MAX_IMAGE_SIZE || opts.outWidth > MAX_IMAGE_SIZE) {
                    scale = (int)Math.pow(2, (int)Math.round(Math.log(MAX_IMAGE_SIZE/(double)Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
                }
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = scale;

                return BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
            } catch (IOException e) {
                return null;
            }
        }

        private byte[] getImageDataFromUrl (URL url) {
            byte[] datas = {};

            try {
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                datas = IOUtils.toByteArray(input);

                input.close();
                connection.disconnect();
            } catch (IOException e) {
            }

            return datas;
        }
    }



}





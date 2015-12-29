package kaist.test.TapC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import junit.framework.Test;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import kaist.test.R;

@SuppressLint("ValidFragment")
public class TestActivity extends Fragment {
    Context mContext;

    private ListView m_ListView;
    private CustomListViewAdapter adapter;
    Document doc;
    ArrayList<CrawlingData> temp = new ArrayList<CrawlingData>();

    public TestActivity()
    {

    }


    public TestActivity(Context context)
    {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_json, null);

        m_ListView = (ListView) view.findViewById(R.id.listview);

        adapter = new CustomListViewAdapter(getContext(),R.layout.list_xml, temp);
        adapter.notifyDataSetChanged();

        m_ListView.setAdapter(adapter);
        m_ListView.setOnItemClickListener(mItemClickListener);

        TestAsyncTask jsoupAsyncTask = new TestAsyncTask();
        jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {
            //String tv = (String)parent.getAdapter().getItem(position);
            String tv = temp.get(position).getComicLink();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tv));
            startActivity(browserIntent);
        }
    };

    public class CrawlingData
    {
        String comicLink;
        Bitmap image;
        String comicName;

        public CrawlingData()
        {
            comicLink = "";
            image = null;
            comicName = "";
        }

        public String getComicLink()
        {
            return comicLink;
        }


        public void setComicLink(String input)
        {
            comicLink = input;
        }

        public Bitmap getImage()
        {
            return image;
        }

        public void setImage(Bitmap input)
        {
            image = input;
        }

        public String getComicName()
        {
            return comicName;
        }

        public void setComicName(String input)
        {
            comicName = input;
        }
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
                    CrawlingData input  = new CrawlingData();
                    Elements elems = item.getElementsByClass("sbjx");
                    Elements elems2 = item.getElementsByClass("crop");
                    for (Element elem : elems)
                    {
                        Elements href_elems = elem.getElementsByTag("a");
                        for(Element href_elem : href_elems)
                        {
                            String url = href_elem.attr("href");
                            input.setComicLink("http://marumaru.in" + url);
                        }
                        Elements span_elems = elem.getElementsByClass("cat");
                        for (Element span_elem : span_elems)
                        {
                            String name = elems.text();
                            input.setComicName(name);
                        }
                    }
                    for (Element elem2 : elems2)
                    {
                        Elements elems3 = item.getElementsByTag("img");
                        for (Element elem3 : elems3)
                        {
                            String img_url = elem3.attr("src");
                            input.setImage(downloadImage(img_url));
                        }
                    }
                    temp.add(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            m_ListView.requestLayout();
        }

        private Bitmap downloadImage(String url)
        {
            final int MAX_IMAGE_SIZE = 172;

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

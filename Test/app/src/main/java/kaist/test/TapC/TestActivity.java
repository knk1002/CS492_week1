package kaist.test.TapC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kaist.test.R;

@SuppressLint("ValidFragment")
public class TestActivity extends Fragment {
    Context mContext;

    private ListView m_ListView;
    private ArrayAdapter<String> Adapter;
    Document doc;
    Document doc2;
    ArrayList<CrawlingData> temp = new ArrayList<CrawlingData>();

    public TestActivity(Context context)
    {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_json, null);

        Adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

        m_ListView = (ListView) view.findViewById(R.id.listview);
        m_ListView.setAdapter(Adapter);
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
            String tv = temp.get(position).getImageLink();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tv));
            startActivity(browserIntent);
        }
    };

    private class CrawlingData
    {
        String comicLink;
        String imageLink;
        String comicName;

        public CrawlingData()
        {
            comicLink = "";
            imageLink = "";
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

        public String getImageLink()
        {
            return imageLink;
        }

        public void setImageLink(String input)
        {
            imageLink = input;
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
                            input.setImageLink(img_url);
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
            for(CrawlingData s : temp) {
                Adapter.add(s.getComicName());
            }
        }
    }



}

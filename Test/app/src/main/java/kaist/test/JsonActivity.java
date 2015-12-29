package kaist.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.String;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import android.widget.ArrayAdapter;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class JsonActivity extends Fragment {
    Context mContext;

    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;
    Document doc;
    ArrayList<String> temp;

    public JsonActivity()
    {

    }

    public JsonActivity(Context context)
    {
        super();
        this.mContext = context;
        this.m_Adapter = null;
        this.m_ListView = null;
        this.doc = null;
        this.temp = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.activity_json, null);
        m_Adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        m_ListView = (ListView) view.findViewById(R.id.listview);
        m_ListView.setAdapter(m_Adapter);
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect("http://marumaru.in/").userAgent("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52").get();
                Elements items = doc.select("td.ranic > a");
                for (Element item : items) {
                    temp.add(item.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for(String s : temp) {
                m_Adapter.add(s);
            }
            m_Adapter.notifyDataSetChanged();
            m_ListView.requestLayout();
        }
    }



}

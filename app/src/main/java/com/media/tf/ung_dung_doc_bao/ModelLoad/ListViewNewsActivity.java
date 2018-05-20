package com.media.tf.ung_dung_doc_bao.ModelLoad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.media.tf.ung_dung_doc_bao.Adapter.Customadapter;
import com.media.tf.ung_dung_doc_bao.Class_Imlepment.Docbao;
import com.media.tf.ung_dung_doc_bao.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListViewNewsActivity extends AppCompatActivity {

    ListView listView;
    Customadapter customadapter;
    ArrayList<Docbao> mangdocbao;

    //Button btncheckNetWork;
    //Cờ kiểm tra trạng thái internet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewnews);
        listView = (ListView) findViewById(R.id.listView);
        mangdocbao = new ArrayList<Docbao>();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        // button kiểm tra kết nối internet


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadRss().execute("http://www.24h.com.vn/upload/rss/congnghethongtin.rss");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, mangdocbao.get(i).link, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListViewNewsActivity.this, WebviewNewsActivity.class);
                intent.putExtra("link", mangdocbao.get(i).link);
                startActivity(intent);
                overridePendingTransition(R.anim.demo_face_out,R.anim.demo_face_in);
            }
        });


    }


    class ReadRss extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... Params) {
            return docNoiDung_Tu_URL(Params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument1(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String tiltle = "";
            String link = "";
            NodeList nodeListdescription = document.getElementsByTagName("description");

            String hinhanh = "";

            for (int i = 0; i < nodeList.getLength(); i++) {

                String cdata = nodeListdescription.item(i+1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if (matcher.find())
                {
                    hinhanh = matcher.group(1);
                    //Log.d("hinhanh",hinhanh + "........." + i);
                }
                Element element = (Element) nodeList.item(i);
                tiltle = parser.getValue(element, "title");
                link = parser.getValue(element, "link");
                //Toast.makeText(MainActivity.this,tiltle,Toast.LENGTH_LONG).show();
                //Log.d("link",link);
                mangdocbao.add(new Docbao(tiltle,link, hinhanh));

            }
           customadapter = new Customadapter(ListViewNewsActivity.this, android.R.layout.simple_list_item_1,mangdocbao);
            listView .setAdapter(customadapter);
            super.onPostExecute(s);
//            //Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }

    private String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }


}

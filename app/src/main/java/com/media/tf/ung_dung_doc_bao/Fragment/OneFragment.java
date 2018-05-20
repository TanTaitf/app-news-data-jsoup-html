package com.media.tf.ung_dung_doc_bao.Fragment;

import android.app.Fragment;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.baoyz.widget.PullRefreshLayout;
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.media.tf.ung_dung_doc_bao.Adapter.Customadapter;
import com.media.tf.ung_dung_doc_bao.Class_Imlepment.Docbao;
import com.media.tf.ung_dung_doc_bao.ModelLoad.XMLDOMParser;
import com.media.tf.ung_dung_doc_bao.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.link24h;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkBaoTinTuc;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkEva;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkItNew;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkThanhNien;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkTinTucOnline;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkTinTucVn;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkVietNamNet;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkVnpress;
import static com.media.tf.ung_dung_doc_bao.HelperLinkPublic.LinkPublic.linkXaLuan;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.fragmentManager;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.fragmentTransaction;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.materialMenu;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.myDrawerLayout;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.myToolbar;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.textView;

/**
 * Created by Windows 8.1 Ultimate on 21/07/2017.
 */
// chọn thư viện fragment support.v4.app do dùng chung với viewPager
public class OneFragment extends Fragment {

    ListView listView;
    Customadapter customadapter;
    ArrayList<Docbao> mangdocbao;
    Fragment fragment_1, fragment_2 ;
    ProgressBar prgGoogle;
    String xml = "";
    String linkURL;
    public static SearchView searchView;
    public static String jpg_Html = "";
    RequestQueue requestQueue ;
    MyAsyncTask asyncTask;
    ArrayList<String> arrayLink_Tintuc_Onl, arrayTitle_TinTuc_Onl, arrayHinh_TinTuc_Onl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one,container,false);
        listView = (ListView)view.findViewById(R.id.listView);
        prgGoogle = (ProgressBar)view.findViewById(R.id.google_progress);
        final PullRefreshLayout layout = (PullRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        setHasOptionsMenu(true);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) myToolbar.getLayoutParams();
//        layoutParams.height = 150 ;
//        myToolbar.setLayoutParams(layoutParams);
        fragment_2 = new TwoFragment();
        fragment_1 = new OneFragment();
        final Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_in);
        final Animation animation_title_toolbar = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
        final Animation ani_zoom = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_out);

        final Animation animAlpha = AnimationUtils.loadAnimation(getActivity(),R.anim.bounce);

        materialMenu.setIconState(MaterialMenuDrawable.IconState.BURGER);

        prgGoogle.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        prgGoogle.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(getActivity())
                .colors(getResources().getIntArray(R.array.google_colors)).build());
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawerLayout.openDrawer(Gravity.START);
            }
        });
        arrayLink_Tintuc_Onl = new ArrayList<>();
        arrayTitle_TinTuc_Onl = new ArrayList<>();
        arrayHinh_TinTuc_Onl = new ArrayList<>();
        // listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                // Change the refresh style, there are five styles of use, MATERIAL、CIRCLES、 WATER_DROP、RING and SMARTISAN
                layout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayLink_Tintuc_Onl.clear();
                        arrayTitle_TinTuc_Onl.clear();
                        arrayHinh_TinTuc_Onl.clear();
                        mangdocbao.clear();
                        new ReadRss().execute(linkURL);
                    }
                });
            }
        });

        // refresh complete
        layout.setRefreshing(false);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadRss().execute(linkURL);
            }
        });
        initKeyBoardListener();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.startAnimation(ani_zoom);
                myToolbar.startAnimation(animation_title_toolbar);
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.layoutMainContent,fragment_2);
                Bundle bundle = new Bundle();
                bundle.putString("link",mangdocbao.get(i).link);
                fragment_2.setArguments(bundle);
                fragmentTransaction.commit();

            }
        });


        return view;
    }
    private void initKeyBoardListener() {
        // Минимальное значение клавиатуры. Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        // Окно верхнего уровня view. Top-level window decor view.
        final View decorView = getActivity().getWindow().getDecorView();
        // Регистрируем глобальный слушатель. Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // Видимый прямоугольник внутри окна. Retrieve visible rectangle inside window.
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        Log.d("Pasha", "SHOW");
                        //Toast.makeText(getActivity(),"keyboard show", Toast.LENGTH_SHORT).show();

                        //return true;
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        //Log.d("Pasha", "HIDE");
                        //Toast.makeText(getActivity(), "keyboard visible", Toast.LENGTH_SHORT).show();
                        if (! searchView.isIconified ()) {
                            searchView.setIconified (true);
                        }
                    }
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight;

            }
        });
    }

    private void applyFontToMenuItem(EditText mi) {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "myriadpro.ttf");
        mi.setTypeface(font);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);


        MenuItem myActionMenuItem = menu.findItem( R.id.menu_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        EditText editText = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        editText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        editText.setHint("Search ...");
        editText.setTextColor(getResources().getColor(R.color.colorWhite));
        editText.setTextSize(19);
        applyFontToMenuItem(editText);

        int searchImgId = android.support.v7.appcompat.R.id.search_button; // I used the explicit layout ID of searchview's ImageView
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.icon_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                filter(text);
                return false;
            }
        });
        initKeyBoardListener();
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        int i ;
        if (charText.length() == 0) {

        } else {
            for (i = 0;i < mangdocbao.size(); i++ )
                if (mangdocbao.get(i).getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    final int finalI = i;
                    listView.post(new Runnable() {
                        public void run() {
                            listView.smoothScrollToPosition(finalI);
                        }});


                }
        }
    }



    public void replaceFragmentWithAnimation(Fragment fragment){
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
        transaction.replace(R.id.layoutMainContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Getting RSS feed link from HTML source code
     *
     * @param //ulr is url of the website
     * @returns url of rss link of website
     * */
    public String getRSSLinkFromURL(String url) {
        // RSS url
        String rss_url = null;
        String jpg = "";

        try {
            // Using JSoup library to parse the html source code
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            Toast.makeText(getActivity(),  doc.toString(),Toast.LENGTH_SHORT).show();

            // finding rss links which are having link[type=application/rss+xml]
            org.jsoup.select.Elements links = doc.select("link[type=application/rss+xml]");


            org.jsoup.nodes.Document document = Jsoup.connect(url).get();
            String title = doc.title();

            Elements pngs = doc.select("img[src$=.jpg]");
            jpg = pngs.get(0).attr("href").toString();
            Log.d("LinkAnh", "linkanh " +jpg);
            Log.d("No of RSS links found", " " + links.size());

            // check if urls found or not
            if (links.size() > 0) {
                rss_url = links.get(0).attr("href").toString();
            } else {
                // finding rss links which are having link[type=application/rss+xml]
                org.jsoup.select.Elements links1 = doc
                        .select("link[type=application/atom+xml]");
                if(links1.size() > 0){
                    rss_url = links1.get(0).attr("href").toString();
                }
            }

        } catch (IOException e) {
            Log.d("TTT", "getRSSLinkFromURL: "+e.toString());
            e.printStackTrace();
        }

        // returing RSS url
        return rss_url;
    }

    /**
     * Method to get xml content from url HTTP Get request
     * */
    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            Log.d("EEE",e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // nhận dữ liệu từ activity
        Bundle bundle_nhan = getArguments();
        if (bundle_nhan == null)
        {
            Toast.makeText(getActivity(), "Vui lòng tải lại...!!!", Toast.LENGTH_LONG).show();
            return;
        }
        linkURL = bundle_nhan.getString("linkNews");
        mangdocbao = new ArrayList<Docbao>();

    }


    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = downloadUrl(urlString);
        return  convertStreamToString(stream);

    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("DC",sb.toString());
        //Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
        return sb.toString();
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        Log.d("DA",conn.toString());
        return conn.getInputStream();
    }

    /**
     * fuction return link jpg from url (html)
     * @param //url
     * @return
     */


    public class MyAsyncTask extends AsyncTask<String, Integer, String>{
        public AsyncResponse delegate = null;
        @Override
        protected String doInBackground(String... Params) {
                return  docNoiDung_Tu_URL(Params[0]);

        }
        @Override
        protected void onPostExecute(String s) {
            delegate.processFinish(s);
            super.onPostExecute(s);

        }
    }


    class ReadHmtl extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... Params) {
            return docNoiDung_Tu_URL(Params[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String getStringVolley(String url) {

        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);

                       // Log.d("AAA",abc.toString());

                    }


//                        org.jsoup.nodes.Document document = null;
//
//                        document = (org.jsoup.nodes.Document) Jsoup.parse(response);
//                        if (document != null) {
//                            Elements subjectElements = document.select("div.ctDetatilS");
////                            src[0] = subjectElements.attr("src");
////                            Log.e("VVV", "doInBackground: " + src[0]);
////                            return;
//                            for (org.jsoup.nodes.Element element : subjectElements) {
//                                org.jsoup.nodes.Element imgSubject = element.getElementsByTag("img").first();
//                                if (imgSubject != null) {
//                                    String src1 = imgSubject.attr("src");
//
//                                    //Log.d("PPP", "doInBackground: " + jpg[0]);
//                                }
//                            }
//
//                        }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("AAA",error.toString());
                    Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_SHORT).show();
                }
        });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        return textView.getText().toString();
    }
    public static String getString(String s){
        return s;
    }
    public ArrayList<String> getJPGfromURL(ArrayList<String> url) throws ExecutionException, InterruptedException {
        ArrayList<String> jpg = new ArrayList<>();
        int dem = 0;
        for (int i = 0; i < url.size(); i++){
            String rss_url = new ReadHmtl().execute(url.get(i)).get();
            org.jsoup.nodes.Document document = (org.jsoup.nodes.Document) Jsoup.parse(rss_url);
            if (document != null) {
                Elements subjectElements = document.select("div.ctDetatilS");
                for (org.jsoup.nodes.Element element : subjectElements) {
                    org.jsoup.nodes.Element imgSubject = element.getElementsByTag("img").first();
                    if (imgSubject != null) {
                        String src1 = imgSubject.attr("src");
                        dem = 1;
                        if (dem == 1){
                            jpg.add(src1);
                            dem = 0;
                            break;
                        }
                    }
                }

            }
        }
        return jpg;
    }

    class ReadRss extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... Params) {

            return docNoiDung_Tu_URL(Params[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null){
                prgGoogle.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                if (linkURL == link24h || linkURL == linkVnpress || linkURL
                        == linkEva || linkURL == linkVietNamNet ){
                    DOMParser_Genare(s);
                }
                if (linkURL == linkItNew){
                    DOMParser_ICTNews(s);
                }
                if (linkURL == linkTinTucOnline){
                    DOMParser_TinTucOnline(s);
                }
                if (linkURL == linkBaoTinTuc || linkURL == linkThanhNien  ){
                    DOMParser_Thanhnien(s);
                }
                if (linkURL == linkXaLuan){
                    DOMParser_XaLuan(s);
                }
                if (linkURL == linkTinTucVn){
                    DOMParser_TinTucVN(s);
                }
            }
            super.onPostExecute(s);

        }
    }
    private String StringTiltle(String title){
        while (title.contains("34")||title.contains("@")||title.contains("#")
                ||title.contains("$")|| title.contains("%")||title.contains("^")
                ||title.contains("&") ||title.contains("*")||title.contains(";")){
            title = title.replace("@","").replace("#","").replace("$","")
                    .replace("%","").replace("^","").replace("&","")
                    .replace("*","").replace(";","").replace("34"," ");
        }
        return title;
    }
    private static String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * Hàm đọc Các trang còn lại
     */
    public void DOMParser_Genare(String s){
        XMLDOMParser parser = new XMLDOMParser();
        Document document = parser.getDocument1(s);
        NodeList nodeList = document.getElementsByTagName("item");
        String tiltle = "";
        String link = "";
        NodeList nodeListdescription = document.getElementsByTagName("description");

        String hinhanh = "";

        for (int i = 0; i < nodeList.getLength(); i++) {

            String cdata = nodeListdescription.item(i + 1).getTextContent();
            Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
            Matcher matcher = p.matcher(cdata);
            if (matcher.find()) {
                hinhanh = matcher.group(1);
                Log.d("hinhanh", hinhanh + "........." + i);
            }
            Element element = (Element) nodeList.item(i);
            tiltle = parser.getValue(element, "title");
            link = parser.getValue(element, "link");
            tiltle = StringTiltle(tiltle);
            //Toast.makeText(getActivity(), link, Toast.LENGTH_LONG).show();
            Log.d("link", link);
            mangdocbao.add(new Docbao(tiltle, link, hinhanh));

        }
        customadapter = new Customadapter(getActivity(), android.R.layout.simple_list_item_1, mangdocbao);
        listView.setAdapter(customadapter);
    }

    /**
     * Hàm đọc trang tintuconline
     */
    public void DOMParser_TinTucOnline(String s){
        XMLDOMParser parser = new XMLDOMParser();
        InputStream stream = new ByteArrayInputStream(s.getBytes());
        Document document = parser.getDocument(stream);
        if (document != null){
            NodeList nodeList = document.getElementsByTagName("item");
            String tiltle = "";
            String link = "";
            NodeList nodeListdescription = document.getElementsByTagName("description");

            String hinhanh = "http://tintuconline.com.vn/images/logottol2x.png";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                tiltle = parser.getValue(element, "title");
                link = parser.getValue(element, "link");
                tiltle = StringTiltle(tiltle);
                arrayTitle_TinTuc_Onl.add(tiltle);
                arrayLink_Tintuc_Onl.add(link);
            }

//            ArrayList<String> hinh = new ArrayList<>();
//            try {
//                hinh = getJPGfromURL(arrayLink_Tintuc_Onl);
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            for (int i = 0 ; i < arrayTitle_TinTuc_Onl.size();i++){
                Log.d("title " + i, arrayTitle_TinTuc_Onl.get(i).toString());
            }
            for (int i = 0 ; i < arrayLink_Tintuc_Onl.size();i++){
                Log.d("link " + i, arrayLink_Tintuc_Onl.get(i).toString());
            }

            arrayHinh_TinTuc_Onl.add("https://lh3.googleusercontent.com/-IHZA7PJ1kfynOQsUnc_kuWItoL0rMNR0P01pWc1rzpjIdLiydcZ2s6gqsV_Bu7uwf907iQyRBdQtCI=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh6.googleusercontent.com/4z0SWJbkfKRqo0KG6U94BBsB8v_EtGXMyySYkZLN7aA7zBnLwSadPrrco2Te5rv7wN7DuCeyBOQ4JVE=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh3.googleusercontent.com/SH6mICJ4mGB1O9FemK-gZR8RqdI7XteAlwsvctSM1gZFCU3qgYqg8L1HeWnozCRsz8qt5-C3w3YbxFQ=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh5.googleusercontent.com/MxHqxcSzDU4mr-3o0nJmYiYGxLkctRnMmjcWTsoEoR07WWVwkVNUJ7SHREtmHRjyfJskrwBiNkO5vkI=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh6.googleusercontent.com/24r5HDSLrs0Zdz-RKKDMjxIHwopLKS1dcNpbYRQVXZfJHhd7EGTWN07mfN-t3UMMPnL3rPC2czOq1ew=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh3.googleusercontent.com/UcARfqfIMv8aecpuFKmAyfDlveiz9g6jfunURsAVflxbA6cYrAdDTVKfNg2lHpOMvXZBWQaIencdREA=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh6.googleusercontent.com/jr3gfaaytgXcwsFeX9T1tdP384o_oIilzNG1Ap4Bsfvq-poProJ8tTLcUDKGSyeSRoKYYDTjedwqQdE=w1366-h655");
            arrayHinh_TinTuc_Onl.add("https://lh4.googleusercontent.com/RqXVIB_G3u_n0e6z2tbrO7d2oHpCcuzj1h69PQi41dnp3r8nO415dZBu5ZNlJuj5kyB_i2R_Ur_f_RM=w1366-h655");


            for (int i = 0; i < arrayTitle_TinTuc_Onl.size(); i++) {
                mangdocbao.add(new Docbao(arrayTitle_TinTuc_Onl.get(i)
                            ,arrayLink_Tintuc_Onl.get(i),Ramdom(arrayHinh_TinTuc_Onl)));

            }
            customadapter = new Customadapter(getActivity(), android.R.layout.simple_list_item_1,mangdocbao);
            listView .setAdapter(customadapter);
        }else {
            Toast.makeText(getActivity(),"Lỗi Mạng",Toast.LENGTH_LONG).show();
        }
    }
    public String Ramdom(ArrayList<String> s)
    {

        Random ra = new Random();
        int number = ra.nextInt(8);
        Log.d("random",""+ number);
        //rand.nextInt((max - min) + 1) + min;
        //int soa = ra.nextInt((50-40)+1) +40;

        return s.get(number);
    }

    /**
     * Hàm đọc trang Tin tức vn
     */
    private void DOMParser_TinTucVN(String  s) {

        InputStream stream = new ByteArrayInputStream(s.getBytes());
        InputSource inputSource = new InputSource(stream);
        inputSource.setEncoding("UTF-8");
        String tiltle = "";
        String link = "";
        String hinhanh = "";
        DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dBuilderFactory.newDocumentBuilder();
            Document dom = documentBuilder.parse(inputSource);

            // get the root element.....
            Element docElement = dom.getDocumentElement();
            Log.i("Root Element", docElement.getTagName());

            // now get the NodeList of root elements
            NodeList nodeList = docElement.getElementsByTagName("item");
            NodeList nodeListdescription = docElement.getElementsByTagName("description");
            Log.i("NodeList Length", nodeList.getLength()+"");
            for (int i = 0; i < nodeList.getLength(); i++) {

                Element eleBook = (Element) nodeList.item(i);
                Log.i("Node", eleBook.getTagName());

                NodeList titleNode = eleBook.getElementsByTagName("title");
                Element TitleEle = (Element) titleNode.item(0);
                Log.i("Title", "Title - "+TitleEle.getFirstChild().getNodeValue());
                tiltle = TitleEle.getFirstChild().getNodeValue();
                tiltle = tiltle.trim();

                NodeList AuthorFName1Node = eleBook.getElementsByTagName("link");
                Element AuthorFName1Ele = (Element) AuthorFName1Node.item(0);
                Log.i("link","link - "+AuthorFName1Ele.getFirstChild().getNodeValue());
                link = AuthorFName1Ele.getFirstChild().getNodeValue();
                tiltle = StringTiltle(tiltle);
                String cdata = nodeListdescription.item(i+1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if (matcher.find())
                {
                    hinhanh = matcher.group(1);
                    Log.d("hinhanh",hinhanh + "........." + i);
                }

                mangdocbao.add(new Docbao(tiltle,link, hinhanh));
            }
            customadapter = new Customadapter(getActivity(), android.R.layout.simple_list_item_1,mangdocbao);
            listView .setAdapter(customadapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Hàm đọc trang Thanh niên và Baotintuc
     */
    private void DOMParser_Thanhnien(String  s) {

        InputStream stream = new ByteArrayInputStream(s.getBytes());
        InputSource inputSource = new InputSource(stream);
        inputSource.setEncoding("UTF-8");
        String tiltle = "";
        String link = "";
        //String hinhanh = "";
        String hinhanh = "http://www.xaluan.com/images/news/Image/2017/09/22/small_359c4e619d88c2.img.jpg";
        DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dBuilderFactory.newDocumentBuilder();
            Document dom = documentBuilder.parse(inputSource);

            // get the root element.....
            Element docElement = dom.getDocumentElement();
            Log.i("Root Element", docElement.getTagName());

            // now get the NodeList of root elements
            NodeList nodeList = docElement.getElementsByTagName("item");
            NodeList nodeListdescription = docElement.getElementsByTagName("description");
            Log.i("NodeList Length", nodeList.getLength()+"");
            for (int i = 0; i < nodeList.getLength(); i++) {

                Element eleBook = (Element) nodeList.item(i);
                Log.i("Node", eleBook.getTagName());

                NodeList titleNode = eleBook.getElementsByTagName("title");
                Element TitleEle = (Element) titleNode.item(0);
                Log.i("Title", "Title - "+i + ": "+TitleEle.getFirstChild().getNodeValue().trim());
                tiltle = TitleEle.getFirstChild().getNodeValue();
                tiltle = tiltle.trim();
                //Toast.makeText(getActivity(),tiltle,Toast.LENGTH_SHORT).show();
                NodeList AuthorFName1Node = eleBook.getElementsByTagName("link");
                Element AuthorFName1Ele = (Element) AuthorFName1Node.item(0);
                Log.d("link","link - " + i +": "+AuthorFName1Ele.getFirstChild().getNodeValue().trim());
                link = AuthorFName1Ele.getFirstChild().getNodeValue();
                tiltle = StringTiltle(tiltle);

                String cdata = nodeListdescription.item(i+1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if (matcher.find())
                {
                    hinhanh = matcher.group(1);
                    Log.d("hinhanh","Hinh so " + i + ": " + hinhanh);
                }

                mangdocbao.add(new Docbao(tiltle,link, hinhanh));
            }
            customadapter = new Customadapter(getActivity(), android.R.layout.simple_list_item_1,mangdocbao);
            listView .setAdapter(customadapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * Hàm đọc trang Xa Luan
    *
     */
    private void DOMParser_XaLuan(String  s) {

        InputStream stream = new ByteArrayInputStream(s.getBytes());
        InputSource inputSource = new InputSource(stream);
        inputSource.setEncoding("UTF-8");
        String tiltle = "";
        String link = "";
        String hinhanh = "";
        DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dBuilderFactory.newDocumentBuilder();
            Document dom = documentBuilder.parse(inputSource);

            // get the root element.....
            Element docElement = dom.getDocumentElement();
            Log.i("Root Element", docElement.getTagName());

            // now get the NodeList of root elements
            NodeList nodeList = docElement.getElementsByTagName("item");
            NodeList nodeListdescription = docElement.getElementsByTagName("description");
            Log.i("NodeList Length", nodeList.getLength()+"");
            for (int i = 0; i < nodeList.getLength(); i++) {

                Element eleBook = (Element) nodeList.item(i);
                Log.i("Node", eleBook.getTagName());

                NodeList titleNode = eleBook.getElementsByTagName("title");
                Element TitleEle = (Element) titleNode.item(0);
                Log.i("Title", "Title - "+i + ": "+TitleEle.getFirstChild().getNodeValue().trim());
                tiltle = TitleEle.getFirstChild().getNodeValue();
                tiltle = tiltle.trim();
                //Toast.makeText(getActivity(),tiltle,Toast.LENGTH_SHORT).show();
                NodeList AuthorFName1Node = eleBook.getElementsByTagName("link");
                Element AuthorFName1Ele = (Element) AuthorFName1Node.item(0);
                Log.d("link","link - " + i +": "+AuthorFName1Ele.getFirstChild().getNodeValue().trim());
                link = AuthorFName1Ele.getFirstChild().getNodeValue();
                tiltle = StringTiltle(tiltle);

                String cdata = nodeListdescription.item(i+2).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if (matcher.find())
                {
                    hinhanh = matcher.group(1);
                    Log.d("hinhanh","Hinh so " + i + ": " + hinhanh);
                }
                hinhanh = hinhanh.replace("small_","");

                mangdocbao.add(new Docbao(tiltle,link, hinhanh));
            }
            customadapter = new Customadapter(getActivity(), android.R.layout.simple_list_item_1,mangdocbao);
            listView .setAdapter(customadapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Hàm dành cho đọc trang ictnew
     */
    public void DOMParser_ICTNews(String s){
        /**
         * 3 dòng dưới của trang ictnew
         */
        XMLDOMParser parser = new XMLDOMParser();
        InputStream stream = new ByteArrayInputStream(s.getBytes());
        Document document = parser.getDocument(stream);
        /**
         * Khúc này của trang ictnew
         */
        if (document != null){
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
                    Log.d("hinhanh",hinhanh + "........." + i);
                }
                Element element = (Element) nodeList.item(i);
                tiltle = parser.getValue(element, "title");
                link = parser.getValue(element, "link");
                tiltle = StringTiltle(tiltle);
                //Toast.makeText(getActivity(),tiltle, Toast.LENGTH_LONG).show();
                Log.d("link",link);
                mangdocbao.add(new Docbao(tiltle,link, hinhanh));

            }
            customadapter = new Customadapter(getActivity(), android.R.layout.simple_list_item_1,mangdocbao);
            listView .setAdapter(customadapter);
        }else {
            Toast.makeText(getActivity(),"Lỗi Mạng",Toast.LENGTH_LONG).show();

        }

    }

}

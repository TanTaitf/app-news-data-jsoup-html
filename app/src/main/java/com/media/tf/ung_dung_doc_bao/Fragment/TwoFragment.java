package com.media.tf.ung_dung_doc_bao.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.media.tf.ung_dung_doc_bao.R;

import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.materialMenu;
import static com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity.myToolbar;

/**
 * Created by Windows 8.1 Ultimate on 21/07/2017.
 */
// chọn thư viện fragment support.v4.app do dùng chung với viewPager
public class TwoFragment extends Fragment{
    public static WebView webView;
    String linkURL = "";
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two,container,false);
        webView = (WebView)view.findViewById(R.id.webview);
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        setHasOptionsMenu(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()== true){
                    webView.goBack();
                }else {
                    if (getFragmentManager().getBackStackEntryCount()> 0){
                        getFragmentManager().popBackStack();
                    }else {
                        webView.clearCache(true);
                        onDestroy();
                    }
                }
            }
        });

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Vui lòng chờ...");
        progress.setIndeterminate(false);
        progress.setCancelable(true);


        // nhận dữ liệu từ activity
        Bundle bundle_nhan = getArguments();
        if (bundle_nhan == null)
        {
            Toast.makeText(getActivity(), "Vui lòng tải lại...!!!", Toast.LENGTH_LONG).show();
            //return null;
        }
        else {
            linkURL = bundle_nhan.getString("link");
            webView.loadUrl(linkURL);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progress.show();
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progress.dismiss();
                    //webView.setVisibility(View.VISIBLE);
                    super.onPageFinished(view, url);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    progress.dismiss();
                    super.onReceivedError(view, request, error);
                }
            });

        }

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void hideViews() {
        myToolbar.animate().translationY(-myToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        myToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            webView.clearHistory();
            getFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

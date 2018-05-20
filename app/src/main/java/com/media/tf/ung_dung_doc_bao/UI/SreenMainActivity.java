package com.media.tf.ung_dung_doc_bao.UI;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.media.tf.ung_dung_doc_bao.FontCustom.CustomTypefaceSpan;
import com.media.tf.ung_dung_doc_bao.Fragment.OneFragment;
import com.media.tf.ung_dung_doc_bao.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import static com.media.tf.ung_dung_doc_bao.Fragment.TwoFragment.webView;
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

public class SreenMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout myTabLayout;
    ViewPager myViewPager;
    public static Toolbar myToolbar;
    public static DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    public static TextView textView;
    public static MaterialMenuDrawable materialMenu;
    private boolean isDrawerOpened;
    public static FragmentManager fragmentManager ;
    public static FragmentTransaction fragmentTransaction;
    TextView txttitle, txtcontent;
    Button btnok;
    ShimmerTextView shi_TxtInfo;
    private AdView mAdView;
    ShimmerTextView shimmer;
    boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Anhxa();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                check = checkInternet();
            }
        });
        if (check == true){
            setSupportActionBar(myToolbar);
            // nút back cho toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("24h");
            materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
            myToolbar.setNavigationIcon(materialMenu);

            myToolbar.setTitleTextColor(android.graphics.Color.WHITE);
            myDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    materialMenu.setTransformationOffset(
                            MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                            isDrawerOpened ? 2 - slideOffset : slideOffset
                    );
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    isDrawerOpened = true;
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    isDrawerOpened = false;
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    if(newState == DrawerLayout.STATE_IDLE) {
                        if(isDrawerOpened) {
                            materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
                        } else {
                            materialMenu.setIconState(MaterialMenuDrawable.IconState.BURGER);
                        }
                    }
                }
            });

            // id ứng dụng
            MobileAds.initialize(this, "ca-app-pub-6352050986417104~6333490903");
            //MobileAds.initialize(this, idapp);


            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    //Toast.makeText(getApplicationContext(), "Đã mở Ad", Toast.LENGTH_SHORT).show();
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            });


            Menu m = myNavigationView.getMenu();
            for (int i=0;i<m.size();i++) {
                MenuItem mi = m.getItem(i);

                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu!=null && subMenu.size() >0 ) {
                    for (int j=0; j <subMenu.size();j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }

                //the method we have create in activity
                applyFontToMenuItem(mi);
            }


            // set default color icon navigation view
            myNavigationView.setItemIconTintList(null);
            myNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#455a64")));
            myNavigationView.setBackgroundColor(Color.parseColor("#ffffff"));




            // ánh xạ view header
//        View headerView = myNavigationView.getHeaderView(0);
//        textView = (TextView)headerView.findViewById(R.id.txttitle_header);
//        shimmer = (ShimmerTextView)headerView.findViewById(R.id.shimmer_tv);
//
//        Typeface font = Typeface.createFromAsset(getAssets(), "myriadpro.ttf");
//        shimmer.setTypeface(font);
//        Shimmer shi = new Shimmer();
//        shi.start(shimmer);

            // stop shhimer
            // shimmer.cancel();

            AddFragment(link24h);

            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDrawerLayout.openDrawer(Gravity.START);
                }
            });
            final Animation ani_item = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
            //myNavigationView.setAnimation(ani_item);

            myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    String link = link24h;
                    switch (item.getItemId())
                    {
                        case R.id.tin24h:
                            myToolbar.setTitle("24h");

                            link = link24h;
                            break;
                        case R.id.tinVnpress:
                            // OK
                            myToolbar.setTitle("VnExpress");
                            link = linkVnpress;
                            break;
                        case R.id.tinThanhNien:
                            myToolbar.setTitle("Thanh Niên");
                            // Confix
                            link = linkThanhNien;
                            break;
                        case R.id.tinBaoTinTuc:
                            myToolbar.setTitle("Tin Tức");
                            link = linkBaoTinTuc;
                            break;
                        case R.id.tinXaLuan:
                            myToolbar.setTitle("Xã Luận");
                            link = linkXaLuan;
                            break;
                        case R.id.tinVietnamnet:
                            myToolbar.setTitle("VietNamNet");
                            link = linkVietNamNet;
                            break;
                        case R.id.tinIctnews:
                            myToolbar.setTitle("ICT News");
                            link = linkItNew;
                            break;
                        case R.id.tintucvn:
                            myToolbar.setTitle("Tin Tức VN");
                            link = linkTinTucVn;
                            break;
//                    case R.id.tinNguoiLD:
//                        link = linkNguoiLD;
//                        break;
                        case R.id.tinTucOnline:
                            myToolbar.setTitle("Tin Tức Online");
                            link = linkTinTucOnline;
                            break;
//                    case R.id.tinSoha:
//                        link = linkSoha;
//                        break;
                        case R.id.tinEva:
                            myToolbar.setTitle("Eva");
                            link = linkEva;
                            break;
                        case R.id.email_menu:
                            try {
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setType("plain/text");
                                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "Tinmoi_2018vn@gmail.com" });
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Chào Bạn !!!");
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Rất vui khi nhận được sự góp ý của bạn :-)");
                                startActivity(sendIntent);
                            }catch (Exception e){
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("text/plain");
                                emailIntent.putExtra("sms_body","Xin Chao Bạn.....*_*");
                                startActivity(emailIntent);
                            }
                            myToolbar.setTitle("24h");
                            break;
                        case R.id.rate_menu:
                            myToolbar.setTitle("24h");
                            break;
                        case R.id.share_menu:
                            try{
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("text/plain");
                                emailIntent.putExtra("sms_body","Xin Chào.....");
                                startActivity(emailIntent);
                            }catch (Exception e){

                            }
                            myToolbar.setTitle("24h");
                            break;
                        case R.id.info_meu:
                            ShowDialog(R.style.DialogAnimation);
                            myToolbar.setTitle("24h");
                            break;
                    }
                    AddFragment(link);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                    myToolbar.startAnimation(animation);
                    myDrawerLayout.closeDrawer(Gravity.START);
                    return false;
                }
            });
        }
        else {
            showDialogCheckInternet();
        }




    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        final boolean[] kt = {checkInternet()};
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                kt[0] = checkInternet();
            }
        });
        if (kt[0] == false){
            showDialogCheckInternet();
        }else {
            startActivity(getIntent());
        }
        super.onRestart();
    }

    public boolean checkInternet(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    private  void showDialogCheckInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Vui lòng kiểm tra Internet !");
        builder.setIcon(R.drawable.ic_wifi);

        builder.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                //finish();
                return;
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                return;
            }
        });
        builder.show();
    }
    public void AddFragment(String link){
        fragmentManager = getFragmentManager();
        Fragment fragment = new OneFragment();
        fragmentTransaction = fragmentManager.beginTransaction();

        //fragmentTransaction.addToBackStack("1");
        fragmentTransaction.replace(R.id.layoutMainContent,fragment);
        Bundle bundle = new Bundle();
        bundle.putString("linkNews",link);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }
    private void Anhxa() {
        myToolbar = (Toolbar)findViewById(R.id.mytoolbar);
        myDrawerLayout = (DrawerLayout)findViewById(R.id.mydrawerLayout);
        myNavigationView = (NavigationView)findViewById(R.id.myNavigationView);


//        myTabLayout = (TabLayout)findViewById(R.id.myTablayout);
//        myViewPager = (ViewPager)findViewById(R.id.myViewPager);

    }
    public void ShowDialog(int animationSource)
    {
        final Dialog dialog = new Dialog(SreenMainActivity.this);
        // bỏ title của dialog đặt trước khi gắn vào layout
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false); // set hiển thị cứng cho dialog
        AnhxaDialog(dialog);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sequential);
        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // lệnh tắt dialog
                //dialog.cancel();
            }
        });

        dialog.show();

    }
    public void AnhxaDialog(Dialog dialog)
    {
        // anh xa dialog
        shi_TxtInfo = (ShimmerTextView)dialog.findViewById(R.id.textTitleinfo);
        Typeface font = Typeface.createFromAsset(getAssets(), "myriadpro.ttf");
        shi_TxtInfo.setTypeface(font);
        Shimmer shi = new Shimmer();
        shi.start(shi_TxtInfo);
        txttitle = (TextView) dialog.findViewById(R.id.txttitleinfo);
        txtcontent  = (TextView) dialog.findViewById(R.id.txtinfo);
        btnok = (Button)dialog.findViewById(R.id.ok);

    }
    private boolean hideSoftKeyboard() {
        View view = SreenMainActivity.this.getCurrentFocus ();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) SreenMainActivity.this.getSystemService (Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow (view.getWindowToken (), 0);
            return true;
        }
        return false;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "myriadpro.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); //Use this if you want to center the items
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        if (myDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            myDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            if (webView != null && webView.canGoBack()== true){
                webView.goBack();
            }else {
                if (getFragmentManager().getBackStackEntryCount()> 0){
                    getFragmentManager().popBackStack();
                }else {
                    if (webView != null){
                        webView.clearCache(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                    }
                    else{
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                    }

                }
            }
        }




    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}


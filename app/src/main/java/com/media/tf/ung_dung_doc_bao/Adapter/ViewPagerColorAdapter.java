package com.media.tf.ung_dung_doc_bao.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Windows 8.1 Ultimate on 21/07/2017.
 */

public class ViewPagerColorAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> ArrayListTitle = new ArrayList<>();
    public ViewPagerColorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // phương thức khởi tạo để add fragment gồm fragment và string title
    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        ArrayListTitle.add(title);
    }
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return ArrayListTitle.get(position);
//    }

}

package com.media.tf.ung_dung_doc_bao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.media.tf.ung_dung_doc_bao.Class_Imlepment.Docbao;
import com.media.tf.ung_dung_doc_bao.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Windows 8.1 Ultimate on 25/10/2016.
 */

public class Customadapter extends ArrayAdapter<Docbao> {

    public Customadapter(Context context, int resource, List<Docbao> items) {
        super(context, resource, items);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.dong_layout_listview, null);
        }
        Docbao p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txttitle = (TextView) view.findViewById(R.id.textViewtitle);
            txttitle.setText(p.title);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(p.image)
                    .error(R.drawable.img_load).into(imageView);


        }

        // gan hieu ung
        Animation animAlpha = AnimationUtils.loadAnimation(getContext(),R.anim.enter);
        view.startAnimation(animAlpha);
        return view;
    }

}
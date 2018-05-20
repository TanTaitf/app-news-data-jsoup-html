package com.media.tf.ung_dung_doc_bao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.media.tf.ung_dung_doc_bao.UI.SreenMainActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;

public class WellcomHome extends AppCompatActivity {

    ShimmerTextView myshimer;
    ShimmerButton btnButton;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_application);
        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE); // khai báo tên khóa, mode appen

        Boolean checknumber = sharedPreferences.getBoolean("number",false);
        if(checknumber == true) {
            startActivity(new Intent(getApplicationContext(), SreenMainActivity.class));
            finish();
            return;
        }


        myshimer = (ShimmerTextView)findViewById(R.id.textTitle);
        btnButton = (ShimmerButton)findViewById(R.id.btn_Docngay);
        Typeface font = Typeface.createFromAsset(getAssets(), "myriadpro.ttf");
        myshimer.setTypeface(font);
        btnButton.setTypeface(font);
        Shimmer shi = new Shimmer();
        shi.start(myshimer);
        shi.start(btnButton);

        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("number",true);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), SreenMainActivity.class));
                finish();
            }
        });
    }
}

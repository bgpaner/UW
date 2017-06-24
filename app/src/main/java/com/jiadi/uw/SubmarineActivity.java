package com.jiadi.uw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by 北 on 2017/6/24.
 */

public class SubmarineActivity extends AppCompatActivity {
    private ImageButton buttonCus1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submarine);
        mContext=this;
        buttonCus1= (ImageButton)findViewById(R.id.ButtonCus1);
        buttonCus1.setOnClickListener(buttonCus1ClickListener);
    }
    private View.OnClickListener buttonCus1ClickListener = new View.OnClickListener() {
        public void onClick(View arg0) {
            Intent setIntent = new Intent();
            setIntent.setClass(mContext, Wifisetting.class);
            startActivity(setIntent);
        }
    };
}

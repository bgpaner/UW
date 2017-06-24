package com.jiadi.uw;
/**
 * Created by 北 on 2017/6/24.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class VesselActivity extends AppCompatActivity {
    private ImageButton buttonCus1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vessel);
        mContext=this;
        buttonCus1= (ImageButton)findViewById(R.id.ButtonCus1);
        buttonCus1.setOnClickListener(buttonCus1ClickListener);
    }

    private OnClickListener buttonCus1ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            Intent setIntent = new Intent();
            setIntent.setClass(mContext, Wifisetting.class);
            startActivity(setIntent);
        }
    };
}

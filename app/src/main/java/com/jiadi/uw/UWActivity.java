package com.jiadi.uw;
/**
 * Created by 北 on 2017/6/24.
 */
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

public class UWActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隐去标题（应用的名字必须要写在setContentView之前，否则会有异常）
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_uuv);
        //获得TabHost
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("Submarine").setIndicator(
                "潜水机器人").setContent(
                new Intent(this,SubmarineActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Vessel").setIndicator(
                "无人船").setContent(
                new Intent(this,VesselActivity.class)));
        mTabHost.setCurrentTab(0);
    }
}

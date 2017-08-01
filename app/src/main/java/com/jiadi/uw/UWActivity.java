package com.jiadi.uw;
/**
 * Created by 北 on 2017/6/24.
 */
import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class UWActivity extends AppCompatActivity {
    private BottomBar bottombar;
    private Fragment Submarine;
    private Fragment Vessel;
    private Fragment Wifisetting;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隐去标题（应用的名字必须要写在setContentView之前，否则会有异常）
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_uuv);

       init();
    }
    private void init(){
        Submarine= new SubmarineActivity();
        Vessel=new VesselActivity();
        Wifisetting=new Wifisetting();
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentContainer, Submarine);
        transaction.add(R.id.contentContainer, Vessel);
        transaction.add(R.id.contentContainer, Wifisetting);
        transaction.commit();

        bottombar = tools.find(this, R.id.bottomBar);
        bottombar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                transaction = fragmentManager.beginTransaction();
                switch (tabId) {
                    case R.id.tab_submarine:
                        transaction.show(Submarine);
                        transaction.hide(Vessel);
                        transaction.hide(Wifisetting);
                        break;
                    case R.id.tab_vessel:
                        transaction.show(Vessel);
                        transaction.hide(Submarine);
                        transaction.hide(Wifisetting);
                        break;
                    case R.id.tab_wifisetting:
                        transaction.show(Wifisetting);
                        transaction.hide(Vessel);
                        transaction.hide(Submarine);
                        break;
                }
                transaction.commit();
            }
        });

    }
}

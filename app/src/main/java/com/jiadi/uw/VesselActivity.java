package com.jiadi.uw;
/**
 * Created by 北 on 2017/6/24.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class VesselActivity extends Fragment {
    private ImageButton buttonCus1;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vessel, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {//buttonCus1= (ImageButton) buttonCus1.findViewById(R.id.ButtonCus1);
    }

    /*private OnClickListener buttonCus1ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            Intent setIntent = new Intent();
            setIntent.setClass(mContext, Wifisetting.class);
            startActivity(setIntent);
        }
    };*/
}

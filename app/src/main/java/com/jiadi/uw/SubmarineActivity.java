package com.jiadi.uw;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

/**
 * Created by 北 on 2017/6/24.
 */

public class SubmarineActivity extends Fragment {
    private ImageButton bt_Back;
    private ImageButton bt_Stop;
    private ImageButton bt_RightUp;
    private ImageButton bt_Right;
    private ImageButton bt_RightDown;
    private ImageButton bt_LeftUp;
    private ImageButton bt_Left;
    private ImageButton bt_LeftDown;
    private ImageButton bt_Float;
    private ImageButton bt_Dive;
    private ImageButton bt_TakePic;
    private CheckBox cb_Gravity;
    private Context SContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submarine, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        bt_Back=tools.find(view,R.id.btnBack);
        bt_Dive=tools.find(view,R.id.btnDive);
        bt_Float=tools.find(view,R.id.btnFloat);
        bt_Left=tools.find(view,R.id.btnLeft1);
        bt_LeftDown=tools.find(view,R.id.btnLeftDown);
        bt_LeftUp=tools.find(view,R.id.btnLeftUp);
        bt_Right=tools.find(view,R.id.btnRight1);
        bt_RightDown=tools.find(view,R.id.btnRightDown);
        bt_RightUp=tools.find(view,R.id.btnRightUp);
        bt_Stop=tools.find(view,R.id.btnStop);
        bt_TakePic=tools.find(view,R.id.ButtonTakePic);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SContext=getActivity();
        bt_TakePic.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_Stop.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_RightUp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_Right.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_RightDown.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_Back.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_Dive.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_Float.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_Left.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_LeftDown.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });

        bt_LeftUp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //sendCommand(COMM_VIDEOLEFT);
                        break;
                }
                return false;
            }
        });
    }
}

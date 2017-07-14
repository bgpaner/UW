package com.jiadi.uw;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.jiadi.uw.tools.*;


public class Wifisetting extends Fragment {
    private final int MSG_ID_ERR_CONN = 1001;
    private final int MSG_ID_ERR_SEND = 1002;
    private final int MSG_ID_ERR_RECEIVE = 1003;
    private final int MSG_ID_CON_READ = 1004;
    private final int MSG_ID_CON_SUCCESS = 1005;
    private final int MSG_ID_START_CHECK = 1006;
    private final int MSG_ID_ERR_INIT_READ = 1007;
    private final int MSG_ID_CLEAR_QUIT_FLAG = 1008;

    private final int MSG_ID_LOOP_START = 1010;
    private final int MSG_ID_HEART_BREAK_RECEIVE = 1011;
    private final int MSG_ID_HEART_BREAK_SEND = 1012;
    private final int MSG_ID_LOOP_END = 1013;

    private String ROUTER_CONTROL_URL = "www.baidu.com";
    private String ROUTER_CONTROL_URL_TEST = "www.baidu.com";
    private int ROUTER_CONTROL_PORT = 80;
    private int ROUTER_CONTROL_PORT_TEST = 80;
    private final String WIFI_SSID_PERFIX = "21507";
    private byte[] TEST = {(byte)0xEB, (byte)0x90, (byte)0x46, (byte)0x00, (byte) 0x00};

    private final int WIFI_STATE_UNKNOW = 0x3000;
    private final int WIFI_STATE_DISABLED = 0x3001;
    private final int WIFI_STATE_NOT_CONNECTED = 0x3002;
    private final int WIFI_STATE_CONNECTED = 0x3003;

    private final int STATUS_INIT = 0x2001;
    private Context WContext;
    private TextView mLogText;
    SocketClient mtcpSocket;
    private Thread mThreadClient = null;
    private String loc;
    private StringBuffer sb;
    String content;

    private boolean m4test = false;
    private boolean mThreadFlag = false;
    private boolean bReaddyToSendCmd = false;
    private boolean bHeartBreakFlag = false;

    private int mWifiStatus = STATUS_INIT;
    private final int STATUS_CONNECTED = 0x2003;
    private int mHeartBreakCounter = 0;
    private final byte COMMAND_PERFIX = -1;
    private boolean mQuitFlag = false;
    private int mLastCounter = 0;

    private final int HEART_BREAK_CHECK_INTERVAL = 8000;//ms
    private final int QUIT_BUTTON_PRESS_INTERVAL = 2500;//ms
    private final int HEART_BREAK_SEND_INTERVAL = 2500;//ms

    private TextView Text_ip;
    private TextView Text_port;
    private Button Button_connect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wifisetting, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        Text_ip = tools.find(view, R.id.editText1_ip);
        Text_port = tools.find(view, R.id.editText2_port);
        Button_connect = tools.find(view, R.id.button_connect);
        mLogText=tools.find(view,R.id.textView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ROUTER_CONTROL_URL = String.valueOf(Text_ip);
        WContext=getActivity();
        Button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!m4test) {
                    connectToRouter(m4test);
                }
            }
        });

    }

    private void sendCommand(byte[] data) {
        if (mWifiStatus != STATUS_CONNECTED || null == mtcpSocket) {
            mLogText.setText("状态异常, 无法发送命令 ...." + data.toString());
            return;
        }

        if (!bReaddyToSendCmd) {
            mLogText.setText("please wait 1 second to send msg ....");
            return;
        }

        try {
            mtcpSocket.sendMsg(data);
            dialog(WContext, null, null, "success", null, -1);
        } catch (Exception e) {
            Log.i("Socket", e.getMessage() != null ? e.getMessage().toString() : "sendCommand error!");
            dialog(WContext, null, null, "sendCommand try失败：" + e.getMessage().toString(), null, -1);
            //Toast.makeText(mContext, "发送消息给路由器失败  ：" + e.getMessage(),
            //        Toast.LENGTH_SHORT).show();
        }

    }

    private void handleCallback(byte[] command) {
        if (null == command || command.length != Constant.COMMAND_LENGTH) {
            return;
        }

        byte cmd1 = command[1];
        byte cmd2 = command[2];
        //byte cmd3 = command[3];

        if (command[0] != COMMAND_PERFIX || command[Constant.COMMAND_LENGTH - 1] != COMMAND_PERFIX) {
            return;
        }

        if (cmd1 != 0x03) {
            Log.i("Socket", "unknow command from router, ignor it! cmd1=" + cmd1);
            return;
        }

        switch (cmd2) {
            case (byte) 0x01:
                mLogText.setText("收到小车心跳包 ！");
                handleHeartBreak();
                break;
            case (byte) 0x02:
                handleHeartBreak();
                break;
            default:

                break;
        }
    }

    private int getWifiStatus() {
        int status = WIFI_STATE_UNKNOW;
        WifiManager mWifiMng = (WifiManager)WContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        switch (mWifiMng.getWifiState()) {
            case WifiManager.WIFI_STATE_DISABLED:
            case WifiManager.WIFI_STATE_DISABLING:
            case WifiManager.WIFI_STATE_ENABLING:
            case WifiManager.WIFI_STATE_UNKNOWN:
                status = WIFI_STATE_DISABLED;
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                status = WIFI_STATE_NOT_CONNECTED;
                ConnectivityManager conMan = (ConnectivityManager) WContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo.State wifiState = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                if (NetworkInfo.State.CONNECTED == wifiState) {
                    WifiInfo info = mWifiMng.getConnectionInfo();
                    if (null != info) {
                        String bSSID = info.getBSSID();
                        String SSID = info.getSSID();
                        Log.i("Socket", "getWifiStatus bssid=" + bSSID + " ssid=" + SSID);
                        if (null != SSID && SSID.length() > 0) {
                            if (SSID.toLowerCase().contains(WIFI_SSID_PERFIX)) {
                                status = WIFI_STATE_CONNECTED;
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return status;
    }

    private void connectToRouter(boolean isTest) {
        int status = getWifiStatus();
        if (WIFI_STATE_CONNECTED == status || isTest) {
            mThreadFlag = true;
            mThreadClient = new Thread(mRunnable);
            mThreadClient.start();
        } else if (WIFI_STATE_NOT_CONNECTED == status) {
            mLogText.setText("初始化连接路由器失败，wifi未连接，或者路由器状态异常！！");
        } else {
            mLogText.setText("初始化连接路由器失败，wifi未开启，请手动开启后重试！");
        }
    }

    private void initWifiConnection() {
        mWifiStatus = STATUS_INIT;
        Log.i("Socket", "initWifiConnection");
        try {
            if (mtcpSocket != null) {
                mtcpSocket.closeSocket();
            }
            String clientUrl = ROUTER_CONTROL_URL;
            int clientPort = ROUTER_CONTROL_PORT;
            if (m4test) {
                clientUrl = ROUTER_CONTROL_URL_TEST;
                clientPort = ROUTER_CONTROL_PORT_TEST;
            }
            mtcpSocket = new SocketClient(clientUrl, clientPort);
            Log.i("Socket", "Wifi Connect created ip=" + clientUrl
                    + " port=" + clientPort);
            mWifiStatus = STATUS_CONNECTED;
        } catch (Exception e) {
            mLogText.setText("初始化失败");
            Log.d("Socket", "initWifiConnection return exception! ");
        }

        Message msg = new Message();
        if (mWifiStatus != STATUS_CONNECTED || null == mtcpSocket) {
            msg.what = MSG_ID_ERR_CONN;
        } else {
            msg.what = MSG_ID_CON_SUCCESS;
        }
        mLogText.setText("正在初始化.....稍等");
        mHandler.sendMessage(msg);
        mLogText.setText("初始化成功");
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            BufferedInputStream is = null;
            try {
                //连接服务器
                initWifiConnection();
                mLogText.setText("连接服务器成功");
                is = new BufferedInputStream(mtcpSocket.getInputStream());

            } catch (Exception e) {
                dialog(WContext, null, null, "连接服务器失败：" + e.getMessage().toString(), null, -1);
                Message msg = new Message();
                msg.what = MSG_ID_ERR_INIT_READ;
                mHandler.sendMessage(msg);
                return;
            }

            long lastTicket = System.currentTimeMillis();
            byte[] command = {0, 0, 0, 0, 0};
            int commandLength = 0;
            int i = 0;
            while (mThreadFlag) {
                try {
                    //sendCommand(TEST);
                    byte[] buffer = new byte[is.available()];
                    if (is.read(buffer) > 0) {
                        //printRecBuffer ("receive buffer", buffer, buffer.length);
                    }
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = MSG_ID_ERR_RECEIVE;
                    mHandler.sendMessage(msg);
                }
            }
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("Main", "handle internal Message, id=" + msg.what);

            switch (msg.what) {
                case MSG_ID_ERR_RECEIVE:
                    break;
                case MSG_ID_CON_READ:
                    byte[] command = (byte[]) msg.obj;
                    //mLogText.setText("handle response from router: " + command.toString() );
                    handleCallback(command);
                    break;
                case MSG_ID_ERR_INIT_READ:
                    mLogText.setText("打开监听失败！！");
                    break;
                case MSG_ID_CON_SUCCESS:
                    mLogText.setText("成功连接到路由器!");

                    Message msgStartCheck = new Message();
                    msgStartCheck.what = MSG_ID_START_CHECK;
                    mHandler.sendMessageDelayed(msgStartCheck, 3000);

                    Message msgHB1 = new Message();
                    msgHB1.what = MSG_ID_HEART_BREAK_RECEIVE;//启动心跳包检测循环
                    mHandler.sendMessage(msgHB1);

                    Message msgHB2 = new Message();
                    msgHB2.what = MSG_ID_HEART_BREAK_SEND;//启动心跳包循环发送
                    //mHandler.sendMessage(msgHB2);

                    break;
                case MSG_ID_START_CHECK:
                    mLogText.setText("开始进行自检，请稍等。。。。!!");
                    bReaddyToSendCmd = true;
                    //selfcheck();
                    break;
                case MSG_ID_ERR_CONN:
                    mLogText.setText("连接路由器失败!");
                    break;
                case MSG_ID_CLEAR_QUIT_FLAG:
                    mQuitFlag = false;
                    break;
                case MSG_ID_HEART_BREAK_RECEIVE:
                    if (mHeartBreakCounter == 0) {
                        bHeartBreakFlag = false;

                    } else if (mHeartBreakCounter > 0) {
                        bHeartBreakFlag = true;
                    } else {
                        mLogText.setText("心跳包出现异常，已经忽略***");
                    }
                    Log.i("main", "handle MSG_ID_HEART_BREAK_RECEIVE :flag=" + bHeartBreakFlag);

                    if (mLastCounter == 0 && mHeartBreakCounter > 0) {
                        //startIconAnimation();
                    }
                    mLastCounter = mHeartBreakCounter;
                    mHeartBreakCounter = 0;
                    Message msgHB = new Message();
                    msgHB.what = MSG_ID_HEART_BREAK_RECEIVE;//启动心跳包检测循环
                    mHandler.sendMessageDelayed(msgHB, HEART_BREAK_CHECK_INTERVAL);
                    break;
                case MSG_ID_HEART_BREAK_SEND:
                    Message msgSB = new Message();
                    msgSB.what = MSG_ID_HEART_BREAK_SEND;//循环向路由器发送心跳包
                    Log.i("main", "handle MSG_ID_HEART_BREAK_SEND");

                    //sendCommand(COMM_HEART_BREAK);
                    mHandler.sendMessageDelayed(msgSB, HEART_BREAK_SEND_INTERVAL);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void handleHeartBreak() {
        Log.i("Main", "handleHeartBreak");
        mHeartBreakCounter++;
        bHeartBreakFlag = true;
    }
    Handler handler = new Handler();
    Runnable updateMessage = new Runnable() {

        public void run() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time=format.format(new Date());
            loc = time + "\n";
            loc = loc + sb;
            mLogText.setText(loc);
            handler.postDelayed(updateMessage, 1000);
        }
    };

    Handler saveHandler = new Handler();
    final Runnable saveThread = new Runnable() {

        public void run() {
            content = content + loc + "\r\n";
            saveHandler.postDelayed(saveThread,1000);
        }
    };
}

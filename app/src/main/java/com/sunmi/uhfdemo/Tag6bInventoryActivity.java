package com.sunmi.uhfdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.sunmi.rfid.RFIDManager;
import com.sunmi.rfid.ReaderCall;
import com.sunmi.rfid.constant.CMD;
import com.sunmi.rfid.constant.ParamCts;
import com.sunmi.rfid.entity.DataParameter;
import com.sunmi.uhfdemo.widget.LogList;
import com.sunmi.uhfdemo.widget.TagReal6BList;

import java.util.ArrayList;
import java.util.List;

public class Tag6bInventoryActivity extends AppCompatActivity implements View.OnClickListener {
    public static List<String> list = new ArrayList<>();
    private boolean isScan;
    private boolean isLoop;
    private TextView mStartStop;
    private TagReal6BList mTagReal6BList;
    private LogList mLogList;
    private int count;
    private List<DataParameter> data = new ArrayList<>();
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) return;
            switch (intent.getAction()) {
                case ParamCts.BROADCAST_ON_LOST_CONNECT:
                    Toast.makeText(Tag6bInventoryActivity.this, R.string.rfid_please_check_device_connect, Toast.LENGTH_SHORT).show();
                    break;
                case ParamCts.BROADCAST_BATTER_LOW_ELEC:
                    Toast.makeText(Tag6bInventoryActivity.this, R.string.rfid_low_power_hint, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private ReaderCall mIReaderCall = new ReaderCall() {
        @Override
        public void onSuccess(byte cmd, DataParameter params) throws RemoteException {
            if (cmd == CMD.ISO18000_6B_INVENTORY) {
                if (!isDestroyed()) {
                    runOnUiThread(() -> {
                        if (!isDestroyed()) {
                            String info = getString(R.string.rfid_scan_result, count, list.size());
                            mLogList.writeLog(info, ParamCts.SUCCESS);
                            refreshStartStop(isScan = false);
                        }
                    });
                }
            }
        }

        @Override
        public void onTag(byte cmd, byte state, DataParameter tag) throws RemoteException {
            if (cmd == CMD.ISO18000_6B_INVENTORY) {
                if (!isDestroyed()) {
                    runOnUiThread(() -> {
                        if (!isDestroyed()) {
                            if (state == ParamCts.FOUND_TAG) {
                                String uid = tag.getString(ParamCts.TAG_UID);
                                mLogList.writeLog("new tag:" + uid, ParamCts.SUCCESS);
                                list.add(0, uid);
                                data.add(0, tag);
                            } else {
                                String uid = tag.getString(ParamCts.TAG_UID);
                                mLogList.writeLog("update tag:" + uid, ParamCts.SUCCESS);
                                int index = list.indexOf(uid);
                                data.set(index, tag);
                            }
                            count += 1;
                            mTagReal6BList.setTag(data);
                        }
                    });
                }
            }
        }

        @Override
        public void onFiled(byte cmd, byte errorCode, String msg) throws RemoteException {
            if (cmd == CMD.ISO18000_6B_INVENTORY) {
                if (!isDestroyed()) {
                    runOnUiThread(() -> {
                        if (!isDestroyed()) {
                            mLogList.writeLog(cmd + ":" + msg, errorCode);
                            refreshStartStop(isScan = false);
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag6b_inventory);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.rfid_6b_inventory);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mStartStop = findViewById(R.id.startstop6b);
        mTagReal6BList = findViewById(R.id.tag_real_6b_list);
        mLogList = findViewById(R.id.log_list);
        mStartStop.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ParamCts.BROADCAST_BATTER_LOW_ELEC);
        filter.addAction(ParamCts.BROADCAST_ON_LOST_CONNECT);
        registerReceiver(br, filter);
        RFIDManager.getInstance().getHelper().registerReaderCall(mIReaderCall);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
        RFIDManager.getInstance().getHelper().unregisterReaderCall();
    }

    private void refreshStartStop(boolean start) {
        if (start) {
            mStartStop.setBackgroundResource(R.drawable.button_disenabled_background);
            mStartStop.setText(getResources().getString(R.string.stop_inventory));
            mStartStop.setEnabled(false);
        } else {
            mStartStop.setBackgroundResource(R.drawable.button_background);
            mStartStop.setText(getResources().getString(R.string.start_inventory));
            mStartStop.setEnabled(true);
        }
    }

    private void startInventory() {
        try {
            mLogList.writeLog("start Inventory", ParamCts.SUCCESS);
            list.clear();
            data.clear();
            count = 0;
            mTagReal6BList.clearText();
            mTagReal6BList.clearTag();
            RFIDManager.getInstance().getHelper().iso180006BInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLogList.tryClose()) {
                return true;
            }
        }
        if (keyCode == 288) {
            if (!isLoop && !isScan) {
                isLoop = true;
                refreshStartStop(isScan = true);
                startInventory();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 288) {
            isLoop = false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (R.id.startstop6b == v.getId()) {
            if (!isScan) {
                refreshStartStop(isScan = true);
                startInventory();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mLogList.tryClose()) {
                    return true;
                }
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

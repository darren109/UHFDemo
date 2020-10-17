package com.sunmi.uhfdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.sunmi.rfid.RFIDManager;
import com.sunmi.rfid.ReaderCall;
import com.sunmi.rfid.constant.CMD;
import com.sunmi.rfid.constant.ParamCts;
import com.sunmi.rfid.entity.DataParameter;
import com.sunmi.uhfdemo.widget.LogList;
import com.sunmi.uhfdemo.widget.TagRealList;
import com.sunmi.uhfdemo.widget.spiner.AbstractSpinerAdapter;
import com.sunmi.uhfdemo.widget.spiner.SpinerPopWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tag6cInventoryActivity extends AppCompatActivity implements View.OnClickListener {
    public static List<String> list = new ArrayList<>();
    public List<DataParameter> data = new ArrayList<>();
    private DataParameter result = new DataParameter();
    private List<String> mSessionIdList;
    private List<String> mInventoriedFlagList;
    private LogList mLogList;
    private TextView mStartStop;
    private TextView mSessionIdTextView, mInventoriedFlagTextView;
    private TableRow mDropDownRow1, mDropDownRow2;
    private SpinerPopWindow mSpinerPopWindow1, mSpinerPopWindow2;
    private EditText mRealRoundEditText;
    private EditText mRealIntervalText;
    private TagRealList mTagRealList;
    private int mPos1 = 1, mPos2 = 0;
    private TextView mTagsCountText, mTagsTotalText;
    private TextView mTagsSpeedText, mTagsTimeText, mTagsOpTimeText;
    private boolean mDefault = true;
    private static final int DEFAULT_SESSION_ID = 1;
    private RadioGroup mSelectCommand;
    private int mSelectCmdValue = -1;
    private byte nRepeat;
    private int count;
    private long dtEndInventory, dtStartInventory;
    private int nMaxRSSI, nMinRSSI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag6c_inventory);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.rfid_6c_inventory);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mSessionIdList = new ArrayList<>();
        mInventoriedFlagList = new ArrayList<>();
        mLogList = findViewById(R.id.log_list);
        mStartStop = findViewById(R.id.startstop);
        mSessionIdTextView = findViewById(R.id.session_id_text);
        mInventoriedFlagTextView = findViewById(R.id.inventoried_flag_text);
        mDropDownRow1 = findViewById(R.id.table_row_session_id);
        mDropDownRow2 = findViewById(R.id.table_row_inventoried_flag);
        mTagsCountText = findViewById(R.id.tags_count_text);
        mTagsTotalText = findViewById(R.id.tags_total_text);
        mTagsSpeedText = findViewById(R.id.tags_speed_text);
        mTagsTimeText = findViewById(R.id.tags_time_text);
        mTagsOpTimeText = findViewById(R.id.tags_op_time_text);
        mTagRealList = findViewById(R.id.tag_real_list);
        mRealRoundEditText = findViewById(R.id.real_round_text);
        mRealIntervalText = findViewById(R.id.real_interval_text);
        mStartStop.setOnClickListener(this);

        mDropDownRow1.setEnabled(mSelectCmdValue == 0);
        mDropDownRow2.setEnabled(mSelectCmdValue == 0);
        mDropDownRow1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSpinWindow1();
            }
        });
        mDropDownRow2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSpinWindow2();
            }
        });
        mSessionIdList.clear();
        mInventoriedFlagList.clear();
        String[] lists = getResources().getStringArray(R.array.session_id_list);
        mSessionIdList.addAll(Arrays.asList(lists));
        lists = getResources().getStringArray(R.array.inventoried_flag_list);
        mInventoriedFlagList.addAll(Arrays.asList(lists));
        mSpinerPopWindow1 = new SpinerPopWindow(this);
        mSpinerPopWindow1.refreshData(mSessionIdList, 0);
        mSpinerPopWindow1.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            public void onItemClick(int pos) {
                setSessionIdText(pos);
            }
        });
        mSpinerPopWindow2 = new SpinerPopWindow(this);
        mSpinerPopWindow2.refreshData(mInventoriedFlagList, 0);
        mSpinerPopWindow2.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            public void onItemClick(int pos) {
                setInventoriedFlagText(pos);
            }
        });

        updateView();
        mSelectCommand = findViewById(R.id.comand_select_radio);
        mSelectCommand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cmd_common:
                        mSelectCmdValue = -1;
                        break;
                    case R.id.cmd_S0:
                    default:
                        mSelectCmdValue = 0;
                        break;
                }
                mDropDownRow1.setEnabled(mSelectCmdValue == 0);
                mDropDownRow2.setEnabled(mSelectCmdValue == 0);
            }
        });
        refreshText();
        refreshList();
        RFIDManager.getInstance().getHelper().getBatteryChargeNumTimes();
    }


    public void refresh() {
        result.clear();
        list.clear();
        data.clear();
        refreshList();
        refreshText();
        clearText();
    }

    private void refreshStartStop(boolean start) {
        if (start) {
            mStartStop.setBackgroundResource(R.drawable.button_disenabled_background);
            mStartStop.setText(getResources()
                    .getString(R.string.stop_inventory));
            mStartStop.setEnabled(false);
        } else {
            mStartStop.setBackgroundResource(R.drawable.button_background);
            mStartStop.setText(getResources().getString(
                    R.string.start_inventory));
            mStartStop.setEnabled(true);
        }
    }

    private void setSessionIdText(int pos) {
        if (pos >= 0 && pos < mSessionIdList.size()) {
            String value = mSessionIdList.get(pos);
            mSessionIdTextView.setText(value);
            mPos1 = pos;
        }
    }

    private void setInventoriedFlagText(int pos) {
        if (pos >= 0 && pos < mInventoriedFlagList.size()) {
            String value = mInventoriedFlagList.get(pos);
            mInventoriedFlagTextView.setText(value);
            mPos2 = pos;
        }
    }

    private void showSpinWindow1() {
        mSpinerPopWindow1.setWidth(mDropDownRow1.getWidth());
        mSpinerPopWindow1.showAsDropDown(mDropDownRow1);
    }

    private void showSpinWindow2() {
        mSpinerPopWindow2.setWidth(mDropDownRow2.getWidth());
        mSpinerPopWindow2.showAsDropDown(mDropDownRow2);
    }

    private void updateView() {
        mRealRoundEditText.setText(String.valueOf(nRepeat <= 0 ? 1 : nRepeat));
        if (mDefault) {
            mDefault = false;
            setSessionIdText(DEFAULT_SESSION_ID);
            setInventoriedFlagText(mPos2);
        } else {
            setSessionIdText(mPos1);
            setInventoriedFlagText(mPos2);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startstop) {
            refreshStartStop(isScan = true);
            startInventory();
        }
    }

    public void startInventory() {
        long time = 0;
        if (!TextUtils.isEmpty(mRealIntervalText.getText())) {
            time = Long.parseLong(mRealIntervalText.getText().toString());
        }
        if (time > 255) {
            Toast.makeText(this, getResources().getString(R.string.interval_time_error), Toast.LENGTH_SHORT).show();
            return;
        }
        byte btRealInterval = (byte) time;

        nRepeat = 0;
        String strRepeat = mRealRoundEditText.getText().toString();
        if (strRepeat.length() <= 0) {
            Toast.makeText(this, getResources().getString(R.string.repeat_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        nRepeat = (byte) Integer.parseInt(strRepeat);
        if ((nRepeat & 0xFF) <= 0) {
            Toast.makeText(this, getResources().getString(R.string.repeat_min), Toast.LENGTH_SHORT).show();
            return;
        }
        boolean customizedSession;
        byte btSession = 0;
        byte btTarget = 0;
        if (mSelectCmdValue == 0) {
            customizedSession = true;
            btSession = (byte) (mPos1 & 0xFF);
            btTarget = (byte) (mPos2 & 0xFF);
        } else {
            customizedSession = false;
        }
        refresh();
        mLogList.writeLog("start Inventory", ParamCts.SUCCESS);
        dtStartInventory = System.currentTimeMillis();
        if (customizedSession) {
            RFIDManager.getInstance().getHelper().customizedSessionTargetInventory(btSession, btTarget, (byte) 0x00, (byte) 0x00, btRealInterval, nRepeat);
        } else {
            RFIDManager.getInstance().getHelper().realTimeInventory(nRepeat);
        }
    }

    private void refreshList() {
        mTagRealList.refreshList(data);
    }

    private void refreshText() {
        mTagsCountText.setText(String.valueOf(data.size()));
        mTagsTotalText.setText(String.valueOf(count));

        mTagsSpeedText.setText(String.valueOf(result.getInt(ParamCts.READ_RATE, 0)));
        mTagsTimeText.setText(String.valueOf(dtEndInventory - dtStartInventory));
        if (result.getInt(ParamCts.READ_RATE, 0) > 0) {
            mTagsOpTimeText.setText(String.valueOf(result.getInt(ParamCts.DATA_COUNT, 0) * 1000 / result.getInt(ParamCts.READ_RATE, 1)));
        } else {
            mTagsOpTimeText.setText("0");
        }

        mTagRealList.refreshText(nMinRSSI, nMaxRSSI);
    }

    private void setMaxMinRSSI(int nRSSI) {
        if (nMaxRSSI < nRSSI) {
            nMaxRSSI = nRSSI;
        }

        if (nMinRSSI == 0) {
            nMinRSSI = nRSSI;
        } else if (nMinRSSI > nRSSI) {
            nMinRSSI = nRSSI;
        }
    }

    private void clearText() {
        mTagsCountText.setText("0");
        mTagsTotalText.setText("0");
        mTagsSpeedText.setText("0");
        mTagsTimeText.setText("0");
        mTagsOpTimeText.setText("0");
        mTagRealList.clearText();
    }

    private final ReaderCall mReaderCall = new ReaderCall() {
        @Override
        public void onSuccess(byte cmd, DataParameter params) throws RemoteException {
            if (!isDestroyed()) {
                runOnUiThread(() -> {
                    switch (cmd) {
                        case CMD.REAL_TIME_INVENTORY:
                        case CMD.CUSTOMIZED_SESSION_TARGET_INVENTORY:
                            result.putAll(params);
                            dtStartInventory = result.getLong(ParamCts.START_TIME, dtStartInventory);
                            dtEndInventory = result.getLong(ParamCts.END_TIME, System.currentTimeMillis());
                            if (!isDestroyed()) {
                                runOnUiThread(() -> {
                                    if (!isDestroyed()) {
                                        String info = getString(R.string.rfid_scan_result, count, list.size());
                                        mLogList.writeLog(info, ParamCts.SUCCESS);
                                        refreshStartStop(isScan = false);
                                        refreshText();
                                    }
                                });
                            }
                            break;
                    }
                });
            }
        }

        @Override
        public void onTag(byte cmd, byte state, DataParameter tag) throws RemoteException {
            if (!isDestroyed()) {
                runOnUiThread(() -> {
                    switch (cmd) {
                        case CMD.REAL_TIME_INVENTORY:
                        case CMD.CUSTOMIZED_SESSION_TARGET_INVENTORY:
                            if (!isDestroyed()) {
                                runOnUiThread(() -> {
                                    if (!isDestroyed()) {
                                        if (state == ParamCts.FOUND_TAG) {
                                            String epc = tag.getString(ParamCts.TAG_EPC);
                                            mLogList.writeLog("new tag:" + epc, ParamCts.SUCCESS);
                                            list.add(0, epc);
                                            data.add(0, tag);
                                        } else {
                                            String epc = tag.getString(ParamCts.TAG_EPC);
                                            mLogList.writeLog("update tag:" + epc, ParamCts.SUCCESS);
                                            int index = list.indexOf(epc);
                                            data.set(index, tag);
                                        }
                                        count += 1;
                                        if (!TextUtils.isEmpty(tag.getString(ParamCts.TAG_RSSI))) {
                                            setMaxMinRSSI(Integer.parseInt(tag.getString(ParamCts.TAG_RSSI)));
                                        }
                                        refreshList();
                                    }
                                });
                            }
                            break;
                    }
                });
            }
        }

        @Override
        public void onFailed(byte cmd, byte errorCode, String msg) throws RemoteException {
            if (!isDestroyed()) {
                runOnUiThread(() -> {
                    switch (cmd) {
                        case CMD.REAL_TIME_INVENTORY:
                        case CMD.CUSTOMIZED_SESSION_TARGET_INVENTORY:
                            if (!isDestroyed()) {
                                runOnUiThread(() -> {
                                    if (!isDestroyed()) {
                                        mLogList.writeLog(cmd + ":" + msg, errorCode);
                                        refreshStartStop(isScan = false);
                                    }
                                });
                            }
                            break;
                    }
                });
            }
        }
    };

    private final BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ParamCts.BROADCAST_ON_LOST_CONNECT)) {
                Toast.makeText(Tag6cInventoryActivity.this, R.string.rfid_please_check_device_connect, Toast.LENGTH_SHORT).show();
            } else if (intent.getAction().equals(ParamCts.BROADCAST_BATTER_LOW_ELEC)) {
                byte elec = intent.getByteExtra(ParamCts.BATTERY_REMAINING_PERCENT, (byte) 0x00);
                processElec(elec);
                mLogList.writeLog(getString(R.string.rfid_low_power_hint) + "(" + mElec + "%)", ParamCts.FAIL);
            }
        }
    };

    private int mElec = 0;
    private boolean isLoop;
    private boolean isScan;

    private void processElec(int elec) {
        if (mElec <= 10) {
            Toast.makeText(this, getString(R.string.rfid_low_power_hint) + "(" + mElec + "%)", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ParamCts.BROADCAST_BATTER_LOW_ELEC);
        filter.addAction(ParamCts.BROADCAST_ON_LOST_CONNECT);
        registerReceiver(br, filter);
        RFIDManager.getInstance().getHelper().registerReaderCall(mReaderCall);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
        RFIDManager.getInstance().getHelper().unregisterReaderCall();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

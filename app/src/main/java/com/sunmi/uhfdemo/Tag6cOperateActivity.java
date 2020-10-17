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
import android.view.Menu;
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
import com.sunmi.uhfdemo.utils.StringTool;
import com.sunmi.uhfdemo.widget.HexEditTextBox;
import com.sunmi.uhfdemo.widget.LogList;
import com.sunmi.uhfdemo.widget.TagAccessList;
import com.sunmi.uhfdemo.widget.spiner.AbstractSpinerAdapter;
import com.sunmi.uhfdemo.widget.spiner.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;

public class Tag6cOperateActivity extends AppCompatActivity implements View.OnClickListener {
    private LogList mLogList;
    private TextView mGet, mRead, mSelect, mWrite, mLock, mKill;
    private TextView mTagAccessListText;
    private TableRow mDropDownRow;
    private List<String> mAccessList = new ArrayList<>(Tag6cInventoryActivity.list);
    private List<DataParameter> mDataList = new ArrayList<>();
    private SpinerPopWindow mSpinerPopWindow;

    private HexEditTextBox mPasswordEditText;
    private EditText mStartAddrEditText;
    private EditText mDataLenEditText;
    private HexEditTextBox mDataEditText;
    private HexEditTextBox mLockPasswordEditText;
    private HexEditTextBox mKillPasswordEditText;
    private RadioGroup mGroupAccessAreaType;
    private RadioGroup mGroupLockAreaType;
    private RadioGroup mGroupLockType;
    private TagAccessList mTagAccessList;
    private int mPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag6c_operate);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.rfid_6c_operate);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mLogList = findViewById(R.id.log_list);
        mGet = findViewById(R.id.get);
        mRead = findViewById(R.id.read);
        mSelect = findViewById(R.id.select);
        mWrite = findViewById(R.id.write);
        mLock = findViewById(R.id.lock);
        mKill = findViewById(R.id.kill);
        mGet.setOnClickListener(this);
        mRead.setOnClickListener(this);
        mSelect.setOnClickListener(this);
        mWrite.setOnClickListener(this);
        mLock.setOnClickListener(this);
        mKill.setOnClickListener(this);

        mPasswordEditText = findViewById(R.id.password_text);
        mStartAddrEditText = findViewById(R.id.start_addr_text);
        mDataLenEditText = findViewById(R.id.data_length_text);
        mDataEditText = findViewById(R.id.data_write_text);
        mLockPasswordEditText = findViewById(R.id.lock_password_text);
        mKillPasswordEditText = findViewById(R.id.kill_password_text);

        mGroupAccessAreaType = findViewById(R.id.group_access_area_type);
        mGroupLockAreaType = findViewById(R.id.group_lock_area_type);
        mGroupLockType = findViewById(R.id.group_lock_type);

        mTagAccessListText = findViewById(R.id.tag_access_list_text);
        mDropDownRow = findViewById(R.id.table_row_tag_access_list);

        mTagAccessList = findViewById(R.id.tag_access_list);


        mDropDownRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSpinWindow();
            }
        });

        mAccessList.add("Cancel");

        mSpinerPopWindow = new SpinerPopWindow(this);
        mSpinerPopWindow.refreshData(mAccessList, 0);
        mSpinerPopWindow.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            public void onItemClick(int pos) {
                setAccessSelectText(pos);
            }
        });

        updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(1, 1, 1, "刷新");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
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
            case 1:
                refresh();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        mDataList.clear();
        mTagAccessListText.setText("");
        mPos = -1;
        mStartAddrEditText.setText("");
        mDataLenEditText.setText("");
        mDataEditText.setText("");
        mPasswordEditText.setText("");
        mLockPasswordEditText.setText("");
        mKillPasswordEditText.setText("");
        mGroupAccessAreaType.check(R.id.set_access_area_password);
        mGroupLockAreaType.check(R.id.set_lock_area_access_password);
        mGroupLockType.check(R.id.set_lock_free);
        refreshList();
        refreshText();
        clearText();
    }

    private void setAccessSelectText(int pos) {
        if (pos >= 0 && pos < mAccessList.size()) {
            String value = mAccessList.get(pos);
            mTagAccessListText.setText(value);
            mPos = pos;
        }
    }

    private void showSpinWindow() {
        mSpinerPopWindow.refreshData(mAccessList, 0);
        mSpinerPopWindow.setWidth(mDropDownRow.getWidth());
        mSpinerPopWindow.showAsDropDown(mDropDownRow);
    }

    private void updateView() {
        if (mPos < 0) {
            mPos = mAccessList.size() - 1;
        }
        setAccessSelectText(mPos);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get: {
                RFIDManager.getInstance().getHelper().getAccessEpcMatch();
                break;
            }
            case R.id.select: {
                if (mPos == -1 || mPos > mAccessList.size() - 2) {
                    RFIDManager.getInstance().getHelper().cancelAccessEpcMatch();
                } else {
                    byte[] btAryEpc = null;
                    try {
                        String[] result = StringTool.stringToStringArray(mAccessList.get(mPos).toUpperCase(), 2);
                        btAryEpc = StringTool.stringArrayToByteArray(result, result.length);
                    } catch (Exception e) {
                        String msg = getResources().getString(R.string.param_unknown_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    if (btAryEpc == null) {
                        String msg = getResources().getString(R.string.param_unknown_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    RFIDManager.getInstance().getHelper().setAccessEpcMatch((byte) (btAryEpc.length & 0xFF), btAryEpc);
                }
                break;
            }
            case R.id.read:
            case R.id.write: {
                byte btMemBank = 0x00;
                byte btWordAdd = 0x00;
                byte btWordCnt = 0x00;
                byte[] btAryPassWord = null;
                if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_password) {
                    btMemBank = 0x00;
                } else if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_epc) {
                    btMemBank = 0x01;
                } else if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_tid) {
                    btMemBank = 0x02;
                } else if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_user) {
                    btMemBank = 0x03;
                }

                try {
                    btWordAdd = (byte) Integer.parseInt(mStartAddrEditText.getText().toString());
                } catch (Exception e) {
                    String msg = getResources().getString(R.string.param_start_addr_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                try {
                    String[] reslut = StringTool.stringToStringArray(mPasswordEditText.getText().toString().toUpperCase(), 2);
                    btAryPassWord = StringTool.stringArrayToByteArray(reslut, 4);
                } catch (Exception e) {
                    String msg = getResources().getString(R.string.param_password_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                if (view.getId() == R.id.read) {
                    try {
                        btWordCnt = (byte) (Integer.parseInt(mDataLenEditText.getText().toString()));
                    } catch (Exception e) {
                        String msg = getResources().getString(R.string.param_data_len_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    if ((btWordCnt & 0xFF) <= 0) {
                        String msg = getResources().getString(R.string.param_data_len_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    RFIDManager.getInstance().getHelper().readTag(btMemBank, btWordAdd, btWordCnt, btAryPassWord);
                } else {
                    byte[] btAryData = null;
                    String[] result = null;
                    try {
                        result = StringTool.stringToStringArray(mDataEditText.getText().toString().toUpperCase(), 2);
                        btAryData = StringTool.stringArrayToByteArray(result, result.length);
                        btWordCnt = (byte) ((result.length / 2 + result.length % 2) & 0xFF);
                    } catch (Exception e) {
                        String msg = getResources().getString(R.string.param_data_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    if (btAryData == null || btAryData.length <= 0) {
                        String msg = getResources().getString(R.string.param_data_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    if (btAryPassWord == null || btAryPassWord.length < 4) {
                        String msg = getResources().getString(R.string.param_password_error);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        mLogList.writeLog(msg, ParamCts.FAIL);
                        return;
                    }

                    mDataLenEditText.setText(String.valueOf(btWordCnt & 0xFF));

                    RFIDManager.getInstance().getHelper().writeTag(btAryPassWord, btMemBank, btWordAdd, btWordCnt, btAryData);
                }

                break;
            }
            case R.id.lock: {
                byte btMemBank = 0x00;
                byte btLockType = 0x00;
                byte[] btAryPassWord = null;
                if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_access_password) {
                    btMemBank = 0x04;
                } else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_kill_password) {
                    btMemBank = 0x05;
                } else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_epc) {
                    btMemBank = 0x03;
                } else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_tid) {
                    btMemBank = 0x02;
                } else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_user) {
                    btMemBank = 0x01;
                }

                if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_free) {
                    btLockType = 0x00;
                } else if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_free_ever) {
                    btLockType = 0x02;
                } else if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_lock) {
                    btLockType = 0x01;
                } else if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_lock_ever) {
                    btLockType = 0x03;
                }

                try {
                    String[] reslut = StringTool.stringToStringArray(mLockPasswordEditText.getText().toString().toUpperCase(), 2);
                    btAryPassWord = StringTool.stringArrayToByteArray(reslut, 4);
                } catch (Exception e) {
                    String msg = getResources().getString(R.string.param_lockpassword_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                if (btAryPassWord == null || btAryPassWord.length < 4) {
                    String msg = getResources().getString(R.string.param_lockpassword_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                RFIDManager.getInstance().getHelper().lockTag(btAryPassWord, btMemBank, btLockType);
                break;
            }
            case R.id.kill: {
                byte[] btAryPassWord = null;
                try {
                    String[] reslut = StringTool.stringToStringArray(mKillPasswordEditText.getText().toString().toUpperCase(), 2);
                    btAryPassWord = StringTool.stringArrayToByteArray(reslut, 4);
                } catch (Exception e) {
                    String msg = getResources().getString(R.string.param_killpassword_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                if (btAryPassWord == null || btAryPassWord.length < 4) {
                    String msg = getResources().getString(R.string.param_killpassword_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                RFIDManager.getInstance().getHelper().killTag(btAryPassWord);
                break;
            }
        }
    }

    private void refreshList() {
        mTagAccessList.refreshList(mDataList);
    }

    private void refreshText() {
        mTagAccessList.refreshText();
    }

    private void clearText() {
        mTagAccessList.clearText();
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) return;
            switch (intent.getAction()) {
                case ParamCts.BROADCAST_ON_LOST_CONNECT:
                    Toast.makeText(Tag6cOperateActivity.this, R.string.rfid_please_check_device_connect, Toast.LENGTH_SHORT).show();
                    break;
                case ParamCts.BROADCAST_BATTER_LOW_ELEC:
                    Toast.makeText(Tag6cOperateActivity.this, R.string.rfid_low_power_hint, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private final ReaderCall mReaderCall = new ReaderCall() {
        @Override
        public void onSuccess(byte cmd, DataParameter params) throws RemoteException {
            if (!isDestroyed()) {
                runOnUiThread(() -> {
                    mLogList.writeLog(String.format("handle 0x%02X success", cmd), ParamCts.SUCCESS);
                    switch (cmd) {
                        case CMD.GET_ACCESS_EPC_MATCH:
                            mTagAccessListText.setText(params.getString(ParamCts.TAG_ACCESS_EPC_MATCH, ""));
                            break;
                        case CMD.READ_TAG:
                        case CMD.WRITE_TAG:
                        case CMD.LOCK_TAG:
                        case CMD.KILL_TAG:
                            int index = -1;
                            for (int i = 0; i < mDataList.size(); i++) {
                                if (params.getString(ParamCts.TAG_EPC).equals(mDataList.get(i).getString(ParamCts.TAG_EPC))) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1) {
                                mDataList.get(index).putAll(params);
                                refreshList();
                                refreshText();
                            } else {
                                mDataList.add(params);
                            }
                            /*if (!TextUtils.isEmpty(params.getString(ParamCts.TAG_EPC)) && mPos != -1 && mPos < mAccessList.size() - 2) {
                                mAccessList.set(mPos, params.getString(ParamCts.TAG_EPC));
                            }*/
                            break;
                    }
                });
            }
        }

        @Override
        public void onTag(byte cmd, byte state, DataParameter tag) throws RemoteException {}

        @Override
        public void onFailed(byte cmd, byte errorCode, String msg) throws RemoteException {
            if (!isDestroyed()) {
                runOnUiThread(() -> mLogList.writeLog(String.format("handle 0x%02X fail, error code: 0x%02X %s", cmd, errorCode, msg), ParamCts.FAIL));
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLogList.tryClose()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RFIDManager.getInstance().getHelper().registerReaderCall(mReaderCall);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ParamCts.BROADCAST_ON_LOST_CONNECT);
        filter.addAction(ParamCts.BROADCAST_BATTER_LOW_ELEC);
        registerReceiver(br, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RFIDManager.getInstance().getHelper().unregisterReaderCall();
        unregisterReceiver(br);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

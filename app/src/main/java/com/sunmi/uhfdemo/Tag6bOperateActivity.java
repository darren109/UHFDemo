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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.sunmi.uhfdemo.widget.spiner.AbstractSpinerAdapter;
import com.sunmi.uhfdemo.widget.spiner.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;

public class Tag6bOperateActivity extends AppCompatActivity implements View.OnClickListener {
    private LogList mLogList;
    private TextView mRead, mWrite, mSetWP, mGetWP;
    private TextView mTag6BUIDListText;
    private TableRow mDropDownRow;
    private List<String> mUIDList = new ArrayList<>(Tag6bInventoryActivity.list);
    private SpinerPopWindow mSpinerPopWindow;
    private HexEditTextBox mReadStartAddrEditText;
    private HexEditTextBox mReadDataLenEditText;
    private HexEditTextBox mReadDataEditText;
    private HexEditTextBox mWriteStartAddrEditText;
    private HexEditTextBox mWriteDataLenEditText;
    private HexEditTextBox mWriteDataEditText;
    private HexEditTextBox mSetWPAddrEditText;
    private HexEditTextBox mGetWPAddrEditText;
    private EditText mWPStatusText;
    private int mPos = -1;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) return;
            switch (intent.getAction()) {
                case ParamCts.BROADCAST_ON_LOST_CONNECT:
                    Toast.makeText(Tag6bOperateActivity.this, R.string.rfid_please_check_device_connect, Toast.LENGTH_SHORT).show();
                    break;
                case ParamCts.BROADCAST_BATTER_LOW_ELEC:
                    Toast.makeText(Tag6bOperateActivity.this, R.string.rfid_low_power_hint, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private ReaderCall mReaderCall = new ReaderCall() {
        @Override
        public void onSuccess(byte cmd, DataParameter params) throws RemoteException {
            if (!isDestroyed()) {
                runOnUiThread(() -> {
                    switch (cmd) {
                        case CMD.ISO18000_6B_READ_TAG:
                            mReadDataEditText.setText(params.getString(ParamCts.TAG_DATA));
                            break;
                        case CMD.ISO18000_6B_WRITE_TAG:
                            Toast.makeText(Tag6bOperateActivity.this, "write data success", Toast.LENGTH_SHORT).show();
                            mLogList.writeLog("write data size: " + params.getByte(ParamCts.TAG_DATA_LEN), ParamCts.SUCCESS);
                            break;
                        case CMD.ISO18000_6B_LOCK_TAG:
                            String msg = "";
                            switch (params.getByte(ParamCts.TAG_STATUS) & 0xFF) {
                                case 0x00:
                                    msg = "Successfully locked";
                                    break;
                                case 0xFE:
                                    msg = "Locked State";
                                    break;
                                case 0xFF:
                                    msg = "Unable to lock";
                                    break;
                                default:
                                    break;
                            }
                            if (!msg.isEmpty()) {
                                Toast.makeText(Tag6bOperateActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            mLogList.writeLog("Lock:" + msg, ParamCts.SUCCESS);
                        case CMD.ISO18000_6B_QUERY_LOCK_TAG:
                            switch (params.getByte(ParamCts.TAG_STATUS) & 0xFF) {
                                case 0x00:
                                    mWPStatusText.setText(getResources().getString(R.string.unlocked));
                                    break;
                                case 0xFE:
                                    mWPStatusText.setText(getResources().getString(R.string.locked));
                                    break;
                            }
                            break;
                        default:
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
                runOnUiThread(() -> mLogList.writeLog(String.format("%02X", cmd) + "," + String.format("%02X", errorCode) + "," + msg, errorCode));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag6b_operate);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.rfid_6b_operate);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mUIDList.add("Cancel");
        mLogList = findViewById(R.id.log_list);
        mRead = findViewById(R.id.read);
        mWrite = findViewById(R.id.write);
        mSetWP = findViewById(R.id.set_wp_ever);
        mGetWP = findViewById(R.id.get_wp_ever);

        mRead.setOnClickListener(this);
        mWrite.setOnClickListener(this);
        mWrite.setOnClickListener(this);
        mSetWP.setOnClickListener(this);
        mGetWP.setOnClickListener(this);

        mReadStartAddrEditText = findViewById(R.id.read_start_addr_text);
        mReadDataLenEditText = findViewById(R.id.read_data_len_text);
        mReadDataEditText = findViewById(R.id.read_data_show_text);
        mWriteStartAddrEditText = findViewById(R.id.write_start_addr_text);
        mWriteDataLenEditText = findViewById(R.id.write_data_len_text);
        mWriteDataEditText = findViewById(R.id.write_data_show_text);

        mSetWPAddrEditText = findViewById(R.id.tag_6b_wp_ever_addr_text);
        mGetWPAddrEditText = findViewById(R.id.tag_6b_get_wp_ever_addr_text);
        mWPStatusText = findViewById(R.id.tag_6b_wp_ever_status_text);

        mTag6BUIDListText = findViewById(R.id.tag_6b_uid_list_text);
        mDropDownRow = findViewById(R.id.table_row_tag_6b_uid_list);
        mDropDownRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSpinWindow();
            }
        });

        mSpinerPopWindow = new SpinerPopWindow(this);
        mSpinerPopWindow.refreshData(mUIDList, 0);
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
        mReadStartAddrEditText.setText("");
        mReadDataLenEditText.setText("");
        mReadDataEditText.setText("");
        mWriteStartAddrEditText.setText("");
        mWriteDataLenEditText.setText("");
        mWriteDataEditText.setText("");
        mSetWPAddrEditText.setText("");
        mGetWPAddrEditText.setText("");
        mWPStatusText.setText("");
    }

    private void setAccessSelectText(int pos) {
        if (pos >= 0 && pos < mUIDList.size()) {
            String value = mUIDList.get(pos);
            mTag6BUIDListText.setText(value);
            mPos = pos;
        }
    }

    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(mDropDownRow.getWidth());
        mSpinerPopWindow.showAsDropDown(mDropDownRow);
    }

    private void updateView() {
        if (mPos < 0) {
            mPos = 0;
        }
        setAccessSelectText(mPos);
    }

    @Override
    public void onClick(View v) {
        String strText = mTag6BUIDListText.getText().toString();
        String[] result = null;
        byte[] btAryUID = null;

        if (strText.length() <= 0) {
            String msg = getString(R.string.uid_select);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            mLogList.writeLog(msg, ParamCts.FAIL);
            return;
        }


        try {
            result = StringTool.stringToStringArray(strText.toUpperCase(), 2);
            btAryUID = StringTool.stringArrayToByteArray(result, 8);
        } catch (Exception e) {
            String msg = getString(R.string.uid_error);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            mLogList.writeLog(msg, ParamCts.FAIL);
            return;
        }

        switch (v.getId()) {
            case R.id.read: {
                byte btWordAdd = 0x00, btWordCnt = 0x00;

                try {
                    btWordAdd = (byte) (Integer.parseInt(mReadStartAddrEditText.getText().toString(), 16) & 0xFF);
                } catch (Exception e) {
                    Toast.makeText(
                            this,
                            getString(R.string.param_read_start_addr_error),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    btWordCnt = (byte) (Integer.parseInt(mReadDataLenEditText.getText().toString(), 16) & 0xFF);
                } catch (Exception e) {
                    String msg = getString(R.string.param_read_data_len_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                RFIDManager.getInstance().getHelper().iso180006BReadTag(btAryUID, btWordAdd, btWordCnt);
                break;
            }
            case R.id.write: {
                byte btWordAdd = 0x00, btWordCnt = 0x00;
                byte btAryBuffer[] = null;

                try {
                    result = StringTool.stringToStringArray(mWriteDataEditText.getText().toString().toUpperCase(), 2);
                    btAryBuffer = StringTool.stringArrayToByteArray(result, result.length);
                } catch (Exception e) {
                    String msg = getString(R.string.param_write_data_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                try {
                    btWordAdd = (byte) (Integer.parseInt(mWriteStartAddrEditText.getText().toString(), 16) & 0xFF);
                } catch (Exception e) {
                    String msg = getString(R.string.param_write_start_addr_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }
                btWordCnt = (byte) (result.length & 0xFF);
                mWriteDataLenEditText.setText(String.format("%02X", btWordCnt));
                RFIDManager.getInstance().getHelper().iso180006BWriteTag(btAryUID, btWordAdd, btWordCnt, btAryBuffer);
                break;
            }
            case R.id.set_wp_ever: {
                byte btWordAdd = 0x00;

                try {
                    btWordAdd = (byte) (Integer.parseInt(mSetWPAddrEditText.getText().toString(), 16) & 0xFF);
                } catch (Exception e) {
                    String msg = getString(R.string.param_wp_ever_addr_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                RFIDManager.getInstance().getHelper().iso180006BLockTag(btAryUID, btWordAdd);

                break;
            }
            case R.id.get_wp_ever: {
                byte btWordAdd = 0x00;

                try {
                    btWordAdd = (byte) (Integer.parseInt(mGetWPAddrEditText.getText().toString(), 16) & 0xFF);
                } catch (Exception e) {
                    String msg = getString(R.string.param_get_wp_ever_addr_error);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mLogList.writeLog(msg, ParamCts.FAIL);
                    return;
                }

                RFIDManager.getInstance().getHelper().iso180006BQueryLockTag(btAryUID, btWordAdd);
                break;
            }
        }
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLogList.tryClose()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

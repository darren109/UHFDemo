package com.sunmi.rfid;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * RFID Manager
 */
public class RFIDManager {
    private static final String TAG = "RFIDManager";
    private static final int RE_CONNECT = 20;

    private RFIDManager() {
    }

    private static class SingletonInstance {
        private static RFIDManager sInstance = new RFIDManager();
    }

    public static RFIDManager getInstance() {
        return SingletonInstance.sInstance;
    }

    protected boolean printLog;
    private Context appContext;
    private boolean isConnect;
    private int count;
    private RFIDHelper helper;
    private IScanRFIDInterface scanInterface;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            scanInterface = IScanRFIDInterface.Stub.asInterface(service);
            if (printLog) Log.i(TAG, "Service Connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            scanInterface = null;
            if (printLog) Log.w(TAG, "Service error Disconnected!");
        }
    };

    public void connect(Context context) {
        if (context == null) throw new RuntimeException("Parameter context is Null");
        this.appContext = context.getApplicationContext();
        if (scanInterface == null) {
            PackageInfo packageInfo = getPackageInfo(appContext, "com.sunmi.scanner");
            if (packageInfo != null) {
                if (packageInfo.versionCode >= 20032002) {
                    Intent intent = new Intent();
                    intent.setPackage("com.sunmi.scanner");
                    intent.setAction("com.sunmi.scanner.IScanRFIDInterface");
                    appContext.startService(intent);
                    appContext.bindService(intent, conn, Service.BIND_AUTO_CREATE);
                    isConnect = true;
                } else {
                    if (printLog) Log.w(TAG, "connect: Service is not supported.");
                }
            } else {
                if (printLog) Log.w(TAG, "connect: un found service.");
            }
        } else {
            if (printLog) Log.i(TAG, "connect: scanner rfid is connect.");
        }
    }

    public void disconnect() {
        try {
            if (scanInterface != null) {
                isConnect = false;
                appContext.unbindService(conn);
                scanInterface = null;
                appContext = null;
                if (printLog) Log.i(TAG, "Service Disconnected!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "disconnect: error", e);
        }
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }

    public RFIDHelper getHelper() {
        if (helper == null) {
            if (scanInterface != null) {
                helper = new ServicesHelper(scanInterface);
            } else if (isConnect && count > RE_CONNECT) {
                count++;
                SystemClock.sleep(50);
                return getHelper();
            } else {
                if (printLog) {
                    Log.e(TAG, "getHelper: Please call connect() method first or Service is not supported.");
                }
                throw new RuntimeException("Please call connect() method first or Service is not supported.");
            }
        }
        count = 0;
        return helper;
    }

    /**
     * check the app is installed
     *
     * @return
     */
    private PackageInfo getPackageInfo(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo;
    }
}

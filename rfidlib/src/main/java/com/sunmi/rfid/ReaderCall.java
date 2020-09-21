package com.sunmi.rfid;

import android.os.RemoteException;

import com.sunmi.rfid.entity.DataParameter;

public abstract class ReaderCall extends IReaderCall.Stub {
    @Override
    public abstract void onSuccess(byte cmd, DataParameter params) throws RemoteException;

    @Override
    public abstract void onTag(byte cmd, byte state, DataParameter tag) throws RemoteException;

    @Override
    public abstract void onFiled(byte cmd, byte errorCode, String msg) throws RemoteException;
}

package com.sunmi.rfid;

/**
 * @author darren by Darren1009@qq.com - 2020/10/10
 */
public abstract class FirmwareUpdateCall extends IFirmwareUpdateCall.Stub {
    /**
     * 升级成功
     */
    @Override
    public abstract void onSuccess();

    /**
     * 升级进度
     *
     * @param progress 百分比
     */
    @Override
    public abstract void onProgress(int progress);

    /**
     * 升级失败
     *
     * @param code -1: update failed
     *             -2: file not exist
     *             -3: file suffix not .bin or binFile is null
     * @param msg  information
     */
    @Override
    public abstract void onFailed(int code, String msg);
}
